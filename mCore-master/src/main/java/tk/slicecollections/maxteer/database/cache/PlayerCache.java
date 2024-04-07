package tk.slicecollections.maxteer.database.cache;

import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.database.cache.types.TheBridgeCache;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PlayerCache {

    @Nonnull
    private String playerKey;
    private final List<Data> DATA_CACHE = new ArrayList<>();

    public void setupDataCache() {
        DATA_CACHE.add(new ProfileCache(playerKey));
        DATA_CACHE.add(new SkyWarsCache(playerKey));
        DATA_CACHE.add(new TheBridgeCache(playerKey));
    }

    public List<Data> listDataCache() {
        return DATA_CACHE;
    }

    @SuppressWarnings("unchecked")
    public <T extends Data> T loadTableCache(Class<T> classTableCache) {
        return (T) DATA_CACHE.stream().filter(dataCache -> dataCache.getClass().isAssignableFrom(classTableCache)).findFirst().orElse(null);
    }

}
