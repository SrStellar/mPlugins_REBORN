package tk.slicecollections.maxteer.lobby.lobby;

import static tk.slicecollections.maxteer.lobby.lobby.Lobby.QUERY;

import org.bukkit.scheduler.BukkitRunnable;

public class LobbyEntryTask extends BukkitRunnable {

  @Override
  public void run() {
    QUERY.forEach(Lobby::fetch);
  }
}
