package com.fresco.user.service.tracker.client;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import retrofit2.http.GET;
import retrofit2.http.Query;


@Component
public interface TrackerClient {

	@GET("/api/realtime/VehiclePositions.pb")
	Single<ResponseBody> getVehicleDetails(@Query(value = "key") String key);
}
