package tk.slicecollections.maxteer.bungee.proxiedplayer;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.ProxiedCache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ProxiedProfile {

    private static final Map<String, ProxiedProfile>  PROFILES = new HashMap<>();

    @NonNull
    private String name;
    private ProxiedCache cache;

    public static Collection<ProxiedProfile> listProfiles() {
        return PROFILES.values();
    }

    public static ProxiedProfile loadProfile(String name) {
        if (!PROFILES.containsKey(name)) {
            return null;
        }

        return PROFILES.get(name);
    }

    public static void createProfile(String name) {
        if (PROFILES.containsKey(name)) {
            return;
        }

        ProxiedProfile profile = new ProxiedProfile(name);
        profile.load();
        PROFILES.put(name, profile);
    }

    public static void destroy(ProxiedProfile profile) {
        PROFILES.remove(profile.getName());
    }

    public static boolean isOnline(String playerName) {
        return PROFILES.containsKey(playerName.toLowerCase());
    }

    @SneakyThrows
    public void load() {
        ProxiedCache cache = new ProxiedCache(this.name);
        cache.load();
        this.cache = cache;
    }

    public void destroy() {
        destroy(this);
    }
}
