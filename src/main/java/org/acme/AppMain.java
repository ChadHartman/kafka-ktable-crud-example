package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AppMain {

	@Inject
	KafkaStreams kafkaStreams;

	void onStart(@Observes StartupEvent ev) {
		kafkaStreams.cleanUp();
		kafkaStreams.start();
	}

	void onStop(@Observes ShutdownEvent ev) {
		kafkaStreams.close();
	}
}
