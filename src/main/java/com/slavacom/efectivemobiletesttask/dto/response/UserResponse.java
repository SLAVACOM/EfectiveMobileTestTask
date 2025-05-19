package com.slavacom.efectivemobiletesttask.dto.response;

import com.slavacom.efectivemobiletesttask.model.Card;
import com.slavacom.efectivemobiletesttask.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String password;

    private Role role;
    private List<Card> cards = new ArrayList<>();
}
