package com.slavacom.efectivemobiletesttask.controller;

import com.slavacom.efectivemobiletesttask.dto.response.CardResponse;
import com.slavacom.efectivemobiletesttask.dto.response.UserResponse;
import com.slavacom.efectivemobiletesttask.repository.CardRepository;
import com.slavacom.efectivemobiletesttask.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CardRepository cardRepository;
    private final AdminService adminService;


    @GetMapping("/cards")
    public ResponseEntity<?> getAllCards(
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CardResponse> users = adminService.getAllCards(cardNumber, status, page, size);
        return users.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(users);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponse> users = adminService.getAllUsers(username, role, page, size);
        return users.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(users);
    }

}
