package tk.slicecollections.maxteer.lobby.lobby;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import org.bukkit.Bukkit;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.plugin.config.MConfig;
import tk.slicecollections.maxteer.servers.ServerItem;
import tk.slicecollections.maxteer.servers.ServerPing;

@Getter
public class Lobby {

  private final int slot;
  private final ServerPing serverPing;
  private final int maxPlayers;
  private final String icon;
  private final String serverName;

  public Lobby(int slot, String icon, int maxPlayers, String ip, String serverName) {
    this.slot = slot;
    this.icon = icon;
    this.serverPing = new ServerPing(
        new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1])));
    this.maxPlayers = maxPlayers;
    this.serverName = serverName;
  }

  public void fetch() {
    this.serverPing.fetch();
    ServerItem.SERVER_COUNT.put(this.serverName, this.serverPing.getOnline());
  }

  public int getPlayers() {
    return this.serverName.equals(Main.currentServerName) ? Bukkit.getOnlinePlayers().size()
        : ServerItem.getServerCount(this.serverName);
  }

  private static final List<Lobby> LOBBIES = new ArrayList<>();
  public static final List<Lobby> QUERY = new ArrayList<>();
  public static final MConfig CONFIG = Main.getInstance().getConfig("lobbies");

  public static void setupLobbies() {
    new LobbyEntryTask().runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 30);

    for (String key : CONFIG.getSection("items").getKeys(false)) {
      String servername = CONFIG.getString("items." + key + ".servername");

      LOBBIES.add(
          new Lobby(CONFIG.getInt("items." + key + ".slot"),
              CONFIG.getString("items." + key + ".icon"),
              CONFIG.getInt("items." + key + ".max-players"), servername.split(" ; ")[0],
              servername.split(" ; ")[1]));
    }

    for (Lobby lobby : LOBBIES) {
      if (!ServerItem.alreadyQuerying(lobby.getServerName())) {
        QUERY.add(lobby);
      }
    }
  }

  public static Collection<Lobby> listLobbies() {
    return LOBBIES;
  }
}
