package tk.slicecollections.maxteer.database.cache;

import javafx.concurrent.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCollectionsCacheInterface;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@AllArgsConstructor
public abstract class DataCollectionCache implements DataCollectionsCacheInterface {

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
        return (String) value;
    }

    public Long getAsLong() {
        return (Long) value;
    }

    public Integer getAsInt() {
        return (Integer) value;
    }

    public JSONArray getAsJsonArray() throws ParseException {
        return (JSONArray) new JSONParser().parse(this.value.toString());
    }

    public JSONObject getAsJsonObject() throws ParseException {
        return (JSONObject) new JSONParser().parse(this.value.toString());
    }
}
