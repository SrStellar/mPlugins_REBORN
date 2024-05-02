package tk.slicecollections.maxteer.lobby.lobby;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import tk.slicecollections.maxteer.libraries.holograms.HologramLibrary;
import tk.slicecollections.maxteer.libraries.holograms.api.Hologram;
import tk.slicecollections.maxteer.libraries.npclib.NPCLibrary;
import tk.slicecollections.maxteer.libraries.npclib.api.npc.NPC;
import tk.slicecollections.maxteer.lobby.Language;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.lobby.lobby.trait.NPCSkinTrait;
import tk.slicecollections.maxteer.plugin.config.MConfig;
import tk.slicecollections.maxteer.utils.BukkitUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class DeliveryNPC {

  private String id;
  private Location location;

  private NPC npc;
  private Hologram hologram;

  public DeliveryNPC(Location location, String id) {
    this.location = location;
    this.id = id;
    if (!this.location.getChunk().isLoaded()) {
      this.location.getChunk().load(true);
    }

    this.spawn();
  }

  public void spawn() {
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }

    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }

    this.hologram = HologramLibrary.createHologram(this.location.clone().add(0, 0.5, 0));
    for (int index = Language.lobby$npc$deliveries$hologram.size(); index > 0; index--) {
      this.hologram.withLine(Language.lobby$npc$deliveries$hologram.get(index - 1));
    }

    this.npc = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] ");
    this.npc.data().set("delivery-npc",  true);
    this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);
    this.npc.addTrait(new NPCSkinTrait(this.npc, Language.lobby$npc$deliveries$skin$value, Language.lobby$npc$deliveries$skin$signature));
    this.npc.spawn(this.location);
  }

  public void update() {
    int size = Language.lobby$npc$deliveries$hologram.size();
    for (int index = size; index > 0; index--) {
      this.hologram.updateLine(size - (index - 1), Language.lobby$npc$deliveries$hologram.get(index - 1));
    }
  }

  public void destroy() {
    this.id = null;
    this.location = null;

    this.npc.destroy();
    this.npc = null;
    HologramLibrary.removeHologram(this.hologram);
    this.hologram = null;
  }

  private static final MConfig CONFIG = Main.getInstance().getConfig("npcs");
  private static final List<DeliveryNPC> NPCS = new ArrayList<>();

  public static void setupNPCs() {
    if (!CONFIG.contains("delivery")) {
      CONFIG.set("delivery", new ArrayList<>());
    }

    for (String serialized : CONFIG.getStringList("delivery")) {
      if (serialized.split("; ").length > 6) {
        String id = serialized.split("; ")[6];

        NPCS.add(new DeliveryNPC(BukkitUtils.deserializeLocation(serialized), id));
      }
    }

    Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> NPCS.forEach(DeliveryNPC::update), 20, 20);
  }

  public static void add(String id, Location location) {
    NPCS.add(new DeliveryNPC(location, id));
    List<String> list = CONFIG.getStringList("delivery");
    list.add(BukkitUtils.serializeLocation(location) + "; " + id);
    CONFIG.set("delivery", list);
  }

  public static void remove(DeliveryNPC npc) {
    NPCS.remove(npc);
    List<String> list = CONFIG.getStringList("delivery");
    list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId());
    CONFIG.set("delivery", list);

    npc.destroy();
  }

  public static DeliveryNPC getById(String id) {
    return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
  }

  public static Collection<DeliveryNPC> listNPCs() {
    return NPCS;
  }
}
