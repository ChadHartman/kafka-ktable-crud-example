package org.acme.topology;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.model.Event;
import org.acme.model.EventList;
import org.acme.serde.EventListSerde;
import org.acme.serde.EventSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventProcessorNode {

	@ConfigProperty(name = "org.acme.kafka.topic.events")
	String topicEvents;
	
	@ConfigProperty(name = "org.acme.kafka.store.event-lists")
	String storeEventLists;

	@Inject
	EventSerde eventSerde;

	@Inject
	EventListSerde eventListSerde;

	public void init(StreamsBuilder builder) {

		var mat = Materialized.<String, EventList, KeyValueStore<Bytes, byte[]>>as(storeEventLists)
				.withKeySerde(Serdes.String())
				.withValueSerde(eventListSerde);

		builder.stream(topicEvents, Consumed.with(Serdes.String(), eventSerde))
				.groupByKey()
				.aggregate(this::create, this::aggregate, mat);
	}

	private EventList create() {
		return null;
	}

	private EventList aggregate(String key, Event ev, EventList list) {

		if (ev.isDeleteEvent()) {
			// Delete KTable entry
			return null;
		}

		if (Objects.isNull(list)) {
			// Create KTable Entry this could also be done in the initializer.
			//  We may want to wait until here to perform this operation if we
			//  need metadata from an event
			list = new EventList();
		}

		list.add(ev);
		return list;
	}
}
