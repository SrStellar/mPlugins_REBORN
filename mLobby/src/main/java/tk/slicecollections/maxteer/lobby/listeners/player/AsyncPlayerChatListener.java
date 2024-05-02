package tk.slicecollections.maxteer.lobby.listeners.player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.lobby.Language;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

public class AsyncPlayerChatListener implements Listener {

  private static final Map<String, Long> flood = new HashMap<>();

  private static final DecimalFormat df = new DecimalFormat("###.#");

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    flood.remove(evt.getPlayer().getName());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void AsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    if (!player.hasPermission("mlobby.chat.delay")) {
      long start = flood.containsKey(player.getName()) ? flood.get(player.getName()) : 0;
      if (start > System.currentTimeMillis()) {
        double time = (start - System.currentTimeMillis()) / 1000.0;
        if (time > 0.1) {
          evt.setCancelled(true);
          String timeString = df.format(time).replace(",", ".");
          if (timeString.endsWith("0")) {
            timeString = timeString.substring(0, timeString.lastIndexOf("."));
          }

          player.sendMessage(Language.chat$delay.replace("{time}", timeString));
          return;
        }
      }

      flood.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3));
    }

    Role role = Role.findRoleByPermission(player);
    if (player.hasPermission("mlobby.chat.color")) {
      evt.setMessage(StringUtils.formatColors(evt.getMessage()));
    }

    evt.setFormat(
            Language.chat$format$lobby.replace("{player}", role.getPrefix() + "%s").replace("{color}", Role.getDefaultRole().equals(role) ? Language.chat$color$default : Language.chat$color$custom)
                    .replace("{message}", "%s"));

    evt.getRecipients().clear();
    for (Player players : Bukkit.getOnlinePlayers()) {
      Profile profiles = Profile.loadProfile(players.getName());
      if (profiles != null) {
        evt.getRecipients().add(players);
      }
    }
  }
}
