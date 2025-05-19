package com.slavacom.efectivemobiletesttask.service;

import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    public BigDecimal getUserBalance(Authentication authentication) {
        return BigDecimal.ONE;

    }
}
