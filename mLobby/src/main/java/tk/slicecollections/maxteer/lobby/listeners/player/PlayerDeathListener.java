package tk.slicecollections.maxteer.lobby.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.player.Profile;

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent evt) {
    Player player = evt.getEntity();
    evt.setDeathMessage(null);
    
    Profile profile = Profile.loadProfile(player.getName());
    if (profile != null) {
      evt.setDroppedExp(0);
      evt.getDrops().clear();
      player.setHealth(20.0);
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), profile::refresh, 3);
    }
  }
}
