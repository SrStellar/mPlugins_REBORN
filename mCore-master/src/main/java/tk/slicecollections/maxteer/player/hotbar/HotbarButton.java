package tk.slicecollections.maxteer.player.hotbar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotbarButton {

    private int slot;
    private HotbarAction action;
    private String icon;

}
