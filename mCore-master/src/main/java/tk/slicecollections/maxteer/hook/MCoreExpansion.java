package tk.slicecollections.maxteer.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.enums.PlayerVisibility;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.text.SimpleDateFormat;

/**
 * @author oJVzinn
 */
@SuppressWarnings("all")
public class MCoreExpansion extends PlaceholderExpansion {

  private static final SimpleDateFormat MURDER_FORMAT = new SimpleDateFormat("mm:ss");

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public String getAuthor() {
    return "oJVzinn";
  }

  @Override
  public String getIdentifier() {
    return "mCore";
  }

  @Override
  public String getVersion() {
    return Core.getInstance().getDescription().getVersion();
  }

  @Override
  public String onPlaceholderRequest(Player player, String params) {
    Profile profile = null;
    if (player == null || (profile = Profile.loadProfile(player.getName())) == null) {
      return "";
    }

    return null;
  }
}
