package com.example.Backend.Service;

import com.example.Backend.DTO.Request.ApprovalActionRequest;
import com.example.Backend.DTO.Response.ManagerRequestDetailsResponse;
import com.example.Backend.Domain.RequestStatus;
import com.example.Backend.Domain.Role;
import com.example.Backend.Domain.TravelRequest;
import com.example.Backend.Domain.User;
import com.example.Backend.Exception.ResourceConflictException;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Repository.TravelRequestRepository;
import com.example.Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerApprovalService {
    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<ManagerRequestDetailsResponse> getPendingRequests(Long managerId) {
        User manager = findManager(managerId);

        return travelRequestRepository
                .findByEmployeeManagerIdAndStatus(manager.getId(), RequestStatus.SUBMITTED)
                .stream()
                .map(ManagerRequestDetailsResponse::from)
                .toList();
    }

    @Transactional
    public ManagerRequestDetailsResponse getRequestDetails(Long requestId) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        return ManagerRequestDetailsResponse.from(travelRequest);
    }

    @Transactional
    public ManagerRequestDetailsResponse approveRequest(Long requestId, ApprovalActionRequest request) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        User manager = findManager(request.getManagerId());
        validateManagerCanAct(travelRequest, manager);
        validateManagerPending(travelRequest);

        travelRequest.setStatus(RequestStatus.MANAGER_APPROVED);
        travelRequest.setManagerApprover(manager);
        travelRequest.setManagerActionAt(LocalDateTime.now());
        travelRequest.setManagerComments(request.getComments());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Manager approved travel request id={} managerId={}",
                savedTravelRequest.getId(), manager.getId());

        return ManagerRequestDetailsResponse.from(savedTravelRequest);
    }

    @Transactional
    public ManagerRequestDetailsResponse rejectRequest(Long requestId, ApprovalActionRequest request) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        User manager = findManager(request.getManagerId());
        validateManagerCanAct(travelRequest, manager);
        validateManagerPending(travelRequest);

        travelRequest.setStatus(RequestStatus.REJECTED);
        travelRequest.setManagerApprover(manager);
        travelRequest.setManagerActionAt(LocalDateTime.now());
        travelRequest.setManagerComments(request.getComments());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Manager rejected travel request id={} managerId={}",
                savedTravelRequest.getId(), manager.getId());

        return ManagerRequestDetailsResponse.from(savedTravelRequest);
    }

    private TravelRequest findTravelRequest(Long requestId) {
        return travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + requestId));
    }

    private User findManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager Not Found " + managerId));

        if (manager.getRole() != Role.MANAGER) {
            throw new ResourceConflictException("User is not a manager");
        }

        return manager;
    }

    private void validateManagerCanAct(TravelRequest travelRequest, User manager) {
        User employeeManager = travelRequest.getEmployee().getManager();

        if (employeeManager == null || !employeeManager.getId().equals(manager.getId())) {
            throw new ResourceConflictException("Manager cannot act on this travel request");
        }
    }

    private void validateManagerPending(TravelRequest travelRequest) {
        if (travelRequest.getStatus() != RequestStatus.SUBMITTED) {
            throw new ResourceConflictException("Only manager pending travel requests can be approved or rejected");
        }
    }
}
