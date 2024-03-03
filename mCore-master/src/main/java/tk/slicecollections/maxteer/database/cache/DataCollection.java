package tk.slicecollections.maxteer.database.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCollectionsInterface;

@Getter
@AllArgsConstructor
public abstract class DataCollection implements DataCollectionsInterface {

    @NonNull
    private String columnName;

    @NonNull
    private String tableName;

    private Object value;

    private String playerKey;

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
