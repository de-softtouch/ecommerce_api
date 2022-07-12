package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.helper.*;
import com.api.ecommerceweb.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final UserHelper userHelper;
    private final FeedbackHelper feedbackHelper;
    private final OrderHelper orderHelper;
    private final ProductHelper productHelper;
    private final FileUploadHelper fileUploadHelper;
    private final ShopHelper shopHelper;


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCurrentUserDetails(
            @ModelAttribute @Valid AccountUpdateRequest accountUpdateRequest,
            @RequestParam(value = "avt", required = false) MultipartFile multipartFile) {
        return userHelper.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    @GetMapping("/users/me/addresses")
    public ResponseEntity<?> getAddresses() {
        return userHelper.getAddresses();
    }

    @PostMapping("/users/me/addresses")
    public ResponseEntity<?> updateAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return userHelper.updateAddress(addressRequest);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable long id) {
        return userHelper.deleteAddress(id);
    }


    @GetMapping("/verification")
    public ResponseEntity<?> getVerificationCode() {
        return userHelper.getVerificationCode();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        return userHelper.resetPassword(resetPasswordRequest);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam Map<String, String> params) {
        return userHelper.getOrders(params);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> orders(@RequestBody @Valid OrderRequest request) {
        return orderHelper.order(request);
    }


    @PostMapping("/orders/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) {
        return userHelper.cancelOrder(id);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<?> getFeedBacks() {
        return userHelper.getFeedBacks();
    }

    @PostMapping("/users/me/feedbacks")
    public ResponseEntity<?> postFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        return userHelper.postFeedback(feedbackRequest);
    }

    @GetMapping("/users/me/shop/feedbacks")
    public ResponseEntity<?> getFeedbacks() {
        return feedbackHelper.getShopFeedbacks();
    }

    @GetMapping("/users/me/shop/products/{productId}")
    public ResponseEntity<?> getProductDetailInShop(@PathVariable("productId") Long productId) {
        return productHelper.getShopProductDetail(productId);
    }

    @PostMapping("/users/shops")
    public ResponseEntity<?> registerShop(@RequestBody ShopRequest shopRequest) {
        return userHelper.registerShop(shopRequest);
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUserDetail() {
        log.info("get current user detail");
        return userHelper.getCurrentUserDetails();
    }

    @PostMapping("/users/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody @Valid UpdateUserRequest request) {
        return userHelper.updateCurrentUserDetail(request);
    }

    @PostMapping("/users/update")
    public ResponseEntity<?> updateAccount(@RequestBody @Valid UpdateAccountRequest updateAccountRequest) {
        return userHelper.updateMemberAccount(updateAccountRequest);
    }


    /*
    registration shop
     */
    @PostMapping("/shops/registration")
    public ResponseEntity<?> registrationShop(@RequestBody @Valid ShopRequest shopRequest) {
        return userHelper.registerShop(shopRequest);
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShopDetail(@PathVariable Long id) {
        return shopHelper.getShopDetail(id);
    }

    /*
    activate shop
     */
    @GetMapping("/shops/registration/verification")
    public ResponseEntity<?> activateShop(@RequestHeader("code") String code) {
        return userHelper.activeShop(code);
    }


    @GetMapping("/users/me/orders")
    public ResponseEntity<?> getCurrentUserOrders(@RequestParam Map<String, String> param) {
        return orderHelper.getCurrentUserOrders(param);

    }

    @GetMapping("/users/me/shop/orders")
    public ResponseEntity<?> getCurrentUserShopOrders(@RequestParam Map<String, String> param) {
        return orderHelper.getCurrentUserShopOrderItems(param);

    }

    @GetMapping("/users/me/shop/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderHelper.getOrder(id);
    }

    @PostMapping("/users/me/shop")
    public ResponseEntity<?> updateShop(@Valid @RequestBody UpdateShopRequest request) {
        return shopHelper.updateShop(request);
    }

    @GetMapping("/users/me/shop/orders/status")
    public ResponseEntity<?> getOrderItemStatusReport(@RequestParam Map<String, String> param) {
        return orderHelper.getOrderItemStatusTypes();
    }

    @PutMapping("/users/me/shop/orders/{orderId}/items/status")
    public ResponseEntity<?> updateItemsStatus(@PathVariable("orderId") Long orderId, @RequestBody ItemStatusRequest request) {
        return orderHelper.updateItemsStatus(orderId, request);
    }

    @GetMapping("/users/me/shop/orders/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable("orderId") Long orderId) {
        return orderHelper.getOrderItems(orderId);

    }

    @PostMapping("/files")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadHelper.uploadFile(file);
    }
}
