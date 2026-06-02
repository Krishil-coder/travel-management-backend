package com.example.Backend.Repository;

import com.example.Backend.Domain.Policy;
import com.example.Backend.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findFirstByAppliesToRoleAndActiveTrue(Role role);
}
