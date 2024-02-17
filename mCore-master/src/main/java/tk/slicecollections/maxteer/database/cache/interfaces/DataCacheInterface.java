package tk.slicecollections.maxteer.database.cache.interfaces;

import tk.slicecollections.maxteer.database.cache.DataCache;
import tk.slicecollections.maxteer.database.cache.DataCollectionCache;

public interface DataCacheInterface {

    DataCache setupTables();
    void setupCollections(Class<? extends DataCollectionCache>... collections);
    void loadValueCollections(boolean asyncTask);
    void saveValueCollections(boolean asyncTask);
}
