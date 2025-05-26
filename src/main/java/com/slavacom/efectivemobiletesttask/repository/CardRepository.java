package com.slavacom.efectivemobiletesttask.repository;

import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.CardStatus;
import com.slavacom.efectivemobiletesttask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    Optional<Card> findByNumberAndStatus(String number, CardStatus status);

    Optional<Card> findByNumber(String number) ;

    default Optional<Card> findActiveCardByNumber(String number) {
        return findByNumberAndStatus(number, CardStatus.ACTIVE);
    }

    

    List<Card> findAllByOwner(User user);

    void deleteByNumber(String number);

    boolean existsByNumber(String number);
}
