package se.inera.certificate.integration.json;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.schema.adapter.InternalDateAdapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InternalDateDeserializer extends StdDeserializer<InternalDate> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InternalDateDeserializer() {
        super(InternalDate.class);
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public InternalDate deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        switch (jp.getCurrentToken()) {
        case START_ARRAY:
            //[yyyy,MM,dd,hh,mm,ss,ms]
            jp.nextToken(); // VALUE_NUMBER_INT
            int year = jp.getIntValue();
            jp.nextToken(); // VALUE_NUMBER_INT
            int month = jp.getIntValue();
            jp.nextToken(); // VALUE_NUMBER_INT
            int day = jp.getIntValue();
            // We are only interested in year, month and day
            // Skip the time and return at date
            return InternalDateAdapter.parseInternalDate(year, month, day);
        case VALUE_NUMBER_INT:
            return new InternalDate(new LocalDate(jp.getLongValue()));
        case VALUE_STRING:
            String str = jp.getText().trim();
            if (str.length() == 0) { // [JACKSON-360]
                return null;
            } 
            //If the string is on the format yyyy-MM-dd
            if (isDate(str, "date")) {
                return InternalDateAdapter.parseInternalDate(str);
            } 
            //else if it is on utc format yyyy-MM-dd'T'HH:mm:ss.SSSZ drop information after dd
            else if (isDate(str, "utcDate")) {
                return InternalDateAdapter.parseInternalDate(str.substring(0, 10));
            } 
            //else if it is on utc format yyyy-MM-dd'T'HH:mm:ss.SSS drop information after dd
            else if(isDate(str, "localDate")) {
                return InternalDateAdapter.parseInternalDate(str.substring(0, 10));
            }
            else {
                return new InternalDate(str);
            }
        }
        
        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
    }

    private boolean isDate(String str, String format) {
        DateTimeFormatter formatter = null;
        switch (format) {
            case "date":
                formatter = ISODateTimeFormat.date();
            break;
            case "utcDate":
                formatter = ISODateTimeFormat.dateTime();
            break;
            case "localDate":
                formatter = ISODateTimeFormat.dateHourMinuteSecondFraction();
            break;
        }
        try {
            formatter.parseLocalDate(str);
        } catch (IllegalArgumentException pe) {
            return false;
        }
        return true;
        
    }
}
