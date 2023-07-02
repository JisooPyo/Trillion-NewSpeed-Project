package com.sparta.trillionnewspeedproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDto {
    String username;
    boolean isAdmin;
}
