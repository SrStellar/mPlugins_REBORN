package tk.slicecollections.maxteer.database.cache;

import lombok.Getter;
import tk.slicecollections.maxteer.player.Profile;

public abstract class ContainerAbstract {

    @Getter
    private final Profile profile;
    private final Class<? extends DataCache> classTableManager;

    public ContainerAbstract(Profile profile, Class<? extends DataCache> classTableManager) {
        this.profile = profile;
        this.classTableManager = classTableManager;
        load();
    }

    public abstract void load();

    public <T extends DataCollectionCache> T loadCollection(Class<T> classColumn) {
        return this.profile.getCache().loadTableCache(classTableManager).loadCollection(classColumn);
    }
}
