package tk.slicecollections.maxteer.lobby.listeners.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.lobby.cmd.ml.BuildCommand;
import tk.slicecollections.maxteer.player.Profile;

public class PlayerRestListener implements Listener {
  
  @EventHandler
  public void onPlayerConsume(PlayerItemConsumeEvent evt) {
    if (evt.getItem().getType() == Material.GOLDEN_APPLE) {
      evt.getPlayer().setHealth(20.0);
    }
  }
  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent evt) {
    Profile profile = Profile.loadProfile(evt.getPlayer().getName());
    if (profile != null) {
      evt.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent evt) {
    Profile profile = Profile.loadProfile(evt.getPlayer().getName());
    if (profile != null) {
      evt.setCancelled(profile.getGame() == null);
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent evt) {
    evt.setCancelled(!BuildCommand.isBuilder(evt.getPlayer()));
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent evt) {
    Profile profile = Profile.loadProfile(evt.getPlayer().getName());
    evt.setCancelled(!BuildCommand.isBuilder(evt.getPlayer()));
  }
}
