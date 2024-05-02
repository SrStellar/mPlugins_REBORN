package tk.slicecollections.maxteer.lobby.lobby;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.plugin.config.MConfig;
import tk.slicecollections.maxteer.plugin.logger.MLogger;
import tk.slicecollections.maxteer.servers.ServerItem;
import tk.slicecollections.maxteer.utils.BukkitUtils;

import java.util.ArrayList;
import java.util.List;

public class ServerEntry {

  @Getter
  private String key;
  private final List<String> holograms;
  @Getter
  private ItemStack hand;
  @Getter
  private String skinValue;
  @Getter
  private String skinSignature;

  public ServerEntry(String key, List<String> holograms, ItemStack hand, String skinValue, String skinSignature) {
    this.key = key;
    this.holograms = holograms;
    this.hand = hand;
    this.skinValue = skinValue;
    this.skinSignature = skinSignature;
  }

  public ServerItem getServerItem() {
    return ServerItem.getServerItem(this.key);
  }

  public List<String> listHologramLines() {
    return this.holograms;
  }

  public static final MLogger LOGGER = ((MLogger) Main.getInstance().getLogger()).getModule("ENTRIES");
  private static final List<ServerEntry> ENTRIES = new ArrayList<>();

  public static void setupEntries() {
    MConfig config = Main.getInstance().getConfig("entries");
    for (String key : config.getKeys(false)) {
      if (!config.contains(key + ".hand")) {
        config.set(key + ".hand", "AIR : 1");
      }
      ServerEntry se = new ServerEntry(config.getString(key + ".key"), config.getStringList(key + ".holograms"), BukkitUtils.deserializeItemStack(config.getString(key + ".hand")),
        config.getString(key + ".skin.value"), config.getString(key + ".skin.signature"));
      if (se.getServerItem() == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> LOGGER.warning("A entry " + key + " (entries.yml) possui uma key invalida."));
        continue;
      }

      ENTRIES.add(se);
    }
  }

  public static ServerEntry getByKey(String key) {
    return ENTRIES.stream().filter(entry -> entry.getKey().equals(key)).findFirst().orElse(null);
  }
}
