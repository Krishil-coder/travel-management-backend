package com.example.Backend.Service;

import com.example.Backend.DTO.Request.PolicyRequest;
import com.example.Backend.DTO.Response.PolicyResponse;
import com.example.Backend.Domain.Policy;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Repository.PolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyService {

    private final PolicyRepository policyRepository;

    @Transactional
    public PolicyResponse createPolicy(PolicyRequest request) {
        Policy policy = Policy.builder()
                .name(request.getName())
                .appliesToRole(request.getAppliesToRole())
                .maxBudget(request.getMaxBudget())
                .maxTravelClass(request.getMaxTravelClass())
                .hardBlockOnViolation(request.getHardBlockOnViolation())
                .active(request.getActive())
                .build();

        Policy savedPolicy = policyRepository.save(policy);

        log.info("Created policy id={} name={}", savedPolicy.getId(), savedPolicy.getName());

        return PolicyResponse.from(savedPolicy);
    }

    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll()
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    public PolicyResponse getPolicyById(Long id) {
        Policy policy = findPolicy(id);
        return PolicyResponse.from(policy);
    }

    @Transactional
    public PolicyResponse updatePolicy(Long id, PolicyRequest request) {
        Policy policy = findPolicy(id);

        policy.setName(request.getName());
        policy.setAppliesToRole(request.getAppliesToRole());
        policy.setMaxBudget(request.getMaxBudget());
        policy.setMaxTravelClass(request.getMaxTravelClass());
        policy.setHardBlockOnViolation(request.getHardBlockOnViolation());
        policy.setActive(request.getActive());

        Policy savedPolicy = policyRepository.save(policy);

        log.info("Updated policy id={} name={}", savedPolicy.getId(), savedPolicy.getName());

        return PolicyResponse.from(savedPolicy);
    }

    @Transactional
    public PolicyResponse deletePolicy(Long id) {
        Policy policy = findPolicy(id);
        PolicyResponse response = PolicyResponse.from(policy);

        policyRepository.delete(policy);

        log.info("Deleted policy id={} name={}", policy.getId(), policy.getName());

        return response;
    }

    private Policy findPolicy(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Not Found " + id));
    }
}
