package se.inera.certificate.integration.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            String utcString = String.format("%d-%d-%d", year, month, day);
            InternalDateAdapter.parseInternalDate(utcString);
        case VALUE_NUMBER_INT:
            return InternalDateAdapter.parseInternalDate(jp.getText().trim());
        case VALUE_STRING:
            String str = jp.getText().trim();
            if (str.length() == 0) { // [JACKSON-360]
                return null;
            } 
            //If the string is on the format yyyy-MM-dd
            if (isDate(str)) {
                return InternalDateAdapter.parseInternalDate(str);
            } 
            //else if it is on utc format yyyy-MM-dd'T'HH:mm:ss.SSSZ drop information after dd
            else if (isUtcDate(str)) {
                return InternalDateAdapter.parseInternalDate(str.substring(0, 10));
            }
            else {
                return new InternalDate(str);
            }
        }
        
        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
    }

    private boolean isDate(String str) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        try {
            formatter.parseLocalDate(str);
        } catch (IllegalArgumentException pe) {
            return false;
        }
        return true;
    }

    private boolean isUtcDate(String str) {
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        try {
           formatter.parseLocalDate(str);
        } catch (IllegalArgumentException pe) {
            return false;
        }
        return true;
    }

}
