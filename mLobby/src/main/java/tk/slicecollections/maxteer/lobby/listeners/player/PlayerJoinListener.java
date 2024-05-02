package tk.slicecollections.maxteer.lobby.listeners.player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.lobby.Language;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.lobby.hook.LCoreHook;
import tk.slicecollections.maxteer.lobby.lobby.Lobby;
import tk.slicecollections.maxteer.lobby.tagger.TagUtils;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.hotbar.Hotbar;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    evt.setJoinMessage(null);

    Player player = evt.getPlayer();
    TagUtils.sendTeams(player);

    Profile profile = Profile.loadProfile(player.getName());
    LCoreHook.reloadScoreboard(profile);
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();

    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> TitleManager.joinLobby(profile), 10);

    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      TagUtils.setTag(evt.getPlayer());
      if (Role.findRoleByPermission(player).getBroadcast()) {
        String broadcast = Language.lobby$broadcast.replace("{player}", Role.getPrefixed(player.getName()));
        Profile.listProfiles().forEach(pf -> {
          if (!pf.playingGame()) {
            Player players = pf.getPlayer();
            if (players != null) {
              players.sendMessage(broadcast);
            }
          }
        });
      }
    }, 5);

    NMS.sendTitle(player, "", "", 0, 1, 0);
    if (Language.lobby$tab$enabled) {
      NMS.sendTabHeaderFooter(player, Language.lobby$tab$header, Language.lobby$tab$footer);
    }
  }
}
