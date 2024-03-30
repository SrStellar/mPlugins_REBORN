package tk.slicecollections.maxteer.database.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.database.cache.interfaces.DataInterface;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;

import java.util.*;

@RequiredArgsConstructor
public abstract class Data implements DataInterface {

    public static void setupDataCache() {
        new ProfileCache("");
    }

    @NonNull
    protected String tableName;

    @Getter
    @NonNull
    protected String playerKey;

    private final Set<DataCollection> dataCollectionCache = new HashSet<>();

    public void registerNewCollection(DataCollection collectionCache) {
        this.dataCollectionCache.add(collectionCache);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataCollection> T loadCollection(Class<T> collection) {
        return (T) dataCollectionCache.stream().filter(collectionCache -> collectionCache.getClass().isAssignableFrom(collection)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataCollection> T loadCollectionGeneric(Class<T> collection, String columnName) {
        return (T) dataCollectionCache.stream().filter(collectionCache -> collectionCache.getClass().isAssignableFrom(collection) && collectionCache.getColumnName().equals(columnName)).findFirst().orElse(null);
    }

    public Collection<DataCollection> listCollections() {
        return this.dataCollectionCache;
    }
}
