package com.example.samplecrud.service;

import com.example.samplecrud.data.entity.UserEntity;
import com.example.samplecrud.data.repository.UserRepository;
import com.example.samplecrud.model.dto.LoginRequest;
import com.example.samplecrud.model.dto.LoginResponse;
import com.example.samplecrud.model.dto.RegisterUserRequest;
import com.example.samplecrud.security.CustomUserDetails;
import com.example.samplecrud.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return LoginResponse.builder()
                .token(token)
                .build();
    }
    public void registerUser(RegisterUserRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRoles("ROLE_USER");
        userRepository.save(userEntity);
    }
}