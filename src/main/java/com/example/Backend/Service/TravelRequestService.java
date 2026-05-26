package com.example.Backend.Service;

import com.example.Backend.DTO.Request.CreateTravelRequestDto;
import com.example.Backend.DTO.Request.UpdateTravelRequestDto;
import com.example.Backend.DTO.Response.TravelRequestResponse;
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
public class TravelRequestService {
    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public TravelRequestResponse createTravelRequest(CreateTravelRequestDto dto) {
        validateDateRange(dto.getStartDate(), dto.getEndDate());

        User employee = userRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found " + dto.getEmployeeId()));
        validateEmployeeOrManager(employee);

        TravelRequest travelRequest = TravelRequest.builder()
                .employee(employee)
                .destination(dto.getDestination())
                .travelClass(dto.getTravelClass())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .estimatedCost(dto.getEstimatedCost())
                .purpose(dto.getPurpose())
                .status(RequestStatus.DRAFT)
                .build();

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Created travel request id={} employeeId={}",
                savedTravelRequest.getId(), employee.getId());

        return TravelRequestResponse.from(savedTravelRequest);
    }
    @Transactional
    public TravelRequestResponse getTravelRequestById(Long id) {

        TravelRequest request = travelRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + id));
        return TravelRequestResponse.from(request);

    }


    @Transactional
    public TravelRequestResponse updateDraftTravelRequest(Long id, UpdateTravelRequestDto dto) {
        validateDateRange(dto.getStartDate(), dto.getEndDate());

        TravelRequest travelRequest = findTravelRequest(id);
        validateEmployeeOrManager(travelRequest.getEmployee());
        validateDraftStatus(travelRequest);

        travelRequest.setDestination(dto.getDestination());
        travelRequest.setTravelClass(dto.getTravelClass());
        travelRequest.setStartDate(dto.getStartDate());
        travelRequest.setEndDate(dto.getEndDate());
        travelRequest.setEstimatedCost(dto.getEstimatedCost());
        travelRequest.setPurpose(dto.getPurpose());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Updated draft travel request id={} employeeId={}",
                savedTravelRequest.getId(), savedTravelRequest.getEmployee().getId());

        return TravelRequestResponse.from(savedTravelRequest);
    }

    @Transactional
    public TravelRequestResponse deleteDraftTravelRequest(Long id) {

        TravelRequest travelRequest = findTravelRequest(id);
        if(travelRequest.getStatus() != RequestStatus.DRAFT) {
            throw new ResourceConflictException("Only draft travel requests can be deleted");
        }
        TravelRequestResponse response = TravelRequestResponse.from(travelRequest);
        travelRequestRepository.delete(travelRequest);
        return response;
    }

    @Transactional
    public TravelRequestResponse submitTravelRequest(Long id) {
        TravelRequest travelRequest = findTravelRequest(id);
        validateEmployeeOrManager(travelRequest.getEmployee());
        validateDraftStatus(travelRequest);

        travelRequest.setStatus(RequestStatus.SUBMITTED);
        travelRequest.setSubmittedAt(LocalDateTime.now());

        TravelRequest savedTravelRequest = travelRequestRepository.save(travelRequest);

        log.info("Submitted travel request id={} employeeId={}",
                savedTravelRequest.getId(), savedTravelRequest.getEmployee().getId());

        return TravelRequestResponse.from(savedTravelRequest);
    }
    @Transactional
    public List<TravelRequestResponse> getAllTravelRequests() {

        return travelRequestRepository.findAll()
                .stream()
                .map(TravelRequestResponse::from)
                .toList();
    }
        @Transactional
    public List<TravelRequestResponse> getEmployeeRequests(Long employeeId){

        return travelRequestRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(TravelRequestResponse::from)
                .toList();
    }

    private TravelRequest findTravelRequest(Long id) {
        return travelRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + id));
    }

    private void validateDraftStatus(TravelRequest travelRequest) {
        if (travelRequest.getStatus() != RequestStatus.DRAFT) {
            throw new ResourceConflictException("Only draft travel requests can be changed or submitted");
        }
    }

    private void validateEmployeeOrManager(User user) {
        if (user.getRole() != Role.EMPLOYEE && user.getRole() != Role.MANAGER) {
            throw new ResourceConflictException("Travel requests can be created only for Employee or Manager users");
        }
    }

    private void validateDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new ResourceConflictException("End date cannot be before start date");
        }
    }

}
