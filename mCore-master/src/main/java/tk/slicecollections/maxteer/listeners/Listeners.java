package tk.slicecollections.maxteer.listeners;

import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import org.spigotmc.WatchdogThread;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.Manager;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.hotbar.HotbarButton;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;
import tk.slicecollections.maxteer.player.fake.FakeManager;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.plugin.logger.MLogger;
import tk.slicecollections.maxteer.reflection.Accessors;
import tk.slicecollections.maxteer.reflection.acessors.FieldAccessor;
import tk.slicecollections.maxteer.titles.TitleController;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Maxter
 */
public class Listeners implements Listener {

  public static final MLogger LOGGER = ((MLogger) Core.getInstance().getLogger()).getModule("Listeners");
  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();

  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());

  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      try {
        Profile.createProfile(evt.getName());
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, "Nao foi possível carregar os dados do perfil \"" + evt.getName() + "\": ", ex);
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Profile profile = Profile.loadProfile(evt.getPlayer().getName());
    if (profile == null) {
      evt.disallow(PlayerLoginEvent.Result.KICK_OTHER, " \n§cAparentemente o servidor não conseguiu carregar seu Perfil.\n \n§cIsso ocorre normalmente quando o servidor ainda está despreparado para receber logins, aguarde um pouco e tente novamente.");
      return;
    }

    profile.setPlayer(evt.getPlayer());
  }

  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    Profile profile = Profile.loadProfile(evt.getPlayer().getName());
    if (profile != null) {
      try {
        profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle();
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      TitleManager.leaveServer(profile);
      if (profile.getGame() != null) {
        profile.getGame().leave(profile, profile.getGame());
      }
      if (!((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning() || RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null))) {
        profile.saveSync();
      } else {
        profile.save();
      }

      profile.destroy();
    }

    FakeManager.fakeNames.remove(evt.getPlayer().getName());
    FakeManager.fakeRoles.remove(evt.getPlayer().getName());
    FakeManager.fakeSkins.remove(evt.getPlayer().getName());
    DELAY_PLAYERS.remove(evt.getPlayer().getName());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
  }

  @SneakyThrows
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Profile profile = Profile.loadProfile(event.getPlayer().getName());
    if (profile != null) {
      ProfileInformation information = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class);
      if (information.getAsJsonObject().get("created").toString().isEmpty()) {
        information.updateValue("created", System.currentTimeMillis());
      }

      information.updateValue("lastLogin", System.currentTimeMillis());
      Achievement.listAchievements().forEach(achievement -> achievement.check(profile));
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();

    String format = String.format(evt.getFormat(), player.getName(), evt.getMessage());

    String current = Manager.getCurrent(player.getName());
    Role role = Role.findRoleByPermission(player);
    TextComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(format)) {
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
      component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
        TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current + "\n§fGrupo: " + role.getName() + "\n \n§eClique para enviar uma mensagem privada.")));
      component.addExtra(components);
    }

    evt.setCancelled(true);
    evt.getRecipients().forEach(players -> {
      if (players != null) {
        players.spigot().sendMessage(component);
      }
    });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    Profile profile = Profile.loadProfile(player.getName());

    if (profile != null) {
      String[] args = evt.getMessage().replace("/", "").split(" ");

      if (args.length > 0) {
        String command = args[0];
        if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby") && command.equals("lobby") && profile.loadPreferencesContainer().getPreference(PreferenceEnum.PROTECTION_LOBBY)) { //Verifica se a proteção de lobby está ativa
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }

          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
            player.sendMessage("§aVocê tem certeza? Utilize /lobby novamente para voltar ao lobby.");
        } else if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("tell") && args.length > 1 && command.equals("tell") && !args[1].equalsIgnoreCase(player.getName())) {
          profile = Profile.loadProfile(args[1]);
          if (profile != null && !profile.loadPreferencesContainer().getPreference(PreferenceEnum.PRIVATE_MESSAGES)) { //Verifica se a mensagem previada está desativada para todos
            evt.setCancelled(true);
            player.sendMessage("§cEste usuário desativou as mensagens privadas.");
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.loadProfile(player.getName());

    if (profile != null && profile.getHotbar() != null) {
      ItemStack item = player.getItemInHand();
      if (evt.getAction().name().contains("CLICK") && item != null && item.hasItemMeta()) {
        HotbarButton button = profile.getHotbar().compareButton(player, item);
        if (button != null) {
          evt.setCancelled(true);
          button.getAction().execute(profile);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked() instanceof ArmorStand) {
      if (evt.getPlayer().getGameMode() == GameMode.ADVENTURE) {
        evt.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.loadProfile(player.getName());

      if (profile != null && profile.getHotbar() != null) {
        ItemStack item = evt.getCurrentItem();
        if (item != null && item.getType() != Material.AIR) {
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(player.getInventory()) && item.hasItemMeta()) {
            HotbarButton button = profile.getHotbar().compareButton(player, item);
            if (button != null) {
              evt.setCancelled(true);
              button.getAction().execute(profile);
            }
          }
        }
      }
    }
  }

}
