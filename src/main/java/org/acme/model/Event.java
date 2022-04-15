package org.acme.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class Event {

	private static final AtomicInteger ID_GEN = new AtomicInteger();

	private int id;
	private boolean isDeleteEvent;
	private Instant timestamp;
	private String name;

	public Event() {
		this.id = ID_GEN.incrementAndGet();
		this.timestamp = Instant.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDeleteEvent() {
		return isDeleteEvent;
	}

	public void setDeleteEvent(boolean deleteEvent) {
		isDeleteEvent = deleteEvent;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
