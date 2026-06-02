package com.example.Backend.Service;


import com.example.Backend.DTO.Request.CreateUserDto;
import com.example.Backend.DTO.Request.UpdateUserDto;
import com.example.Backend.Domain.Role;
import com.example.Backend.DTO.Response.UserResponse;
import com.example.Backend.Domain.User;
import com.example.Backend.Exception.ResourceConflictException;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceConflictException("Email Already Exists" + dto.getEmail());
        }

        User manager = null;
        if (dto.getManagerId() != null) {
            manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager Not Found" + dto.getManagerId()
                    ));
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .passwordHash(hashPassword(dto.getPassword()))
                .role(dto.getRole())
                .department(dto.getDepartment())
                .manager(manager)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        log.info("Created user id={} email={} role={}",
                savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        return UserResponse.from(savedUser);
    }

    private String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponse::from)
                .toList();
    }
    public List<UserResponse> getUsersById(Long id) {
        return userRepository.findById(id).stream()
                .map(UserResponse::from)
                .toList();
    }

    public List<String> getDepartments() {
        return userRepository.findDistinctDepartments();
    }

    public List<UserResponse> getManagersByDepartment(String department) {
        return userRepository.findByRoleAndDepartment(Role.MANAGER, department)
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found" + id));

        userRepository.findByEmail(dto.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new ResourceConflictException("Email Already Exists" + dto.getEmail());
                });

        User manager = null;
        if (dto.getManagerId() != null) {
            if (dto.getManagerId().equals(id)) {
                throw new ResourceConflictException("User cannot be their own manager");
            }

            manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager Not Found" + dto.getManagerId()
                    ));
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setDepartment(dto.getDepartment());
        user.setManager(manager);
        user.setEnabled(dto.getEnabled());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPasswordHash(hashPassword(dto.getPassword()));
        }

        User savedUser = userRepository.save(user);

        log.info("Updated user id={} email={} role={}",
                savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        return UserResponse.from(savedUser);
    }

    public void Delete(Long id) {
        userRepository.deleteById(id);
    }


}
