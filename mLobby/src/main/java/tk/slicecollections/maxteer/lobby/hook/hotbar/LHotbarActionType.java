package tk.slicecollections.maxteer.lobby.hook.hotbar;

import tk.slicecollections.maxteer.lobby.menus.MenuLobbies;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.hotbar.HotbarActionType;

public class LHotbarActionType extends HotbarActionType {

  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    }
  }
}
