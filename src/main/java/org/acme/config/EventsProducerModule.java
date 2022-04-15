package org.acme.config;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.acme.model.Event;
import org.acme.serde.EventSerde;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class EventsProducerModule {

	@ConfigProperty(name = "org.acme.kafka.bootstrap.servers")
	String bootstrapServers;

	@Inject
	EventSerde eventSerde;

	@Produces
	@EventsProducer
	@ApplicationScoped
	public KafkaProducer<String, Event> createEventsProducer() {
		return new KafkaProducer<>(
				Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
				Serdes.String().serializer(),
				eventSerde.serializer()
		);
	}
}
