package com.slavacom.efectivemobiletesttask.controller;

import com.slavacom.efectivemobiletesttask.dto.request.TransferRequest;
import com.slavacom.efectivemobiletesttask.dto.response.CardResponse;
import com.slavacom.efectivemobiletesttask.service.CardService;
import com.slavacom.efectivemobiletesttask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CardService cardService;

    @GetMapping("/balance")
    public String getBalance(@AuthenticationPrincipal UserDetails user) {

        return userService.getUserBalance(user).toString();
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getMyCards(@AuthenticationPrincipal UserDetails user) {
        List<CardResponse> cards = cardService.getCardsForUser(user.getUsername());

        return cards.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(cards);
    }

    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@AuthenticationPrincipal UserDetails user) {
        CardResponse cardResponse = cardService.createCard(user);

        return ResponseEntity.ok(cardResponse);
    }

    @PostMapping("/cards/transfer")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal UserDetails user,
                                      @Valid @RequestBody TransferRequest request) {
        cardService.transferBetweenOwnCards(user.getUsername(), request.getFromCardNumber(),
                request.getToCardNumber(), request.getAmount());

        return ResponseEntity.ok("Transfer successful");
    }

    @PostMapping("/cards/{cardNumber}/block")
    public ResponseEntity<?> blockCard(@AuthenticationPrincipal UserDetails user,
                                       @PathVariable String cardNumber) {
        cardService.blockCard(user.getUsername(), cardNumber);

        return ResponseEntity.ok("Card " + cardNumber + " blocked successfully");
    }


}
