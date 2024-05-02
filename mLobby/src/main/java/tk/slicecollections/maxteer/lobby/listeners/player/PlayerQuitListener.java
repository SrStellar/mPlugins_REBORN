package tk.slicecollections.maxteer.lobby.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.slicecollections.maxteer.lobby.cmd.ml.BuildCommand;
import tk.slicecollections.maxteer.lobby.tagger.TagUtils;

public class PlayerQuitListener implements Listener {

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    evt.setQuitMessage(null);
    BuildCommand.remove(evt.getPlayer());
    TagUtils.reset(evt.getPlayer().getName());
  }
}
