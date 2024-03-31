package tk.slicecollections.maxteer.menus.profile.Achievements;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.libraries.menu.PagedPlayerMenu;
import tk.slicecollections.maxteer.menus.profile.Achievements.MenuAchievementsList;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.util.ArrayList;
import java.util.List;

public class MenuAchievements extends PagedPlayerMenu {

    @SneakyThrows
    public MenuAchievements(Profile profile, Class<? extends Achievement> clazz) {
        super(profile.getPlayer(), "Títulos", 5);
        this.previousPage = 36;
        this.nextPage = 44;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);

        this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"), 40);

        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> sub = new ArrayList<>();
        for (Achievement achievements : Achievement.listByClass(clazz)) {
            ItemStack item = achievements.getIcon(profile);
            if (achievements.hasCompleted(profile)) {
                items.add(item);
                continue;
            }

            sub.add(item);
        }

        items.addAll(sub);
        this.setItems(items);
        sub.clear();
        items.clear();


        this.register(Core.getInstance());
        this.open();
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    @SneakyThrows
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getCurrentInventory())) {
            evt.setCancelled(true);

            if (evt.getWhoClicked().equals(this.player)) {
                Profile profile = Profile.loadProfile(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
                    ItemStack item = evt.getCurrentItem();

                    if (item != null && item.getType() != Material.AIR) {
                        if (evt.getSlot() == 40) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuAchievementsList(profile);
                        } else if (evt.getSlot() == this.previousPage) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            this.openPrevious();
                        } else if (evt.getSlot() == this.nextPage) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            this.openNext();
                        } else {
                            EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
            this.cancel();
        }
    }
}
