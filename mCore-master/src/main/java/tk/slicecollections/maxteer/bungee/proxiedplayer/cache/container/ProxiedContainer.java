package tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Getter
@RequiredArgsConstructor
public abstract class ProxiedContainer {

    @NonNull
    private String name;

    private Object value;
    public void updateValue(Object object) {
        this.value = object;
    }

    public String getAsString() {
        return value.toString();
    }

    public Long getAsLong() {
        return Long.parseLong(value.toString());
    }

    public Integer getAsInt() {
        return Integer.parseInt(value.toString());
    }

    public Double getAsDouble() {
        return Double.parseDouble(value.toString());
    }

    public JSONArray getAsJsonArray() throws ParseException {
        return (JSONArray) new JSONParser().parse(this.value.toString());
    }

    public JSONObject getAsJsonObject() throws ParseException {
        return (JSONObject) new JSONParser().parse(this.value.toString());
    }
}
