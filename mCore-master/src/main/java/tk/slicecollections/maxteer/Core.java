package tk.slicecollections.maxteer;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.cmd.Commands;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.deliveries.Delivery;
import tk.slicecollections.maxteer.hook.MCoreExpansion;
import tk.slicecollections.maxteer.hook.protocollib.FakeAdapter;
import tk.slicecollections.maxteer.hook.protocollib.HologramAdapter;
import tk.slicecollections.maxteer.hook.protocollib.NPCAdapter;
import tk.slicecollections.maxteer.libraries.MinecraftVersion;
import tk.slicecollections.maxteer.libraries.holograms.HologramLibrary;
import tk.slicecollections.maxteer.libraries.npclib.NPCLibrary;
import tk.slicecollections.maxteer.listeners.Listeners;
import tk.slicecollections.maxteer.listeners.PluginMessageListener;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.fake.FakeManager;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.plugin.MPlugin;
import tk.slicecollections.maxteer.plugin.config.MConfig;
import tk.slicecollections.maxteer.queue.Queue;
import tk.slicecollections.maxteer.queue.QueuePlayer;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Maxter
 */
@SuppressWarnings("unchecked")
public class Core extends MPlugin {

  @Getter
  private static Core instance;
  public static final List<String> warnings = new ArrayList<>();
  public static final List<String> minigames = Arrays.asList("Sky Wars", "The Bridge", "Murder");
  public static String minigame = "";

  @Override
  public void start() {
    instance = this;
  }

  @Override
  public void load() {
    saveDefaultConfig();
  }

  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("O plugin apenas funciona na versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }

    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();

    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }

    // Plugins que causaram incompatibilidades
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("blacklist.txt"), StandardCharsets.UTF_8))) {
      String plugin;
      while ((plugin = reader.readLine()) != null) {
        if (Bukkit.getPluginManager().getPlugin(plugin.split(" ")[0]) != null) {
          warnings.add(" - " + plugin);
        }
      }
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, "Cannot load blacklist.txt: ", ex);
    }

    if (!warnings.isEmpty()) {
      CommandSender sender = Bukkit.getConsoleSender();
      StringBuilder sb = new StringBuilder(" \n §6§lAVISO IMPORTANTE\n \n §7Aparentemente você utiliza plugins que conflitam com o mCore.\n §7Você não poderá iniciar o servidor com os seguintes plugins:");
      for (String warning : warnings) {
        sb.append("\n§f").append(warning);
      }
      sb.append("\n ");
      sender.sendMessage(sb.toString());
      System.exit(0);
      return;
    }

    // Remover /reload
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
      knownCommands.remove("rl");
      knownCommands.remove("reload");
      knownCommands.remove("bukkit:rl");
      knownCommands.remove("bukkit:reload");
    } catch (ReflectiveOperationException ex) {
      getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
    }

    setupRoles();
    Database.setupDatabase(DataTypes.MYSQL, this.getConfig().getString("database.mysql.nome"),
            this.getConfig().getString("database.mysql.usuario"),
            this.getConfig().getString("database.mysql.senha"),
            this.getConfig().getString("database.mysql.host") + ":" +this.getConfig().getString("database.mysql.porta"));

    new MCoreExpansion().register();

    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);

    FakeManager.setupFake();

    Delivery.setupDeliveries();
    TitleManager.setupTitles();
    Commands.setupCommands();
    Listeners.setupListeners();
    Achievement.setupAchievements();

    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());

    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "mCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "mCore", new PluginMessageListener());

    this.getLogger().info("O plugin foi ativado.");
  }

  @Override
  public void disable() {
    Profile.listProfiles().forEach(Profile::saveSync);
    Database.getInstance().destroyConnection();
    this.getLogger().info("O plugin foi desativado.");
  }

  private void setupRoles() {
    MConfig config = getConfig("roles");
    for (String key : config.getSection("roles").getKeys(false)) {
      String name = StringUtils.formatColors(config.getString("roles." + key + ".name", "&7Membro"));
      String prefix = StringUtils.formatColors(config.getString("roles." + key + ".prefix", "&7"));
      String permission = config.getString("roles." + key + ".permission", null);
      Boolean alwaysVisible = config.getBoolean("roles." + key + ".alwaysvisible", false);
      Boolean broadcast = config.getBoolean("roles." + key + ".broadcast", true);
      Boolean fly = config.getBoolean("roles." + key + ".fly", true);
      Role.listRoles().add(new Role((long) Role.listRoles().size(), name, prefix, permission, alwaysVisible, fly, broadcast));
    }

  }

  @Getter
  private static Location lobby;

  public static void setLobby(Location location) {
    lobby = location;
  }

  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }

    Player player = profile.getPlayer();
    if (player != null) {
      player.closeInventory();
      Queue queue = player.hasPermission("mcore.queue") ? Queue.VIP : Queue.MEMBER;
      QueuePlayer qp = queue.getQueuePlayer(player);
      if (qp != null) {
        if (qp.server.equalsIgnoreCase(name)) {
          qp.player.sendMessage("§cVocê já está na fila de conexão!");
        } else {
          qp.server = name;
        }
        return;
      }

      queue.queue(player, profile, name);
    }
  }
}
