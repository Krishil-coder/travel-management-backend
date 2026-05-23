package com.example.Backend.DTO.Request;

import com.example.Backend.Domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank(message = "First name is required!")
    @Size(max=100, message="First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    @Size(max=100, message = "First name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format")
    @Size(max=150)
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max=100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotNull(message = "Role is required!")
    private Role role;

    // Here, we are not using "@NotNull" because we need to admin that not have department;
    @Size(max=100)
    private String department;

    private Long managerId;
}

   