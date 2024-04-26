package tk.slicecollections.maxteer.database.cache.types;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.Data;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.AchievementsInformation;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.collections.TitleInformation;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCollectionsInterface;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileCache extends Data {

    public ProfileCache(String playerKey) {
        super("mCoreProfile", playerKey);
        setupTables();
        setupCollections(ProfileInformation.class, SelectedInformation.class, TitleInformation.class, AchievementsInformation.class);
    }

    @Override
    public Data setupTables() {
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
    public final void setupCollections(Class<? extends DataCollection>... collections) {
        Arrays.stream(collections).forEach(clazz -> {
            try {
                Constructor<? extends DataCollection> constructor = clazz.getConstructor(String.class);
                DataCollection collectionCache = constructor.newInstance(this.playerKey);
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
    @SuppressWarnings("unchecked")
    public void loadValueCollections(boolean asyncTask) {
        Thread task = new Thread(() -> {
            DataTypes type = Database.getInstance().getType();
            Map<String, Object> collectionsValue;
            if (type.equals(DataTypes.MYSQL)) {
                MySQL mySQL = ((MySQL) Database.getInstance());
                List<String> columns = listCollections().stream().map(DataCollection::getColumnName).collect(Collectors.toList());
                collectionsValue = mySQL.getValueColumn(this.tableName, "name = '" + this.playerKey + "'", columns);
                if (collectionsValue.isEmpty()) {
                    mySQL.insertValue(this.tableName, "name", this.playerKey);
                }
            } else {
                collectionsValue = null;
            }

            listCollections().forEach(collectionCache -> {
                if (collectionsValue != null && !collectionsValue.isEmpty() && collectionsValue.get(collectionCache.getColumnName()) != null) {
                    try { //Verifica se é um JSON valido e se existe alguma key nova que não está registrada nos valores do jogador
                        JSONObject plyObject = (JSONObject) new JSONParser().parse(collectionsValue.get(collectionCache.getColumnName()).toString());
                        JSONObject defaultObject = (JSONObject) new JSONParser().parse(collectionCache.getDefaultValue().toString());
                        List<Object> noHasKey = Arrays.stream(defaultObject.keySet().toArray()).filter(key -> !plyObject.containsKey(key)).collect(Collectors.toList());
                        if (!noHasKey.isEmpty()) {
                            noHasKey.forEach(key -> plyObject.put(key, defaultObject.get(key)));
                        }
                        collectionCache.updateValue(plyObject);
                        return;
                    } catch (Exception ignored) {
                        collectionCache.updateValue(collectionsValue.get(collectionCache.getColumnName()));
                        return;
                    }
                }

                collectionCache.updateValue(collectionCache.getDefaultValue());
            });
        });


        if (asyncTask) {
            task.start();
        } else {
            task.run();
        }
    }

    @Override
    public void saveValueCollections(boolean asyncTask) {
        Thread task = new Thread(()-> listCollections().forEach(DataCollectionsInterface::saveValue));

        if (asyncTask) {
            task.start();
        } else {
            task.run();
        }
    }

}
