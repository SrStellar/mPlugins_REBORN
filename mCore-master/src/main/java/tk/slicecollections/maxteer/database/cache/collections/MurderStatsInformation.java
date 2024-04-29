package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

@SuppressWarnings("unchecked")
public class MurderStatsInformation extends DataCollection {

    public MurderStatsInformation(String playerKey) {
        super("stats", "mCoreMurder", false, "", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreMurder", "stats")) {
                mySQL.addColumn("mCoreMurder", "stats");
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        JSONObject defaultObject = new JSONObject();
        defaultObject.put("clkills", 0L);
        defaultObject.put("clbowkills", 0L);
        defaultObject.put("clknifekills", 0L);
        defaultObject.put("clthrownknifekills", 0L);
        defaultObject.put("clwins", 0L);
        defaultObject.put("cldetectivewins", 0L);
        defaultObject.put("clkillerwins", 0L);
        defaultObject.put("clquickestdetective", 0L);
        defaultObject.put("clquickestkiller", 0L);
        defaultObject.put("clchancedetective", 0L);
        defaultObject.put("clchancekiller", 0L);
        defaultObject.put("askills", 0L);
        defaultObject.put("asthrownknifekills", 0L);
        defaultObject.put("aswins", 0L);
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
