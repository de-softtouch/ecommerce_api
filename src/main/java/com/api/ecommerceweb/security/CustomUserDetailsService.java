package com.api.ecommerceweb.security;

import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(s);
        if (optionalUser.isEmpty()) {
            log.error(String.format("User: %s not found", s));
            throw new UsernameNotFoundException(String.format("User: %s not found", s));
        }
        return new CustomUserDetails(optionalUser.get());
    }
}
