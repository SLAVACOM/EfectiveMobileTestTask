package com.slavacom.efectivemobiletesttask.service;

import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.User;
import com.slavacom.efectivemobiletesttask.repository.CardRepository;
import com.slavacom.efectivemobiletesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    private final UserService userService;

    public List<Card> getUserBalance(UserDetails user) {

        var owner = getUser(user);

        return cardRepository.findAllByOwner(owner);

    }

    public User getUser(UserDetails user) {
        return userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException(user.getUsername())
        );
    }


}
