package org.acme.serde;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.acme.model.Event;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public class EventSerde extends JsonSerde<Event> {

	@Inject
	public EventSerde(ObjectMapper objectMapper) {
		super(objectMapper, Event.class);
	}
}
