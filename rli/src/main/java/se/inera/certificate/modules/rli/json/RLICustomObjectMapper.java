package se.inera.certificate.modules.rli.json;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.integration.json.TrimmingStringSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

public class RLICustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = -3291211792669869270L;

	public RLICustomObjectMapper() {
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		registerModule(new RLIModule());
	}
	
	private static final class RLIModule extends SimpleModule {

		private static final long serialVersionUID = -5095620428675230583L;

		public RLIModule() {
			addSerializer(String.class, new TrimmingStringSerializer());

            addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

            addSerializer(LocalDate.class, new LocalDateSerializer());
            addDeserializer(LocalDate.class, new LocalDateDeserializer());
		}
		
	}
}
