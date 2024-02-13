package tk.slicecollections.maxteer.database.cache;

import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PlayerCache {

    @Nonnull
    private String playerKey;
    private final List<DataCache> DATA_CACHE = new ArrayList<>();

    public void setupDataCache() {
        DATA_CACHE.add(new ProfileCache(playerKey).setupTables());
    }

    public List<DataCache> listDataCache() {
        return DATA_CACHE;
    }

    @SuppressWarnings("unchecked")
    public <T extends DataCache> T loadTableCache(Class<T> classTableCache) {
        return (T) DATA_CACHE.stream().filter(dataCache -> dataCache.getClass().isAssignableFrom(classTableCache)).findFirst().orElse(null);
    }

}
