package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollectionCache;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;
import tk.slicecollections.maxteer.titles.Title;

public class TitleInformation extends DataCollectionCache {
    public TitleInformation(String playerKey) {
        super("titles", "mCoreProfile", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreProfile", "titles")) {
                mySQL.addColumn("mCoreProfile", "titles");
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        return new JSONArray();
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
    public void addNewTitle(Title title) throws ParseException {
        JSONArray currentTitles = this.getAsJsonArray();
        currentTitles.add(title.getId());
        this.updateValue(currentTitles);
    }

    public void removeTitle(Title title) throws ParseException {
        JSONArray currentTitles = this.getAsJsonArray();
        currentTitles.remove(title.getId());
        this.updateValue(currentTitles);
    }

    public boolean hasTitle(Title title) throws ParseException {
        JSONArray currentTitles = this.getAsJsonArray();
        return currentTitles.contains(String.valueOf(title.getId()));
    }
}
