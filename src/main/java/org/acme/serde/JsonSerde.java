package org.acme.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract class JsonSerde<T> implements Serde<T>, Serializer<T>, Deserializer<T> {

	private final ObjectMapper objectMapper;
	private final Class<T> itemClass;

	public JsonSerde(ObjectMapper objectMapper, Class<T> itemClass) {
		this.objectMapper = objectMapper;
		this.itemClass = itemClass;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Serializer<T> serializer() {
		return this;
	}

	@Override
	public Deserializer<T> deserializer() {
		return this;
	}

	@Override
	public T deserialize(String s, byte[] bytes) {
		try {
			return objectMapper.readValue(bytes, itemClass);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public byte[] serialize(String s, T item) {
		try {
			return objectMapper.writeValueAsBytes(item);
		} catch (JsonProcessingException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public void close() {
	}
}
