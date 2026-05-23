package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.Role;
import com.example.Backend.Domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long  id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String department;
    private Long managerId;
    private String managerName;
    private boolean enabled;
    private LocalDateTime createdAt;

    public static UserResponse from (User user) {
        // it maps the data with the entity;
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .department(user.getDepartment())
                .managerId(user.getManager() !=null ? user.getManager().getId():null)
                .managerName(user.getManager() != null ? user.getManager().getFirstName()+" "+user.getManager().getLastName():null)
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}