package com.example.Backend.Repository;

import com.example.Backend.Domain.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByTravelRequestId(Long travelRequestId);

    Optional<Itinerary> findByIdAndTravelRequestId(Long id, Long travelRequestId);
}
