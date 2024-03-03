package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;
import tk.slicecollections.maxteer.titles.Title;

public class SelectedInformation extends DataCollection {

    public SelectedInformation(String playerKey) {
        super("selected", "mCoreProfile", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreProfile", "selected")) {
                mySQL.addColumn("mCoreProfile", "selected");
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getDefaultValue() {
        JSONObject informations = new JSONObject();
        informations.put("title", -1);
        return informations;
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
    public void setSelectedTitle(Title title) throws ParseException {
        JSONObject currentSelected = this.getAsJsonObject();
        currentSelected.put("title", title == null ? -1 : title.getId());
        this.updateValue(currentSelected);
    }

    public Long getSelectedTitle() throws ParseException {
        try {
            JSONObject currentSelected = this.getAsJsonObject();
            return Long.parseLong(currentSelected.get("title").toString());
        } catch (Exception e) {
            return -1L;
        }
    }

}
