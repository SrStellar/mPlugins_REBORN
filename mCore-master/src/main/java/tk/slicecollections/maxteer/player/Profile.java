package tk.slicecollections.maxteer.player;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.cache.PlayerCache;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.interfaces.DataCacheInterface;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.game.GameTeam;
import tk.slicecollections.maxteer.player.cash.CashManager;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.player.scoreboard.MScoreboard;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author oJVzinn
 */

@RequiredArgsConstructor
public class Profile {

    private static final ConcurrentHashMap<String, Profile> PROFILES = new ConcurrentHashMap<>();

    public static Collection<Profile> listProfiles() {
        return PROFILES.values();
    }

    public static Profile loadProfile(String name) {
        if (!PROFILES.containsKey(name)) {
            return null;
        }

        return PROFILES.get(name);
    }

    public static void createProfile(String name) {
        Profile profile = new Profile(name);
        profile.load();
        PROFILES.put(name, profile);
    }

    public static void destroy(Profile profile) {
        profile.save();
        PROFILES.remove(profile.getName());
    }

    public static boolean isOnline(String playerName) {
        return PROFILES.containsKey(playerName.toLowerCase());
    }

    @NonNull
    @Getter
    private String name;

    @Getter
    private PlayerCache cache;

    @Getter
    private MScoreboard scoreboard;

    private Game<? extends GameTeam> game;
    private Map<String, Long> lastHit = new HashMap<>();
    private Player player;

    public void load() {
        cache = new PlayerCache(this.name);
        cache.setupDataCache();
    }

    public void save() {
        this.cache.listDataCache().forEach(dataCache -> dataCache.saveValueCollections(true));
        if (this.lastHit != null && !this.lastHit.isEmpty()) {
            this.lastHit.clear();
            this.lastHit = null;
        }

        this.game = null;
        if (this.scoreboard != null) {
            this.scoreboard.destroy();
            this.scoreboard = null;
        }
    }

    public void saveSync() {
        this.cache.listDataCache().forEach(dataCache -> dataCache.saveValueCollections(false));
        if (this.lastHit != null && !this.lastHit.isEmpty()) {
            this.lastHit.clear();
            this.lastHit = null;
        }

        this.game = null;
        if (this.scoreboard != null) {
            this.scoreboard.destroy();
            this.scoreboard = null;
        }
    }

    public CashManager getCashManager() {
        return new CashManager(this);
    }

    public void setGame(Game<? extends GameTeam> game) {
        this.game = game;
        this.lastHit.clear();
    }

    public void setHit(String name) {
        this.lastHit.put(name, System.currentTimeMillis() + 8000);
    }

    public void setScoreboard(MScoreboard scoreboard) {
        if (this.scoreboard != null) {
            this.scoreboard.destroy();
        }
        this.scoreboard = scoreboard;
    }

    public void update() {
        this.scoreboard.update();
    }

    public void destroy() {
        destroy(this);
    }

    public void refresh() {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }

        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExhaustion(0.0f);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.closeInventory();
        player.spigot().setCollidesWithEntities(true);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }

        if (!playingGame()) {
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(Core.getLobby());

            player.setAllowFlight(player.hasPermission("score.fly"));
            try {
                cache.loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class).updateValue("role", StringUtils.stripColors(Role.findRoleByPermission(player).getName()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        this.refreshPlayers();
    }

    public void refreshPlayers() {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        if (this.player == null) {
            this.player = Bukkit.getPlayerExact(this.name);
        }

        return this.player;
    }

    public Game<?> getGame() {
        return this.getGame(Game.class);
    }

    @SuppressWarnings("unchecked")
    public <T extends Game<?>> T getGame(Class<T> gameClass) {
        return this.game != null && gameClass.isAssignableFrom(this.game.getClass()) ? (T) this.game : null;
    }

    public boolean playingGame() {
        return this.game != null;
    }

    public List<Profile> getLastHitters() {
        List<Profile> hitters = this.lastHit.entrySet().stream()
                .filter(entry -> entry.getValue() > System.currentTimeMillis() && isOnline(entry.getKey()))
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(entry -> loadProfile(entry.getKey()))
                .collect(Collectors.toList());

        this.lastHit.clear();
        return hitters;
    }

}
