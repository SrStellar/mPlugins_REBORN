package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

@SuppressWarnings("unchecked")
public class BoosterNetworkInformation extends DataCollection {

    public BoosterNetworkInformation(String minigame) {
        super("booster", "mCoreBooster", false, "", null, minigame);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreBooster", "booster")) {
                mySQL.addColumn("mCoreBooster", "booster");
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        JSONObject defaultObject = new JSONObject();
        defaultObject.put("time", 0L);
        defaultObject.put("multiply", 1.0);
        defaultObject.put("owner", "");
        return defaultObject.toJSONString();
    }

    @Override
    public void saveValue() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            mySQL.updateColumn(this.getTableName(), this.getColumnName(), this.getValue(), "minigame = '" + this.getPlayerKey() + "'");
        }
    }

    public void updateValue(String key, Object value) throws ParseException {
        JSONObject newValue = getAsJsonObject();
        newValue.put(key, value);
        this.updateValue(newValue.toJSONString());
    }

    public <T> T getInformation(String key, T classT) throws ParseException {
        return (T) getAsJsonObject().get(key);
    }

    public <T> T getInformation(String key) throws ParseException {
        return (T) getAsJsonObject().get(key);
    }
}
