package com.fresco.user.service.tracker.gateway;


import com.fresco.user.service.exceptionshandler.BusinessException;
import com.fresco.user.service.exceptionshandler.enums.ErrorCodes;
import com.fresco.user.service.tracker.client.TrackerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class TrackerGateway {

	private final TrackerClient trackerClient;

	@Autowired
    public TrackerGateway(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
    }

	/**
	 * Fetches vehicle details and parses the protobuf data
	 *
	 * @param key API key
	 * @return List of VehicleData objects
	 */
	public byte[] getVehicleDetails(String key) {
		try {
			// Get the raw binary response
			return trackerClient.getVehicleDetails(key)
					.blockingGet()
					.bytes();

		}catch (IOException e) {
			log.error("***** TrackerGateway :: getVehicleDetails : Error reading response: {} ", e.getMessage());
			throw new BusinessException(ErrorCodes.CONNECTION_FAILED, new Object[]{e.getMessage()}, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("***** TrackerGateway :: getVehicleDetails : Error : {} ", e.getMessage());
			throw new BusinessException(ErrorCodes.CONNECTION_FAILED, new Object[]{e.getMessage()}, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
