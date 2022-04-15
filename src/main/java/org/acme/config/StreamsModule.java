package org.acme.config;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.acme.topology.EventProcessorNode;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class StreamsModule {

	@ConfigProperty(name = "org.acme.kafka.app.id")
	String appId;

	@ConfigProperty(name = "org.acme.kafka.bootstrap.servers")
	String bootstrapServers;

	@ConfigProperty(name = "org.acme.kafka.streams.threads.count")
	int threadsCount;

	@Inject
	EventProcessorNode eventProcessorNode;

	@Produces
	@ApplicationScoped
	public KafkaStreams createStreams() {

		var props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threadsCount);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		var builder = new StreamsBuilder();
		eventProcessorNode.init(builder);

		return new KafkaStreams(builder.build(), props);
	}
}
