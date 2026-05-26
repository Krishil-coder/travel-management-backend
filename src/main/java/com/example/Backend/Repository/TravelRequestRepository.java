package com.example.Backend.Repository;

import com.example.Backend.Domain.RequestStatus;
import com.example.Backend.Domain.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRequestRepository
        extends JpaRepository<TravelRequest,Long> {

    List<TravelRequest> findByEmployeeId(Long employeeId);

    List<TravelRequest> findByEmployeeManagerIdAndStatus(Long managerId, RequestStatus status);

    List<TravelRequest> findByStatus(RequestStatus status);
}
