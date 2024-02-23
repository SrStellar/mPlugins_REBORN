package tk.slicecollections.maxteer.database.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCacheInterface;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class DataCache implements DataCacheInterface {

    public static void setupDataCache() {
        new ProfileCache("");
    }

    @NonNull
    protected String tableName;

    @Getter
    @NonNull
    protected String playerKey;

    private final List<DataCollectionCache> dataCollectionCache = new ArrayList<>();

    public void registerNewCollection(DataCollectionCache collectionCache) {
        this.dataCollectionCache.add(collectionCache);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataCollectionCache> T loadCollection(Class<T> collection) {
        return (T) dataCollectionCache.stream().filter(collectionCache -> collectionCache.getClass().isAssignableFrom(collection)).findFirst().orElse(null);
    }

    public List<DataCollectionCache> listCollections() {
        return this.dataCollectionCache;
    }
}
