package tk.slicecollections.maxteer.player.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oJVzinn
 */
@AllArgsConstructor
@Getter
public class Role {

  private static final List<Role> ROLES = new ArrayList<>();

  public static List<Role> listRoles() {
    return ROLES;
  }

  public static String getPrefixed(String player) {
    return findRoleByPermission(Bukkit.getPlayer(player)).getRoleColor();
  }
  public static Role getDefaultRole() {
    return ROLES.stream().filter(role -> role.getPermission() == null).findFirst().orElse(ROLES.get(ROLES.size() - 1));
  }

  public static Role findRoleByPermission(Player player) {
    return ROLES.stream().filter(role -> role.getPermission() != null && player.hasPermission(role.getPermission())).findFirst().orElse(getDefaultRole());
  }

  public static Role findByName(String roleName) {
    return ROLES.stream().filter(role -> StringUtils.stripColors(role.getName()).equalsIgnoreCase(roleName)).findFirst().orElse(getDefaultRole());
  }

  public static Role findByPermission(String permission) {
    return ROLES.stream().filter(role -> role.getPermission().equals(permission)).findFirst().orElse(null);
  }

  public static List<Role> loadAllRoles(Player player) {
    return ROLES.stream().filter(role -> role.getPermission() == null || player.hasPermission(role.getPermission())).collect(Collectors.toList());
  }

  private Long id;
  @NonNull
  private String name;
  @NonNull
  private String prefix;
  private String permission;
  private Boolean alwaysVisible;
  private Boolean canFly;
  private Boolean broadcast;

  public String getRoleColor() {
    String nameStripper = StringUtils.stripColors(this.name);
    return this.name.split(nameStripper)[0];
  }

}
