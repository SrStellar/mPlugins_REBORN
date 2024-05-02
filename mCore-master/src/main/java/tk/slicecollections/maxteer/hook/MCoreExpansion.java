package tk.slicecollections.maxteer.hook;

import lombok.SneakyThrows;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.cash.CashManager;
import tk.slicecollections.maxteer.database.cache.collections.MurderStatsInformation;
import tk.slicecollections.maxteer.database.cache.collections.SkyWarsStatsInformation;
import tk.slicecollections.maxteer.database.cache.collections.TheBridgeStatsInformation;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.database.cache.types.TheBridgeCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;
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
  @SneakyThrows
  public String onPlaceholderRequest(Player player, String params) {
    Profile profile = null;
    if (player == null || (profile = Profile.loadProfile(player.getName())) == null) {
      return "";
    }

    switch (params) {
      case "role": {
        return Role.findRoleByPermission(player).getName();
      }

      case "cash": {
        return StringUtils.formatNumber(new CashManager(profile).getCash());
      }

      case "status_jogadores": {
        return profile.loadPreferencesContainer().getStateName(PreferenceEnum.PLAYER_VISIBILITY);
      }

      case "status_jogadores_nome": {
        if (profile.loadPreferencesContainer().getPreference(PreferenceEnum.PLAYER_VISIBILITY)) {
          return "§aON";
        }

        return "§cOFF";
      }

      case "status_jogadores_inksack": {
        return profile.loadPreferencesContainer().getInkColor(PreferenceEnum.PLAYER_VISIBILITY);
      }

      default: {
        switch (params.split("_")[0]) {
          case "SkyWars": {
            String value = params.replace("SkyWars_", "");
            SkyWarsStatsInformation information = profile.loadSkyWarsStatsContainer();
            if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
              return StringUtils.formatNumber(information.getInformation("1v1" + value, Long.class) + information.getInformation("2v2" + value, Long.class));
            } else if (value.contains("kills") || value.contains("deaths") || value.contains("assists") || value.contains("games") || value.contains("wins")) {
              return StringUtils.formatNumber(information.getInformation(value, Long.class));
            } else if (value.equals("coins")) {
              return StringUtils.formatNumber(profile.loadCoinsContainer(SkyWarsCache.class).getCoins());
            }
          }

          case "TheBridge": {
            String value = params.replace("TheBridge_", "");
            TheBridgeStatsInformation information = profile.loadTheBridgeStatsContainer();
            if (value.equals("kills") || value.equals("deaths") || value.equals("games") || value.equals("points") || value.equals("wins")) {
              return StringUtils.formatNumber(information.getInformation("1v1" + value, Long.class) + information.getInformation("2v2" + value, Long.class));
            } else if (value.contains("kills") || value.contains("deaths") || value.contains("games") || value.contains("points") || value.contains("wins")) {
              return StringUtils.formatNumber(information.getInformation(value, Long.class));
            } else if (value.equals("coins")) {
              return StringUtils.formatNumber(profile.loadCoinsContainer(TheBridgeCache.class).getCoins());
            }
          }

          case "Murder": {
            String value = params.replace("Murder_", "");
            MurderStatsInformation information = profile.loadMurderStatsContainer();
            String data = null;
            if (value.split("_").length > 1) {
              data = value.split("_")[1];
            }

            switch (value.split("_")[0]) {
              case "classic": {
                if (data.equals("kills") || data.equals("bowkills") || data.equals("knifekills") || data.equals("thrownknifekills") || data.equals("wins") || data.equals("detectivewins") || data.equals("killerwins")) {
                  return StringUtils.formatNumber(information.getInformation("cl" + data, Long.class));
                } else if (data.equals("quickestdetective") || data.equals("quickestkiller")) {
                  return MURDER_FORMAT.format(information.getInformation("cl" + data, Long.class) * 1000);
                }
              }

              case "assassins": {
                if (data.equals("kills") || data.equals("thrownknifekills") || data.equals("wins")) {
                  return StringUtils.formatNumber(information.getInformation("as" + data, Long.class));
                }
              }
            }

            if (value.equals("coins")) {
              return StringUtils.formatNumber(profile.loadCoinsContainer(TheBridgeCache.class).getCoins());
            }
          }
        }
      }
    }

    return "";
  }
}
