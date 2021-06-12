package uni.ruse.welearn.welearn.util;

import java.io.IOException;
import java.sql.Time;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return Time.valueOf(jp.getValueAsString() + ":00");
    }
}
