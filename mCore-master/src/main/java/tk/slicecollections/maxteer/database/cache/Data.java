package tk.slicecollections.maxteer.database.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.interfaces.DataInterface;
import tk.slicecollections.maxteer.database.cache.types.*;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

import java.util.*;

@RequiredArgsConstructor
public abstract class Data implements DataInterface {

    public static void setupDataCache() {
        new ProfileCache("", true, false);
        new SkyWarsCache("", true, false);
        new TheBridgeCache("", true, false);
        new MurderCache("", true, false);
        new BoosterCache("", true, false);
    }

    @NonNull
    protected String tableName;

    @Getter
    @NonNull
    protected String playerKey;

    protected boolean loadValue;

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

    public void defaultSave(boolean async) {
        Map<String, Object> columnsValue = new HashMap<>();
        listCollections().forEach(dataCollection -> columnsValue.put(dataCollection.getColumnName(), dataCollection.getValue()));

        Thread task = new Thread(()->  {
            if (Database.getInstance().getType() == DataTypes.MYSQL) {
                ((MySQL) Database.getInstance()).updateColumns(this.tableName, columnsValue, "name = '" + this.getPlayerKey() + "'");
            }

        });


        if (async) {
            task.start();
        } else {
            task.run();
        }
    }

}
