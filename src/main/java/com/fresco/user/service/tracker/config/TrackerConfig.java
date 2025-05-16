package com.fresco.user.service.tracker.config;

import com.fresco.user.service.tracker.client.TrackerClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;


@Configuration
public class TrackerConfig {

	@Bean
	public TrackerClient trackerClient(
			@Value("${connection.read.timeout.second:60}") final Long timeoutSeconds,
			@Value("${dtc.position.base.url}") final String baseUrl) {

		return new Retrofit.Builder()
				.client(new OkHttpClient.Builder()
						.readTimeout(timeoutSeconds, TimeUnit.SECONDS)
						.connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
						.build())
				.baseUrl(baseUrl)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build()
				.create(TrackerClient.class);
	}
}