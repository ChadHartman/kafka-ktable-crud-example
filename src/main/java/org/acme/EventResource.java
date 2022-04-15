package org.acme;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.config.EventListStore;
import org.acme.config.EventsProducer;
import org.acme.health.ReadinessCheck;
import org.acme.model.Event;
import org.acme.model.EventList;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/")
public class EventResource {

	@ConfigProperty(name = "org.acme.kafka.topic.events")
	String topicEvents;

	@Inject
	@EventsProducer
	Producer<String, Event> eventsProducer;

	@Inject
	@EventListStore
	ReadOnlyKeyValueStore<String, EventList> store;

	@Any
	@Inject
	ReadinessCheck readinessCheck;

	@POST
	@Path("event/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event createEvent(@PathParam("name") String name) {
		
		var event = new Event();
		event.setName(name);
		
		eventsProducer.send(new ProducerRecord<>(topicEvents, name, event));
		
		return event;
	}

	@DELETE
	@Path("event/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event deleteEvent(@PathParam("name") String name) {
		
		var event = new Event();
		event.setName(name);
		event.setDeleteEvent(true);
		
		eventsProducer.send(new ProducerRecord<>(topicEvents, name, event));
		
		return event;
	}

	@GET
	@Path("event/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItem(@PathParam("name") String name) {

		if (readinessCheck.isRunning()) {
			try {
				var list = store.get(name);
				return Objects.isNull(list) ?
						Response.status(Status.NOT_FOUND).entity(Map.of("error", "not found")).build() :
						Response.ok().entity(list).build();
			} catch (InvalidStateStoreException ignored) {
			}
		}

		return Response.status(Status.SERVICE_UNAVAILABLE).entity(Map.of("error", "not ready")).build();
	}

	@GET
	@Path("events")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItems() {
		if (readinessCheck.isRunning()) {
			try {
				var body = new TreeMap<String, Object>();
				store.all().forEachRemaining(kv -> body.put(kv.key, kv.value));
				return Response.ok().entity(body).build();
			} catch (InvalidStateStoreException ignored) {
			}
		}

		return Response.status(Status.SERVICE_UNAVAILABLE).entity(Map.of("error", "not ready")).build();
	}
}
