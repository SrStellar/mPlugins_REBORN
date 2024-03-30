package tk.slicecollections.maxteer.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tk.slicecollections.maxteer.Manager;

@Getter
@AllArgsConstructor
public class PartyPlayer {
  private final String name;

  @Setter
  private PartyRole role;

  public void sendMessage(String message) {
    Object player = Manager.getPlayer(name);
    if (player != null) {
      Manager.sendMessage(player, message);
    }
  }

  public boolean isOnline() {
    return Manager.getPlayer(this.name) != null;
  }
}
