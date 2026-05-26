package com.example.Backend.Service;

import com.example.Backend.DTO.Request.FinanceApprovalRequestDto;
import com.example.Backend.DTO.Response.FinanceRequestResponse;
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
public class FinanceApprovalService {

    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<FinanceRequestResponse> getPendingRequests(Long financeId) {
        findFinancer(financeId);

        return travelRequestRepository
                .findByStatus(RequestStatus.MANAGER_APPROVED)
                .stream()
                .map(FinanceRequestResponse::from)
                .toList();
    }

    @Transactional
    public FinanceRequestResponse getRequestDetails(Long requestId) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        return FinanceRequestResponse.from(travelRequest);
    }

    @Transactional
    public FinanceRequestResponse approveRequest(Long requestId, FinanceApprovalRequestDto request) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        User financer = findFinancer(request.getFinanceId());
        validateFinancePending(travelRequest);

        travelRequest.setStatus(RequestStatus.FINANCE_APPROVED);
        travelRequest.setFinanceApprover(financer);
        travelRequest.setFinanceActionAt(LocalDateTime.now());
        travelRequest.setFinanceComments(request.getComments());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Finance approved travel request id={} financeId={}",
                savedTravelRequest.getId(), financer.getId());

        return FinanceRequestResponse.from(savedTravelRequest);
    }

    @Transactional
    public FinanceRequestResponse rejectRequest(Long requestId, FinanceApprovalRequestDto request) {
        TravelRequest travelRequest = findTravelRequest(requestId);
        User financer = findFinancer(request.getFinanceId());
        validateFinancePending(travelRequest);

        travelRequest.setStatus(RequestStatus.REJECTED);
        travelRequest.setFinanceApprover(financer);
        travelRequest.setFinanceActionAt(LocalDateTime.now());
        travelRequest.setFinanceComments(request.getComments());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Finance rejected travel request id={} financeId={}",
                savedTravelRequest.getId(), financer.getId());

        return FinanceRequestResponse.from(savedTravelRequest);
    }

    private TravelRequest findTravelRequest(Long requestId) {
        return travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + requestId));
    }

    private User findFinancer(Long financeId) {
        User financer = userRepository.findById(financeId)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Approver Not Found " + financeId));

        if (financer.getRole() != Role.FINANCER) {
            throw new ResourceConflictException("User is not a financer");
        }

        return financer;
    }

    private void validateFinancePending(TravelRequest travelRequest) {
        if (travelRequest.getStatus() != RequestStatus.MANAGER_APPROVED) {
            throw new ResourceConflictException("Only finance pending travel requests can be approved or rejected");
        }
    }
}
