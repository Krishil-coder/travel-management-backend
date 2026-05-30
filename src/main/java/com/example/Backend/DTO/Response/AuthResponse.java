package com.example.Backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String tokenType;

    private Long userId;

    private String email;

    private String role;

    private String firstName;

    private String lastName;
}
