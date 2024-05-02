package tk.slicecollections.maxteer.lobby.hook;

import com.comphenix.protocol.ProtocolLibrary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tk.slicecollections.maxteer.lobby.Language;
import tk.slicecollections.maxteer.lobby.Main;
import tk.slicecollections.maxteer.lobby.hook.hotbar.LHotbarActionType;
import tk.slicecollections.maxteer.lobby.hook.protocollib.HologramAdapter;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.hotbar.Hotbar;
import tk.slicecollections.maxteer.player.hotbar.HotbarAction;
import tk.slicecollections.maxteer.player.hotbar.HotbarActionType;
import tk.slicecollections.maxteer.player.hotbar.HotbarButton;
import tk.slicecollections.maxteer.player.scoreboard.MScoreboard;
import tk.slicecollections.maxteer.player.scoreboard.scroller.ScoreboardScroller;
import tk.slicecollections.maxteer.plugin.config.MConfig;

public class LCoreHook {

  public static void setupHook() {
    setupHotbars();
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (profile.getScoreboard() != null) {
            profile.getScoreboard().scroll();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, Language.scoreboards$scroller$every_tick);

    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (!profile.playingGame() && profile.getScoreboard() != null) {
            profile.update();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
  }

  public static void reloadScoreboard(Profile profile) {
    Player player = profile.getPlayer();
    List<String> lines = new ArrayList<>(Language.scoreboards$lobby);
    Collections.reverse(lines);
    profile.setScoreboard(new MScoreboard() {
      @Override
      public void update() {
        this.updateHealth();
        for (int index = 0; index < lines.size(); index++) {
          String line = lines.get(index);
          line = PlaceholderAPI.setPlaceholders(player, line);

          this.add(index + 1, line);
        }
      }
    }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles)).to(profile.getPlayer()).build());
    profile.update();
    profile.getScoreboard().scroll();
  }

  private static void setupHotbars() {
    HotbarActionType.addActionType("lobby", new LHotbarActionType());

    MConfig config = Main.getInstance().getConfig("hotbar");
    for (String id : new String[] {"lobby"}) {
      Hotbar hotbar = new Hotbar(id);

      ConfigurationSection hb = config.getSection(id);
      for (String button : hb.getKeys(false)) {
        try {
          hotbar.getButtons().add(new HotbarButton(hb.getInt(button + ".slot"), new HotbarAction(hb.getString(button + ".execute")), hb.getString(button + ".icon")));
        } catch (Exception ex) {
          Main.getInstance().getLogger().log(Level.WARNING, "Falha ao carregar o botao \"" + button + "\" da hotbar \"" + id + "\": ", ex);
        }
      }

      Hotbar.addHotbar(hotbar);
    }
  }

}
