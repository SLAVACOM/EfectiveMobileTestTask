package com.slavacom.efectivemobiletesttask.service;

import com.slavacom.efectivemobiletesttask.dto.response.CardResponse;
import com.slavacom.efectivemobiletesttask.dto.response.UserResponse;
import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.CardStatus;
import com.slavacom.efectivemobiletesttask.model.Role;
import com.slavacom.efectivemobiletesttask.model.User;
import com.slavacom.efectivemobiletesttask.repository.CardRepository;
import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final CardRepository cardRepo;
    private final UserRepository userRepo;

    public Page<UserResponse> getAllUsers(String usernameFilter, String roleFilter, int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("id").descending());

        Specification<User> spec = Specification.where(null);

        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("username"), "%" + usernameFilter + "%"));
        }

        if (roleFilter != null && !roleFilter.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("role"), Role.valueOf(roleFilter.toUpperCase())));
        }

        return userRepo.findAll(spec, pageable).map(this::toDto);
    }

    public Page<CardResponse> getAllCards(String numberFilter, String statusFilter, int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("id").descending());

        Specification<Card> spec = Specification.where(null);

        if (numberFilter != null && !numberFilter.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("number"), "%" + numberFilter + "%"));
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), CardStatus.valueOf(statusFilter.toUpperCase())));
        }

        return cardRepo.findAll(spec, pageable)
                .map(this::toDto);
    }

    private UserResponse toDto(@NotNull User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .cards(user.getCards())
                .build();
    }

    private CardResponse toDto(@NotNull Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .expiryDate(card.getExpiryDate())
                .maskedNumber(card.getMaskedNumber())
                .status(card.getStatus().name())
                .balance(card.getBalance())
                .build();
    }
}
