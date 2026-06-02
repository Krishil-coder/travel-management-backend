package com.example.Backend.Domain;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name="itineraries",indexes = {
        @Index(name="idx_itin_request",columnList = "travel_request_id")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY,optional = false)
    @JoinColumn(name="travel_request_id",nullable = false)
    private TravelRequest travelRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private SegmentType segmentType;

    @Column(nullable = false,length = 200)
    private String fromLocation;

    @Column(nullable = false,length = 200)
    private String toLocation;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(length = 500)
    private String details;



    public enum SegmentType{
      FLIGHT,TRAIN,BUS,HOTEL,CAR_RENTAL,OTHER
    }
}
