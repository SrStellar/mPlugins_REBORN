package tk.slicecollections.maxteer.player;

import lombok.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.cache.Data;
import tk.slicecollections.maxteer.database.cache.PlayerCache;
import tk.slicecollections.maxteer.database.cache.collections.*;
import tk.slicecollections.maxteer.database.cache.types.MurderCache;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.database.cache.types.TheBridgeCache;
import tk.slicecollections.maxteer.deliveries.DeliveryContainer;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.game.GameTeam;
import tk.slicecollections.maxteer.cash.CashManager;
import tk.slicecollections.maxteer.hook.FriendsHook;
import tk.slicecollections.maxteer.player.boosters.BoosterContainer;
import tk.slicecollections.maxteer.player.hotbar.Hotbar;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;
import tk.slicecollections.maxteer.player.preferences.PreferencesContainer;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.player.scoreboard.MScoreboard;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author oJVzinn
 */

@RequiredArgsConstructor
public class Profile {

    private static final ConcurrentHashMap<String, Profile> PROFILES = new ConcurrentHashMap<>();
    private static final Map<String, UUID> UUID_CACHE = new HashMap<>();

    public static Collection<Profile> listProfiles() {
        return PROFILES.values();
    }

    public static Player findCached(String playerName) {
        UUID uuid = UUID_CACHE.get(playerName.toLowerCase());
        return uuid == null ? null : Bukkit.getPlayer(uuid);
    }

    public static Profile loadProfile(String name) {
        if (!PROFILES.containsKey(name)) {
            return null;
        }

        return PROFILES.get(name);
    }

    public static void createProfile(String name) {
        if (PROFILES.containsKey(name)) {
            return;
        }

        Profile profile = new Profile(name);
        profile.load();
        PROFILES.put(name, profile);
    }

    public static void destroy(Profile profile) {
        UUID_CACHE.remove(profile.getName().toLowerCase());
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

    @Getter
    @Setter
    private Hotbar hotbar;

    private Game<? extends GameTeam> game;
    private Map<String, Long> lastHit = new HashMap<>();

    private Player player;

    @SneakyThrows
    public void load() {
        this.cache = new PlayerCache(this.name);
        this.cache.setupDataCache();
    }

    public void save() {
        this.cache.listDataCache().forEach(dataCache -> dataCache.saveValueCollections(true));
        this.cache = null;

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
        this.cache = null;

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

            player.setAllowFlight(Role.findRoleByPermission(player).getCanFly());
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

        if (this.hotbar != null) {
            this.hotbar.getButtons().forEach(button -> {
                if (button.getAction().getValue().equalsIgnoreCase("jogadores")) {
                    player.getInventory().setItem(button.getSlot() - 1, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(player, button.getIcon())));
                }
            });
        }

        if (!this.playingGame()) {
            for (Player online : Bukkit.getOnlinePlayers().stream().filter(online -> !online.equals(getPlayer())).collect(Collectors.toList())) {
                Profile onlineProfile = Profile.loadProfile(online.getName());
                if (onlineProfile == null) {
                    return;
                }

                if (!onlineProfile.playingGame()) {
                    if (loadPreferencesContainer().getPreference(PreferenceEnum.PLAYER_VISIBILITY) || Role.findRoleByPermission(online).getAlwaysVisible()) {
                        player.showPlayer(online);
                    } else {
                        player.hidePlayer(online);
                    }

                    if (onlineProfile.loadPreferencesContainer().getPreference(PreferenceEnum.PLAYER_VISIBILITY) || Role.findRoleByPermission(player).getAlwaysVisible()) {
                        online.showPlayer(player);
                    } else {
                        online.hidePlayer(player);
                    }

                    return;
                }

                player.hidePlayer(online);
                online.hidePlayer(player);
            }
        }
    }

    public Player getPlayer() {
        if (this.player == null) {
            this.player = Bukkit.getPlayerExact(this.name);
        }

        return this.player;
    }

    public void setPlayer(@NotNull Player player) {
        this.player = player;
        UUID_CACHE.put(this.name.toLowerCase(), player.getUniqueId());
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

    public PreferencesContainer loadPreferencesContainer() {
        return new PreferencesContainer(this);
    }

    public DeliveryContainer loadDeliveryContainer() {
        return new DeliveryContainer(this);
    }

    public BoosterContainer loadBoosterContainer() {
        return new BoosterContainer(this);
    }

    public AchievementsInformation loadAchievementsContainer() {
        return this.getCache().loadTableCache(ProfileCache.class).loadCollection(AchievementsInformation.class);
    }

    public CoinsGenericInformation loadCoinsContainer(Class<? extends Data> classData) {
        return this.getCache().loadTableCache(classData).loadCollectionGeneric(CoinsGenericInformation.class, "coins");
    }

    public SkyWarsStatsInformation loadSkyWarsStatsContainer() {
        return this.getCache().loadTableCache(SkyWarsCache.class).loadCollection(SkyWarsStatsInformation.class);
    }

    public TheBridgeStatsInformation loadTheBridgeStatsContainer() {
        return this.getCache().loadTableCache(TheBridgeCache.class).loadCollection(TheBridgeStatsInformation.class);
    }

    public MurderStatsInformation loadMurderStatsContainer() {
        return this.getCache().loadTableCache(MurderCache.class).loadCollection(MurderStatsInformation.class);
    }
}
