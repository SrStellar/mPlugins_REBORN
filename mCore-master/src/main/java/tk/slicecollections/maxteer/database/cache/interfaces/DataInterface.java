package tk.slicecollections.maxteer.database.cache.interfaces;

import tk.slicecollections.maxteer.database.cache.Data;
import tk.slicecollections.maxteer.database.cache.DataCollection;

public interface DataInterface {

    Data setupTables();
    void setupCollections(Class<? extends DataCollection>... collections);
    void loadValueCollections(boolean asyncTask);
    void saveValueCollections(boolean asyncTask);
}
