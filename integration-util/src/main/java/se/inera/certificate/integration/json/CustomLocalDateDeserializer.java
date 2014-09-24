package se.inera.certificate.integration.json;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.interceptor.SoapFaultToSoapResponseTransformerInterceptor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    
    private static final DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();

    public CustomLocalDateDeserializer() {}

    @Override
    @SuppressWarnings("incomplete-switch")
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

        switch (jp.getCurrentToken()) {
        case START_ARRAY:
            jp.nextToken(); // VALUE_NUMBER_INT
            int year = jp.getIntValue();
            jp.nextToken(); // VALUE_NUMBER_INT
            int month = jp.getIntValue();
            jp.nextToken(); // VALUE_NUMBER_INT
            int day = jp.getIntValue();
                
            // We are only interested in year, month and day
            // Skip the time and return at date
            return new LocalDate(year, month, day);                 
        case VALUE_NUMBER_INT:
            return new LocalDate(jp.getLongValue());            
        case VALUE_STRING:
            String str = jp.getText().trim();
            if (str.length() == 0) { // [JACKSON-360]
                return null;
            }
            return parser.parseLocalDate(str);
        }
        
        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
    }
}
