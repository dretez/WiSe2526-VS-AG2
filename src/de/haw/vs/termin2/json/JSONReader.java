package de.haw.vs.termin2.json;

import java.util.List;
import java.util.Map;

public class JSONReader {
    private final Object json;

    public JSONReader(Object json) {
        this.json = json;
    }
    public JSONReader(String json) {
        this(JSONParser.parse(json));
    }

    public Object get(String keyPath) {
        String[] parts = keyPath.split("\\.");
        Object value = json;

        for (String part : parts) {
            if (value instanceof Map) {
                value = ((Map<?, ?>)value).get(part);
            } else if (value instanceof List && part.matches("\\d+")) {
                value = ((List<?>)value).get(Integer.parseInt(part));
            } else {
                return null;
            }
        }

        return value;
    }
}
