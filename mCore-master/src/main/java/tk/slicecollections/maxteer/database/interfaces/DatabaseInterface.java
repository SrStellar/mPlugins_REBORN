package tk.slicecollections.maxteer.database.interfaces;

import tk.slicecollections.maxteer.database.enuns.DataTypes;

public interface DatabaseInterface {

    void setupConnection() throws Exception;
    void destroyConnection();
    DataTypes getType();
}
