package tk.slicecollections.maxteer.database.cache.collections;

import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

public class CoinsGenericInformation extends DataCollection {

    public CoinsGenericInformation(String playerKey, String column, String table) {
        super(column, table, null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn(this.getTableName(), this.getColumnName())) {
                mySQL.addColumn(this.getTableName(), this.getColumnName());
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        return 0.0D;
    }

    @Override
    public void saveValue() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            mySQL.updateColumn(this.getTableName(), this.getColumnName(), this.getValue(), "name = '" + this.getPlayerKey() + "'");
        }
    }

    public void addCoins(Double value) {
        updateValue(this.getAsDouble() + value);
    }


    public void removeCoins(Double value) {
        updateValue(this.getAsDouble() - value);
    }

    public Double getCoins() {
        return this.getAsDouble();
    }

}
