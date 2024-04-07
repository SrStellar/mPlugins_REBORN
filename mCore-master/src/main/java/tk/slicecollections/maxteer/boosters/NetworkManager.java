package tk.slicecollections.maxteer.boosters;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.cache.collections.BoosterNetworkInformation;
import tk.slicecollections.maxteer.database.cache.types.BoosterCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static final Map<String, BoosterCache> BOOSTER_DATA = new HashMap<>();

    public static void setupNetworkManager() {
        for (String minigame : Core.minigames) {
            BOOSTER_DATA.put(minigame, new BoosterCache(minigame));
        }

        Core.getInstance().getLogger().info("Todos os boosters foram carregados com sucesso!");
    }

    @SneakyThrows
    public static BoosterNetwork loadBoosterNetwork(String minigame) {
        BoosterCache cache = BOOSTER_DATA.get(minigame);
        BoosterNetworkInformation information = cache.loadCollection(BoosterNetworkInformation.class);
        BoosterNetwork boosterNetwork = new BoosterNetwork(information.getInformation("time"),
                information.getInformation("multiply"),
                information.getInformation("owner"),
                minigame);

        if (!boosterNetwork.hasBoosterActivated()) {
            boosterNetwork.setTime(0L);
            boosterNetwork.setOwner("");
            boosterNetwork.setMultiply(1.0);
        }

        return boosterNetwork;
    }

    @SneakyThrows
    public static void activateBooster(String minigame, Long time, Double multiply, String owner) {
        BoosterCache cache = BOOSTER_DATA.get(minigame);
        BoosterNetworkInformation information = cache.loadCollection(BoosterNetworkInformation.class);
        if (loadBoosterNetwork(minigame).hasBoosterActivated()) {
            return;
        }

        information.updateValue("time", System.currentTimeMillis() + time);
        information.updateValue("multiply", multiply);
        information.updateValue("owner", owner);
        Profile.listProfiles().forEach(pf -> pf.getPlayer().sendMessage(" \n " + owner + " §7ativou um §6Multiplicador de Coins Geral§7.\n §bTODOS §7os jogadores recebem um bônus de "
                + multiply + "x §7nas partidas de §6" + Core.minigame + "§7.\n "));
    }

    public static boolean hasBoosterActivated(String minigame) {
        return loadBoosterNetwork(minigame).hasBoosterActivated();
    }

    public static BoosterCache loadCache(String minigame) {
        return BOOSTER_DATA.get(minigame);
    }

    public static Map<String, BoosterCache> listAllBoosters() {
        return BOOSTER_DATA;
    }

    public static void destroyNetworkBoosters() {
        BOOSTER_DATA.values().forEach(boosterCache -> boosterCache.saveValueCollections(false));
    }

}
