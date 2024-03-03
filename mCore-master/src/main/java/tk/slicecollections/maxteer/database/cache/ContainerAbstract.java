package tk.slicecollections.maxteer.database.cache;

import lombok.Getter;
import tk.slicecollections.maxteer.player.Profile;

public abstract class ContainerAbstract {

    @Getter
    private final Profile profile;
    private final Class<? extends Data> classTableManager;

    public ContainerAbstract(Profile profile, Class<? extends Data> classTableManager) {
        this.profile = profile;
        this.classTableManager = classTableManager;
        load();
    }

    public abstract void load();

    public <T extends DataCollection> T loadCollection(Class<T> classColumn) {
        return this.profile.getCache().loadTableCache(classTableManager).loadCollection(classColumn);
    }

    public <T extends DataCollection> T loadCollectionGeneric(Class<T> classColumn, String columnName) {
        return this.profile.getCache().loadTableCache(classTableManager).loadCollectionGeneric(classColumn, columnName);
    }
}
