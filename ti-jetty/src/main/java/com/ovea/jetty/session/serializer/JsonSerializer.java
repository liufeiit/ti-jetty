package com.ovea.jetty.session.serializer;

import static org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY;

import java.io.IOException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.VisibilityChecker;

import com.ovea.jetty.session.SerializerException;

public final class JsonSerializer extends SerializerSkeleton {

	private ObjectMapper mapper;

	@Override
	public void start() {
		mapper = new ObjectMapper();

		mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		mapper.configure(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
		mapper.configure(SerializationConfig.Feature.USE_STATIC_TYPING, false);
		mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, false);
		mapper.configure(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY, true);
		mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

		mapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, true);
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		mapper.configure(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS, false);
		mapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
		mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, true);

		mapper.setVisibilityChecker(new VisibilityChecker.Std(ANY, ANY, ANY, ANY, ANY));

		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		mapper = null;
	}

	@Override
	public String serialize(Object o) throws SerializerException {
		try {
			return mapper.writeValueAsString(o);
		} catch (IOException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public <T> T deserialize(String o, Class<T> targetType) throws SerializerException {
		try {
			return mapper.readValue(o, targetType);
		} catch (IOException e) {
			throw new SerializerException(e);
		}
	}
}
