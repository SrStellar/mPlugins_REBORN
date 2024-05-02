package tk.slicecollections.maxteer.lobby.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.lobby.cmd.ml.BuildCommand;
import tk.slicecollections.maxteer.player.Profile;

public class InventoryClickListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.loadProfile(player.getName());

      if (profile != null) {
        evt.setCancelled(!BuildCommand.isBuilder(player));
      }
    }
  }
}
