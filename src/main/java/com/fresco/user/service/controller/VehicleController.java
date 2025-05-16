package com.fresco.user.service.controller;

import com.fresco.user.service.model.VehicleData;
import com.fresco.user.service.service.VehicleTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleTrackerService vehicleTrackerService;

    /**
     * Get all current vehicle positions
     */
    @GetMapping
    public ResponseEntity<List<VehicleData>> getAllVehicles() {
        return ResponseEntity.ok(vehicleTrackerService.getLatestVehicleData());
    }

    /**
     * Get a specific vehicle by ID
     */
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleData> getVehicleById(@PathVariable String vehicleId) {
        VehicleData vehicle = vehicleTrackerService.getVehicleDetails(vehicleId);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Get all vehicles on a specific route
     */
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<VehicleData>> getVehiclesByRoute(@PathVariable String routeId) {
        return ResponseEntity.ok(vehicleTrackerService.getVehiclesByRoute(routeId));
    }

    /**
     * Get historical positions for a specific vehicle
     */
    @GetMapping("/{vehicleId}/history")
    public ResponseEntity<List<VehicleData>> getVehicleHistory(@PathVariable String vehicleId) {
        return ResponseEntity.ok(vehicleTrackerService.getVehicleHistory(vehicleId));
    }

    /**
     * Get a summary of routes and vehicle counts
     */
    @GetMapping("/routes/summary")
    public ResponseEntity<Map<String, Long>> getRoutesSummary() {
        return ResponseEntity.ok(vehicleTrackerService.getRoutesSummary());
    }

    /**
     * Force a refresh of the vehicle data
     */
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshVehicleData() {
        vehicleTrackerService.fetchVehiclePositions();
        return ResponseEntity.ok().build();
    }
}
