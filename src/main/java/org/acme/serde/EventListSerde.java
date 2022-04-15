package org.acme.serde;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.acme.model.EventList;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public class EventListSerde extends JsonSerde<EventList> {

	@Inject
	public EventListSerde(ObjectMapper objectMapper) {
		super(objectMapper, EventList.class);
	}
}
