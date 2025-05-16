package com.fresco.user.service.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class VehicleData {
    // Entity ID
    private String id;

    // Trip information
    private String tripId;
    private String routeId;
    private LocalTime startTime;
    private LocalDate startDate;
    private String scheduleRelationship;

    // Position information
    private double latitude;
    private double longitude;
    private Float speed;
    private Float bearing;

    // Timestamp
    private LocalDateTime timestamp;
    private long rawTimestamp;

    // Vehicle identification
    private String vehicleId;
    private String vehicleLabel;
    private String licensePlate;

    // Additional fields that might be present
    private Integer currentStopSequence;
    private String currentStatus;
    private Float odometer;

    // Helper method to check if position data is available
    public boolean hasPositionData() {
        return latitude != 0 || longitude != 0;
    }
}