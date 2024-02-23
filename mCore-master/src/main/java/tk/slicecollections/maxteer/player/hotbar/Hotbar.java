package tk.slicecollections.maxteer.player.hotbar;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.utils.BukkitUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Hotbar {

    @NonNull
    private String id;
    private final List<HotbarButton> buttons = new ArrayList<>();;

    public void apply(Profile profile) {
        Player player = profile.getPlayer();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        this.buttons.stream().filter(button -> button.getSlot() >= 0 && button.getSlot() <= 8).forEach(button -> {
            ItemStack icon = BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(player, button.getIcon().replace("%perfil%", "")));
            player.getInventory().setItem(button.getSlot(), button.getIcon().contains("%perfil%") ? BukkitUtils.putProfileOnSkull(player, icon) : icon);
        });

        player.updateInventory();
    }

    public HotbarButton compareButton(Player player, ItemStack item) {
        return this.buttons.stream().filter(button -> button.getSlot() - 1 >= 0 && button.getSlot() - 1 <= 8 && item.equals(player.getInventory().getItem(button.getSlot() - 1))).findFirst()
                .orElse(null);
    }

    private static final List<Hotbar> HOTBARS = new ArrayList<>();

    public static void addHotbar(Hotbar hotbar) {
        HOTBARS.add(hotbar);
    }

    public static Hotbar getHotbarById(String id) {
        return HOTBARS.stream().filter(hb -> hb.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}
