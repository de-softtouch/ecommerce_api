package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.controller.member.ResetPasswordRequest;
import com.api.ecommerceweb.dto.AddressDTO;
import com.api.ecommerceweb.dto.UserDTO;
import com.api.ecommerceweb.mapper.AddressMapper;
import com.api.ecommerceweb.mapper.UserMapper;
import com.api.ecommerceweb.model.Address;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.repository.AddressRepository;
import com.api.ecommerceweb.repository.UserRepository;
import com.api.ecommerceweb.request.AccountUpdateRequest;
import com.api.ecommerceweb.request.AddressRequest;
import com.api.ecommerceweb.security.CustomUserDetails;
import com.api.ecommerceweb.utils.FileStorageUtil;
import com.api.ecommerceweb.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component("MemberHelper")
@RequiredArgsConstructor
@Slf4j
public class MemberHelper {

    private final UserRepository userRepo;
    private final FileStorageUtil fileStorageUtil;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepo;


    public ResponseEntity<?> getCurrentUserDetails() {
        CustomUserDetails principal = getPrincipal();
        UserDTO dto = UserMapper.toUserDto(principal.getUser());
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        //get current user details
        CustomUserDetails principal = getPrincipal();
        User user = principal.getUser();
        user.setFullName(accountUpdateRequest.getFullName());
        user.setGender(accountUpdateRequest.getGender());
        map.put("fullName", accountUpdateRequest.getFullName());
        map.put("gender", accountUpdateRequest.getGender());
        //check file is image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            boolean image = fileStorageUtil.isImage(multipartFile);
            if (!image)
                return ResponseEntity.badRequest().body("File is not image type");
            //save file
            String folder = user.getId().toString();
            String savedFileName = fileStorageUtil.storeFile(multipartFile, folder);
            map.put("profileImg", savedFileName);
            user.setProfileImg(savedFileName);
        }
        userRepo.save(user);
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> getFiles() {
        CustomUserDetails principal = getPrincipal();
        List<File> files = fileStorageUtil.getFiles(principal.getUser().getId().toString());
        return ResponseEntity.ok(
                files.stream().map(File::getName).collect(Collectors.toList())
        );
    }

    public ResponseEntity<?> getVerificationCode() {
        CustomUserDetails principal = getPrincipal();
        User user = principal.getUser();
        try {
            String verificationCode = mailUtil.generateVerificationCode();
            mailUtil.sendVerificationCode(principal.getUsername(), verificationCode);
            user.setVerificationCode(verificationCode);
            userRepo.save(user);
            return ResponseEntity.ok("Verification code was sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Send code error - {}", e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        CustomUserDetails principal = getPrincipal();


        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), principal.getPassword()))
            return ResponseEntity.badRequest().body("Password does not match old password!");

        if (!resetPasswordRequest.getVerificationCode().equals(principal.getUser().getVerificationCode()))
            return ResponseEntity.badRequest().body("Verification code does not match!");

        User user = principal.getUser();
        user.setVerificationCode(null);
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Reset password success");
    }

    public ResponseEntity<?> getAddresses() {
        CustomUserDetails principal = getPrincipal();
        List<Address> addresses = addressRepo.findByUserAndStatus(principal.getUser(), 1);
        List<AddressDTO> rs = addresses.stream()
                .map(AddressMapper::toAddressDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {
        List<Address> savedAddresses = addressRepo.findByUserAndStatus(getPrincipal().getUser(), 1);

        Address address;
        if (addressRequest.getId() != null && addressRepo.existsById(addressRequest.getId())) {
            address = addressRepo.getById(addressRequest.getId());
            if (savedAddresses.size() == 1)
                address.setIsDefault(1);
        } else {
            address = new Address();
            address.setUser(getPrincipal().getUser());
        }
        address.setFullName(addressRequest.getFullName());
        address.setPhone(addressRequest.getPhone());
        address.setType(addressRequest.getType());
        address.setAddressDetails(addressRequest.getAddressDetails());
        address.setPostCode(addressRequest.getPostCode());
        if (addressRequest.getIsDefault() != null && addressRequest.getIsDefault() == 1) {
            savedAddresses
                    .forEach(a -> {
                        a.setIsDefault(0);
                        addressRepo.save(a);
                    });
            address.setIsDefault(1);
        } else if (savedAddresses.size() == 0) {
            address.setIsDefault(1);
        }
        addressRepo.save(address);
        return ResponseEntity.ok("Update address success");
    }

    public ResponseEntity<?> deleteAddress(long id) {
        Optional<Address> optionalAddress = addressRepo.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setStatus(0);
            addressRepo.save(address);
            return ResponseEntity.ok("Delete address success");
        }
        return ResponseEntity.badRequest().body("Can not find address with id: " + id);
    }
}
