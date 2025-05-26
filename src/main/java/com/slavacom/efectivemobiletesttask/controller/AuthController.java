package com.slavacom.efectivemobiletesttask.controller;

import com.slavacom.efectivemobiletesttask.dto.request.AuthRequest;
import com.slavacom.efectivemobiletesttask.dto.response.AuthResponse;
import com.slavacom.efectivemobiletesttask.model.Role;
import com.slavacom.efectivemobiletesttask.model.User;
import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import com.slavacom.efectivemobiletesttask.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest body) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest body) {
        if (userRepository.existsByUsername(body.getUsername()))
            return ResponseEntity.badRequest().body("User already exists");

        var user = User.builder()
                .username(body.getUsername())
                .password(passwordEncoder.encode(body.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
