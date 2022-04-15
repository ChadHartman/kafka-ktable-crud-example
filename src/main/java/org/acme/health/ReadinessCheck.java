package org.acme.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class ReadinessCheck implements HealthCheck {

	@Inject
	KafkaStreams kafkaStreams;

	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.builder()
				.name("app")
				.status(isRunning())
				.build();
	}

	public boolean isRunning() {
		return kafkaStreams.state().isRunningOrRebalancing();
	}
}
