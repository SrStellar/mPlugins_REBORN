package tk.slicecollections.maxteer.queue;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.player.Profile;

@AllArgsConstructor
public class QueuePlayer {

  public Player player;
  public Profile profile;
  public String server;

  public void destroy() {
    this.player = null;
    this.profile = null;
    this.server = null;
  }
}
