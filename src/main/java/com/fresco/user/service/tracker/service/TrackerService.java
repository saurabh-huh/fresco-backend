package com.fresco.user.service.tracker.service;

import com.fresco.user.service.model.VehicleData;
import com.fresco.user.service.tracker.gateway.TrackerGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class TrackerService {

    private final TrackerGateway trackerGateway;
    private final String key;

    // Store the latest vehicle data
    private final AtomicReference<List<VehicleData>> latestVehicleData = new AtomicReference<>(new ArrayList<>());

    @Autowired
    public TrackerService(TrackerGateway trackerGateway, @Value("${dtc.position.api.key}") String key) {
        this.trackerGateway = trackerGateway;
        this.key = key;
    }

    /**
     * Scheduled task to fetch vehicle positions from the API
     */
    //@Scheduled(fixedRateString = "${dtc.position.api.refresh-interval:30000}")
    public byte[] fetchVehiclePositions() {
        try {
            return trackerGateway.getVehicleDetails(key);
        } catch (Exception error) {
            log.error("Error fetching vehicle positions", error);
        }
        return new byte[0];
    }

}
