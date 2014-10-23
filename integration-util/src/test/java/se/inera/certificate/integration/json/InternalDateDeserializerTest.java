package se.inera.certificate.integration.json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.certificate.model.InternalDate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public final class InternalDateDeserializerTest {

    private static InternalDateDeserializer deserializer;
    private static JsonFactory factory;
    
    @BeforeClass
    public static void setup() {
        deserializer = new InternalDateDeserializer();
        factory = new JsonFactory();
    }
    
    @Test
    public void deserializeWhenDateTimeIsUTC() throws JsonParseException, IOException {
        
        String date = "2014-09-22T00:00:00.000Z";
        String json = "{\"journalanteckningar\":\"" + date + "\"}";

        JsonParser jp = factory.createParser(json);
        setJsonParserAtCorrectToken(jp);
        
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString())).
                thenReturn(JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + JsonToken.START_ARRAY + ": expected JSON Array, Number or String"));    // Mock implementation
        
        // Deserialize JSON string
        InternalDate ld = deserializer.deserialize(jp, ctxt);

        // Assert that we have a correct date
        assertInternalDate(ld);
    }

    @Test
    public void deserializeWhenDateTimeIsLocal() throws JsonParseException, IOException {
        
        String date = "2014-09-22T00:00:00.000";
        String json = "{\"journalanteckningar\":\"" + date + "\"}";

        JsonParser jp = factory.createParser(json);
        setJsonParserAtCorrectToken(jp);
        
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString())).
                thenReturn(JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + JsonToken.START_ARRAY + ": expected JSON Array, Number or String"));    // Mock implementation
        
        // Deserialize JSON string
        InternalDate ld = deserializer.deserialize(jp, ctxt);

        // Assert that we have a correct date
        assertInternalDate(ld);
    }

    @Test
    public void deserializeWhenOnlyDate() throws JsonParseException, IOException {
        
        String date = "2014-09-22";
        String json = "{\"journalanteckningar\":\"" + date + "\"}";

        JsonParser jp = factory.createParser(json);
        setJsonParserAtCorrectToken(jp);
        
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString())).
                thenReturn(JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + JsonToken.START_ARRAY + ": expected JSON Array, Number or String"));    // Mock implementation
        
        // Deserialize JSON string
        InternalDate ld = deserializer.deserialize(jp, ctxt);
        
        // Assert that we have a correct date
        assertInternalDate(ld);
    }

    @Test
    public void deserializeWhenDateTimeIsJsonArray() throws JsonParseException, IOException {
        
        String date = "[2014,9,22,00,00,00,000]";
        String json = "{\"journalanteckningar\":" + date + "}";

        JsonParser jp = factory.createParser(json);
        setJsonParserAtCorrectToken(jp);
        
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString())).
                thenReturn(JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + JsonToken.START_ARRAY + ": expected JSON Array, Number or String"));    // Mock implementation
        
        // Deserialize JSON string
        InternalDate ld = deserializer.deserialize(jp, ctxt);

        // Assert that we have a correct date
        assertInternalDate(ld);
    }
    
    @Test
    public void deserializeWhenDateTimeIsLong() throws JsonParseException, IOException {
        
        String date = "1411391603828";
        String json = "{\"journalanteckningar\":" + date + "}";

        JsonParser jp = factory.createParser(json);
        setJsonParserAtCorrectToken(jp);
        
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString())).
                thenReturn(JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + JsonToken.START_ARRAY + ": expected JSON Array, Number or String"));    // Mock implementation
        
        // Deserialize JSON string
        InternalDate ld = deserializer.deserialize(jp, ctxt);

        // Assert that we have a correct date
        assertInternalDate(ld);
    }

    private void setJsonParserAtCorrectToken(JsonParser jp) throws IOException, JsonParseException {
        // loop over all fields in JSON object
        while(jp.nextToken() != JsonToken.END_OBJECT) {
            String field = jp.getCurrentName();
            if("journalanteckningar".equals(field)) {
                break;
            }
            // move to next token (which is the field value)
            jp.nextToken();
        }
    }
    
    private void assertInternalDate(InternalDate localDate) throws IOException, JsonProcessingException {
        assertEquals("2014-09-22", localDate.getDate());
    }
    
}
