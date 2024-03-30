package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

@SuppressWarnings("unchecked")
public class SkyWarsStatsInformation extends DataCollection {
    public SkyWarsStatsInformation(String playerKey) {
        super("stats", "mCoreSkyWars", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreSkyWars", "stats")) {
                mySQL.addColumn("mCoreSkyWars", "stats");
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        JSONObject defaultObject = new JSONObject();
        defaultObject.put("1v1kills", 0L);
        defaultObject.put("1v1deaths", 0L);
        defaultObject.put("1v1assists", 0L);
        defaultObject.put("1v1games", 0L);
        defaultObject.put("1v1wins", 0L);
        defaultObject.put("2v2kills", 0L);
        defaultObject.put("2v2deaths", 0L);
        defaultObject.put("2v2assists", 0L);
        defaultObject.put("2v2games", 0L);
        defaultObject.put("2v2wins", 0L);
        return defaultObject.toJSONString();
    }

    @Override
    public void saveValue() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            mySQL.updateColumn(this.getTableName(), this.getColumnName(), this.getValue(), "name = '" + this.getPlayerKey() + "'");
        }
    }

    public void updateValue(String key, Object value) throws ParseException {
        JSONObject newValue = getAsJsonObject();
        newValue.put(key, value);
        this.updateValue(newValue.toJSONString());
    }

    public <T> T getInformation(String key) throws ParseException {
        return (T) getAsJsonObject().get(key);
    }

    public <T> T getInformation(String key, Class<T> tClass) throws ParseException {
        return (T) getAsJsonObject().get(key);
    }
}
