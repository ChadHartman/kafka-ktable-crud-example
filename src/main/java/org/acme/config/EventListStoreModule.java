package org.acme.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import org.acme.model.EventList;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class EventListStoreModule {

	@ConfigProperty(name = "org.acme.kafka.store.event-lists")
	String storeName;

	@Produces
	@EventListStore
	@ApplicationScoped
	public ReadOnlyKeyValueStore<String, EventList> createEventListStore(KafkaStreams streams) {
		return streams.store(StoreQueryParameters.fromNameAndType(
				storeName,
				QueryableStoreTypes.keyValueStore()
		));
	}
}
