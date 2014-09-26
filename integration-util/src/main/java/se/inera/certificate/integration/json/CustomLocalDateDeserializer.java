package se.inera.certificate.integration.json;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * This class is based on the LocalDateDeserializer class. It's content was copied
 * and slightly changed to support dates on the UTC format.
 * 
 * Changes:
 * <li>
 * <ul>The ISODateTimeFormat.dateTimeParser() is used instead of the ISODateTimeFormat.localDateParser()</ul>
 * <ul>In method deserializer the case START_ARRAY handles year, month and day as the original method does but here we
 * do not check if, after day, the next token is END_ARRAY since it's possible next token can be an int value (hours).</ul>
 * </li> 
 *  
 * @see com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer
 * @author Magnus Ekstrand
 *
 */
public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    
    private static final DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();

    public CustomLocalDateDeserializer() {}

    /**
     * <strong>Description copied from class: JsonDeserializer</strong>
     * Method that can be called to ask implementation to deserialize json content into the value type this serializer handles. 
     * Returned instance is to be constructed by method itself. 
     * 
     * Pre-condition for this method is that the parser points to the 
     * first event that is part of value to deserializer (and which is never Json 'null' literal, more on this below): for simple 
     * types it may be the only value; and for structured types the Object start marker. 
     * 
     * Post-condition is that the parser will point to the last event that is part of deserialized value (or in case deserialization
     * fails, event that was not recognized or usable, which may be the same event as the one it pointed to upon call).
     * 
     * Note that this method is never called for JSON null literal, and thus deserializers need (and should) not check for it.
     * 
     * @param jp - Parser used for reading Json content
     * @param ctxt - Context that can be used to access information about this deserialization activity.
     * @return Deserializer value as LocalDate
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    @SuppressWarnings("incomplete-switch")
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
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
