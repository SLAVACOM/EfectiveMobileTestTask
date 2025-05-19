package com.slavacom.efectivemobiletesttask.service;

import com.slavacom.efectivemobiletesttask.dto.response.CardResponse;
import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.CardStatus;
import com.slavacom.efectivemobiletesttask.model.User;
import com.slavacom.efectivemobiletesttask.repository.CardRepository;
import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepo;
    private final UserRepository userRepo;

    public CardResponse createCard(UserDetails user) {
        var owner = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + user.getUsername()));

        Card card = Card.builder()
                .number(generateUniqueCardNumber())
                .expiryDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .owner(owner)
                .build();

        card = cardRepo.save(card);

        return mapToResponse(card);
    }

    public List<CardResponse> getCardsForUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cardRepo.findAllByOwner(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<Card> getCardByNumberDecrypted(String rawNumber) {
        String encrypted = encryptNumber(rawNumber);
        return cardRepo.findActiveCardByNumber(encrypted);
    }

    private CardResponse mapToResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .maskedNumber(mask(card.getNumber()))
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus().name())
                .balance(card.getBalance())
                .build();
    }

    private String encryptNumber(String raw) {
        return raw;
    }

    private String mask(String rawOrEncrypted) {
        String last4 = rawOrEncrypted.substring(rawOrEncrypted.length() - 4);
        return "**** **** **** " + last4;
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = String.valueOf(System.currentTimeMillis()); // например, 13 цифр
            cardNumber += String.format("%03d", new Random().nextInt(1000)); // добавляем 3 случайных цифры
            cardNumber = cardNumber.substring(0, 16);
        } while (cardRepo.existsByNumber(cardNumber));
        return cardNumber;
    }

    public void transferBetweenOwnCards(String username, String fromCardNumber, String toCardNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Transfer amount must be positive");


        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Card fromCard = cardRepo.findActiveCardByNumber(fromCardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Source card not found"));

        Card toCard = cardRepo.findActiveCardByNumber(toCardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Destination card not found"));

        if (!fromCard.getOwner().getId().equals(user.getId()) ||
                !toCard.getOwner().getId().equals(user.getId()))
            throw new AccessDeniedException("Cards do not belong to the user");

        if (fromCard.getStatus() != CardStatus.ACTIVE || toCard.getStatus() != CardStatus.ACTIVE)
            throw new IllegalStateException("Both cards must be active");


        if (fromCard.getBalance().compareTo(amount) < 0)
            throw new IllegalStateException("Insufficient funds on source card");


        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepo.save(fromCard);
        cardRepo.save(toCard);
    }

    public void blockCard(String username, String cardNumber) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Card card = cardRepo.findByNumber(cardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (!card.getOwner().getId().equals(user.getId()))
            throw new AccessDeniedException("Card does not belong to the user");

        if (card.getStatus() == CardStatus.BLOCKED) throw new IllegalStateException("Card is already blocked");


        card.setStatus(CardStatus.BLOCKED);
        cardRepo.save(card);
    }

}
