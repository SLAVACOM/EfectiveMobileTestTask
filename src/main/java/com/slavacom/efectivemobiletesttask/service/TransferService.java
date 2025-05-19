package com.slavacom.efectivemobiletesttask.service;


import com.slavacom.efectivemobiletesttask.dto.request.TransferRequest;
import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.CardStatus;
import com.slavacom.efectivemobiletesttask.model.User;
import com.slavacom.efectivemobiletesttask.repository.CardRepository;
import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final CardRepository cardRepo;
    private final UserRepository userRepo;
    private final CardService cardService;


    public void transferBetweenCards(String username, TransferRequest request) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card from = cardService.getCardByNumberDecrypted(request.getFromCardNumber())
                .orElseThrow(() -> new RuntimeException("From card not found"));

        Card to = cardService.getCardByNumberDecrypted(request.getToCardNumber())
                .orElseThrow(() -> new RuntimeException("To card not found"));

        if (!from.getOwner().getId().equals(user.getId()) ||
                !to.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("You can only transfer between your own cards");
        }

        if (from.getStatus() != CardStatus.ACTIVE || to.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException("Both cards must be active");
        }

        BigDecimal amount = request.getAmount();

        if (from.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepo.save(from);
        cardRepo.save(to);
    }
}
