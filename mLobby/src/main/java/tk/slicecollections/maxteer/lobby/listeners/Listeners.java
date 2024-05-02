package tk.slicecollections.maxteer.lobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.lobby.listeners.entity.EntityListener;
import tk.slicecollections.maxteer.lobby.listeners.player.AsyncPlayerChatListener;
import tk.slicecollections.maxteer.lobby.listeners.player.InventoryClickListener;
import tk.slicecollections.maxteer.lobby.listeners.player.PlayerDeathListener;
import tk.slicecollections.maxteer.lobby.listeners.player.PlayerInteractListener;
import tk.slicecollections.maxteer.lobby.listeners.player.PlayerJoinListener;
import tk.slicecollections.maxteer.lobby.listeners.player.PlayerQuitListener;
import tk.slicecollections.maxteer.lobby.listeners.player.PlayerRestListener;
import tk.slicecollections.maxteer.lobby.listeners.server.ServerListener;

public class Listeners {

  public static void setupListeners() {
    try {
      PluginManager pm = Bukkit.getPluginManager();

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new EntityListener(), Main.getInstance());

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new AsyncPlayerChatListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new InventoryClickListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerDeathListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerInteractListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerJoinListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerQuitListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerRestListener(), Main.getInstance());

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new ServerListener(), Main.getInstance());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
