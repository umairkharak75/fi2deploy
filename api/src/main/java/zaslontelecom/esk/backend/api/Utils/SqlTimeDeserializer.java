package zaslontelecom.esk.backend.api.Utils;

import java.io.IOException;
import java.sql.Time;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SqlTimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String val = jp.getValueAsString();
        return Time.valueOf(val.substring(0,10));
    }
}
