package com.fresco.user.service.service;


import com.fresco.user.service.exceptionshandler.BusinessException;
import com.fresco.user.service.exceptionshandler.enums.ErrorCodes;
import com.fresco.user.service.model.VehicleData;
import com.fresco.user.service.tracker.service.TrackerService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.transit.realtime.GtfsRealtime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VehicleTrackerService {

    private final TrackerService trackerService;

    private final AtomicReference<List<VehicleData>> latestVehicleData = new AtomicReference<>(new ArrayList<>());

    private final Map<String, List<VehicleData>> vehicleHistory = new ConcurrentHashMap<>();

    // Maximum history items to keep per vehicle
    private static final int MAX_HISTORY_ITEMS = 10;

    @Autowired
    public VehicleTrackerService(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    /**
     * Get the latest vehicle data
     * @return List of vehicle data objects
     */
    public List<VehicleData> getLatestVehicleData() {
        return latestVehicleData.get();
    }

    @Scheduled(fixedRateString = "${dtc.position.api.refresh-interval:3000}")
    public void fetchVehiclePositions() {
        try {
            byte[] responseBytes = trackerService.fetchVehiclePositions();

            GtfsRealtime.FeedMessage feedMessage = GtfsRealtime.FeedMessage.parseFrom(responseBytes);

            // Log the feed header information
            if (feedMessage.hasHeader()) {
                log.info("Feed timestamp: {}",
                        feedMessage.getHeader().hasTimestamp() ?
                                feedMessage.getHeader().getTimestamp() : "not available");
            }

            List<VehicleData> data = parseVehicleData(feedMessage);
            latestVehicleData.set(data);
            log.info("Successfully updated vehicle positions. Count: {}", data.size());
        } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("***** TrackerGateway :: getVehicleDetails : Error : {} ", e.getMessage());
            throw new BusinessException(ErrorCodes.CONNECTION_FAILED, new Object[]{e.getMessage()}, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Parse the GTFS-RT feed message into our simplified VehicleData objects
     */
//    private List<VehicleData> parseVehicleData(GtfsRealtime.FeedMessage feedMessage) {
//        List<VehicleData> vehicles = new ArrayList<>();
//
//        for (GtfsRealtime.FeedEntity entity : feedMessage.getEntityList()) {
//            if (entity.hasVehicle()) {
//                GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();
//
//                // Build our simplified vehicle data object
//                VehicleData vehicleData = VehicleData.builder()
//                        .vehicleId(entity.getId())
//                        .licensePlate(
//                                vehiclePosition.hasVehicle() ?
//                                        vehiclePosition.getVehicle().getLicensePlate() : null
//                        )
//                        .tripId(
//                                vehiclePosition.hasTrip() ?
//                                        vehiclePosition.getTrip().getTripId() : null
//                        )
//                        .routeId(
//                                vehiclePosition.hasTrip() ?
//                                        vehiclePosition.getTrip().getRouteId() : null
//                        )
//                        .timestamp(
//                                vehiclePosition.hasTimestamp() ?
//                                        LocalDateTime.ofInstant(
//                                                Instant.ofEpochSecond(vehiclePosition.getTimestamp()),
//                                                ZoneId.systemDefault()
//                                        ) : null
//                        )
//                        .latitude(
//                                vehiclePosition.hasPosition() ?
//                                        vehiclePosition.getPosition().getLatitude() : 0
//                        )
//                        .longitude(
//                                vehiclePosition.hasPosition() ?
//                                        vehiclePosition.getPosition().getLongitude() : 0
//                        )
//                        .bearing(
//                                vehiclePosition.hasPosition() && vehiclePosition.getPosition().hasBearing() ?
//                                        vehiclePosition.getPosition().getBearing() : null
//                        )
//                        .speed(
//                                vehiclePosition.hasPosition() && vehiclePosition.getPosition().hasSpeed() ?
//                                        vehiclePosition.getPosition().getSpeed() : null
//                        )
//                        .build();
//
//                vehicles.add(vehicleData);
//            }
//        }
//
//        return vehicles;
//    }

    private List<VehicleData> parseVehicleData(GtfsRealtime.FeedMessage feedMessage) {
        List<VehicleData> vehicles = new ArrayList<>();

        for (GtfsRealtime.FeedEntity entity : feedMessage.getEntityList()) {
            if (entity.hasVehicle()) {
                GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();

                // Parse start time if available
                LocalTime startTime = null;
                if (vehiclePosition.hasTrip() && vehiclePosition.getTrip().hasStartTime()) {
                    try {
                        String timeStr = vehiclePosition.getTrip().getStartTime();
                        startTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    } catch (Exception e) {
                        log.warn("Could not parse start time: {}", vehiclePosition.getTrip().getStartTime());
                    }
                }

                // Parse start date if available
                LocalDate startDate = null;
                if (vehiclePosition.hasTrip() && vehiclePosition.getTrip().hasStartDate()) {
                    try {
                        String dateStr = vehiclePosition.getTrip().getStartDate();
                        startDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    } catch (Exception e) {
                        log.warn("Could not parse start date: {}", vehiclePosition.getTrip().getStartDate());
                    }
                }

                // Parse position timestamp
                LocalDateTime timestamp = null;
                long rawTimestamp = 0;
                if (vehiclePosition.hasTimestamp()) {
                    rawTimestamp = vehiclePosition.getTimestamp();
                    timestamp = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(rawTimestamp),
                            ZoneId.systemDefault()
                    );
                }

                // Build our complete vehicle data object
                VehicleData vehicleData = VehicleData.builder()
                        // Entity ID
                        .id(entity.getId())

                        // Trip information
                        .tripId(
                                vehiclePosition.hasTrip() && vehiclePosition.getTrip().hasTripId() ?
                                        vehiclePosition.getTrip().getTripId() : null
                        )
                        .routeId(
                                vehiclePosition.hasTrip() && vehiclePosition.getTrip().hasRouteId() ?
                                        vehiclePosition.getTrip().getRouteId() : null
                        )
                        .startTime(startTime)
                        .startDate(startDate)
                        .scheduleRelationship(
                                vehiclePosition.hasTrip() && vehiclePosition.getTrip().hasScheduleRelationship() ?
                                        vehiclePosition.getTrip().getScheduleRelationship().name() : null
                        )

                        // Position information
                        .latitude(
                                vehiclePosition.hasPosition() ?
                                        vehiclePosition.getPosition().getLatitude() : 0
                        )
                        .longitude(
                                vehiclePosition.hasPosition() ?
                                        vehiclePosition.getPosition().getLongitude() : 0
                        )
                        .speed(
                                vehiclePosition.hasPosition() && vehiclePosition.getPosition().hasSpeed() ?
                                        vehiclePosition.getPosition().getSpeed() : null
                        )
                        .bearing(
                                vehiclePosition.hasPosition() && vehiclePosition.getPosition().hasBearing() ?
                                        vehiclePosition.getPosition().getBearing() : null
                        )

                        // Timestamp
                        .timestamp(timestamp)
                        .rawTimestamp(rawTimestamp)

                        // Vehicle identification
                        .vehicleId(
                                vehiclePosition.hasVehicle() && vehiclePosition.getVehicle().hasId() ?
                                        vehiclePosition.getVehicle().getId() : null
                        )
                        .vehicleLabel(
                                vehiclePosition.hasVehicle() && vehiclePosition.getVehicle().hasLabel() ?
                                        vehiclePosition.getVehicle().getLabel() : null
                        )
                        .licensePlate(
                                vehiclePosition.hasVehicle() && vehiclePosition.getVehicle().hasLicensePlate() ?
                                        vehiclePosition.getVehicle().getLicensePlate() : null
                        )

                        // Additional fields
                        .currentStopSequence(
                                vehiclePosition.hasCurrentStopSequence() ?
                                        vehiclePosition.getCurrentStopSequence() : null
                        )
                        .currentStatus(
                                vehiclePosition.hasCurrentStatus() ?
                                        vehiclePosition.getCurrentStatus().name() : null
                        )
                        .odometer(
                                vehiclePosition.hasPosition() && vehiclePosition.getPosition().hasOdometer() ?
                                        (float) vehiclePosition.getPosition().getOdometer() : null
                        )
                        .build();

                vehicles.add(vehicleData);
            }
        }

        return vehicles;
    }

    /**
     * Update the historical record of vehicle positions
     */
    @Scheduled(fixedRateString = "${dtc.position.api.refresh-interval:3000}")
    private void updateVehicleHistory() {
        for (VehicleData vehicle : latestVehicleData.get()) {
            String vehicleId = vehicle.getId();
            if (vehicleId != null) {
                // Get or create the history list for this vehicle
                List<VehicleData> history = vehicleHistory.computeIfAbsent(vehicleId, k -> new ArrayList<>());

                // Add the new data point
                history.add(0, vehicle);

                // Trim if needed
                if (history.size() > MAX_HISTORY_ITEMS) {
                    history.subList(MAX_HISTORY_ITEMS, history.size()).clear();
                }
            }
        }
    }

    /**
     * Get the latest vehicle data for a specific vehicle
     * @param vehicleId The vehicle ID to look up
     * @return The vehicle data if found, null otherwise
     */
    public VehicleData getVehicleDetails(String vehicleId) {
        return latestVehicleData.get().stream()
                .filter(v -> vehicleId.equals(v.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all vehicles on a specific route
     * @param routeId The route ID to filter by
     * @return List of vehicles on the specified route
     */
    public List<VehicleData> getVehiclesByRoute(String routeId) {
        return latestVehicleData.get().stream()
                .filter(v -> routeId.equals(v.getRouteId()))
                .collect(Collectors.toList());
    }

    /**
     * Get historical data for a specific vehicle
     * @param vehicleId The vehicle ID to get history for
     * @return List of historical vehicle positions
     */
    public List<VehicleData> getVehicleHistory(String vehicleId) {
        return vehicleHistory.getOrDefault(vehicleId, new ArrayList<>());
    }

    /**
     * Get a summary of all active routes and the number of vehicles on each
     * @return Map of route IDs to vehicle counts
     */
    public Map<String, Long> getRoutesSummary() {
        return latestVehicleData.get().stream()
                .filter(v -> v.getRouteId() != null)
                .collect(Collectors.groupingBy(
                        VehicleData::getRouteId,
                        Collectors.counting()
                ));
    }
}
