package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollectionCache;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

public class ProfileInformation extends DataCollectionCache {

    public ProfileInformation(String playerKey) {
        super("informations", "mCoreProfile", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreProfile", "informations")) {
                mySQL.addColumn("mCoreProfile", "informations");
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getDefaultValue() {
        JSONObject defaultObject = new JSONObject();
        defaultObject.put("cash", 0L);
        defaultObject.put("role", StringUtils.stripColors(Role.getDefaultRole().getName()));
        defaultObject.put("deliveries", new JSONObject());
        defaultObject.put("preferences", new JSONObject());
        defaultObject.put("created", "");
        defaultObject.put("lastLogin", "");
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

    @SuppressWarnings("unchecked")
    public void updateValue(String key, Object value) throws ParseException {
        JSONObject newValue = getAsJsonObject();
        newValue.put(key, value);
        this.updateValue(newValue.toJSONString());
    }
}
