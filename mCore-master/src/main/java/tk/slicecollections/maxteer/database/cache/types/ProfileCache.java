package tk.slicecollections.maxteer.database.cache.types;

import org.bukkit.Bukkit;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCache;
import tk.slicecollections.maxteer.database.cache.DataCollectionCache;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.collections.TitleInformation;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCollectionsCacheInterface;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ProfileCache extends DataCache {

    public ProfileCache(String playerKey) {
        super("mCoreProfile", playerKey);
        setupTables();
        setupCollections(ProfileInformation.class, SelectedInformation.class, TitleInformation.class);
    }

    @Override
    public DataCache setupTables() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            ((MySQL) Database.getInstance()).setupTable(this.tableName, "`name` VARCHAR(32) PRIMARY KEY NOT NULL, ",
                    "`informations` TEXT, ",
                    "`titles` VARCHAR(255), ",
                    "`boosters` VARCHAR(255), ",
                    "`achievements` VARCHAR(255), ",
                    "`selected` VARCHAR(255)");
        }

        return this;
    }

    @SafeVarargs
    @Override
    public final void setupCollections(Class<? extends DataCollectionCache>... collections) {
        Arrays.stream(collections).forEach(clazz -> {
            try {
                Constructor<? extends DataCollectionCache> constructor = clazz.getConstructor(String.class);
                DataCollectionCache collectionCache = constructor.newInstance(this.playerKey);
                collectionCache.setupColumn();
                registerNewCollection(collectionCache);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        if (this.playerKey.isEmpty()) {
            return;
        }

        loadValueCollections(false);
    }

    @Override
    public void loadValueCollections(boolean asyncTask) {
        Runnable task = () -> {
            DataTypes type = Database.getInstance().getType();
            Map<String, Object> collectionsValue;
            if (type.equals(DataTypes.MYSQL)) {
                MySQL mySQL = ((MySQL) Database.getInstance());
                List<String> columns = listCollections().stream().map(DataCollectionCache::getColumnName).collect(Collectors.toList());
                collectionsValue = mySQL.getValueColumn(this.tableName, "name = '" + this.playerKey + "'", columns);
                if (collectionsValue.isEmpty()) {
                    mySQL.insertValue(this.tableName, "name", this.playerKey);
                }
            } else {
                collectionsValue = null;
            }

            listCollections().forEach(collectionCache -> {
                if (collectionsValue != null && !collectionsValue.isEmpty() && collectionsValue.get(collectionCache.getColumnName()) != null) {
                    collectionCache.updateValue(collectionsValue.get(collectionCache.getColumnName()));
                    return;
                }

                collectionCache.updateValue(collectionCache.getDefaultValue());
            });
        };


        if (asyncTask) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), task);
        } else {
            task.run();
        }
    }

    @Override
    public void saveValueCollections(boolean asyncTask) {
        Runnable task = ()-> listCollections().forEach(collectionCache -> listCollections().forEach(DataCollectionsCacheInterface::saveValue));

        if (asyncTask) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), task);
        } else {
            task.run();
        }
    }

}
