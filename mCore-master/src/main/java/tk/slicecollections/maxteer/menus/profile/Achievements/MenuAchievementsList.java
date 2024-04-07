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
import tk.slicecollections.maxteer.achievements.types.MurderAchievement;
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.achievements.types.TheBridgeAchievement;
import tk.slicecollections.maxteer.libraries.menu.PlayerMenu;
import tk.slicecollections.maxteer.menus.MenuProfile;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.util.List;

public class MenuAchievementsList extends PlayerMenu {

    public MenuAchievementsList(Profile profile) {
        super(profile.getPlayer(), "Desafios", 4);
        List<Achievement> achievements = Achievement.listAllByClass(SkyWarsAchievement.class);
        long max = achievements.size();
        long completed = achievements.stream().filter(achievement -> achievement.hasCompleted(profile)).count();
        String color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
        this.setItem(11, BukkitUtils.deserializeItemStack("GRASS : 1 : nome>&aSky Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

        achievements = Achievement.listAllByClass(TheBridgeAchievement.class);
        max = achievements.size();
        completed = achievements.stream().filter(achievement -> achievement.hasCompleted(profile)).count();
        color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
        this.setItem(13,
                BukkitUtils.deserializeItemStack("STAINED_CLAY:11 : 1 : nome>&aThe Bridge : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

        achievements = Achievement.listAllByClass(MurderAchievement.class);
        max = achievements.size();
        completed = achievements.stream().filter(achievement -> achievement.hasCompleted(profile)).count();
        color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
        this.setItem(15, BukkitUtils.deserializeItemStack("BOW : 1 : nome>&aMurder : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

        this.setItem(31, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"));

        this.register(Core.getInstance());
        this.open();
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    @SneakyThrows
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getInventory())) {
            evt.setCancelled(true);

            if (evt.getWhoClicked().equals(this.player)) {
                Profile profile = Profile.loadProfile(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
                    ItemStack item = evt.getCurrentItem();

                    if (item != null && item.getType() != Material.AIR) {
                        switch (evt.getSlot()) {
                            case 11: {
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                new MenuAchievements(profile, SkyWarsAchievement.class);
                                break;
                            }

                            case 13: {
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                new MenuAchievements(profile, TheBridgeAchievement.class);
                                break;
                            }

                            case 15: {
                                new MenuAchievements(profile, MurderAchievement.class);
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                break;
                            }

                            case 31: {
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                new MenuProfile(profile);
                                break;
                            }
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
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }
}
