package tk.slicecollections.maxteer.menus.profile;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.libraries.menu.PlayerMenu;
import tk.slicecollections.maxteer.menus.MenuProfile;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;
import tk.slicecollections.maxteer.player.preferences.PreferencesContainer;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MenuPreferences extends PlayerMenu {

    @SneakyThrows
    public MenuPreferences(Profile profile) {
        super(profile.getPlayer(), "Preferencias", 5);
        PreferencesContainer container = profile.loadPreferencesContainer();

        this.setItem(11, BukkitUtils.deserializeItemStack("347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n&7jogadores no lobby."));
        this.setItem(20, BukkitUtils.deserializeItemStack(
                "INK_SACK:" + container.getInkColor(PreferenceEnum.PLAYER_VISIBILITY) + " : 1 : nome>" + container.getStateName(PreferenceEnum.PLAYER_VISIBILITY) + " : desc>&fEstado: &7" + container.getStateName(PreferenceEnum.PLAYER_VISIBILITY, true) + "\n \n&eClique para modificar!"));

        this.setItem(12, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>&aMensagens privadas : desc>&7Ative ou desative as mensagens\n&7enviadas através do tell."));
        this.setItem(21, BukkitUtils.deserializeItemStack(
                "INK_SACK:" + container.getInkColor(PreferenceEnum.PRIVATE_MESSAGES) + " : 1 : nome>" + container.getStateName(PreferenceEnum.PRIVATE_MESSAGES) + " : desc>&fEstado: &7" + container.getStateName(PreferenceEnum.PRIVATE_MESSAGES, true) + "\n \n&eClique para modificar!"));

        this.setItem(14, BukkitUtils.deserializeItemStack("REDSTONE : 1 : nome>&aViolência : desc>&7Ative ou desative as partículas\n&7de sangue no PvP."));
        this.setItem(23, BukkitUtils.deserializeItemStack(
                "INK_SACK:" + container.getInkColor(PreferenceEnum.BLOOD_AND_GORE) + " : 1 : nome>" + container.getStateName(PreferenceEnum.BLOOD_AND_GORE) + " : desc>&fEstado: &7" + container.getStateName(PreferenceEnum.BLOOD_AND_GORE, true) + "\n \n&eClique para modificar!"));

        this.setItem(15, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido de\n&7confirmação ao utilizar /lobby."));
        this.setItem(24, BukkitUtils.deserializeItemStack(
                "INK_SACK:" + container.getInkColor(PreferenceEnum.PROTECTION_LOBBY) + " : 1 : nome>" + container.getStateName(PreferenceEnum.PROTECTION_LOBBY) + " : desc>&fEstado: &7" + container.getStateName(PreferenceEnum.PROTECTION_LOBBY, true) + "\n \n&eClique para modificar!"));


        this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"));


        this.register(Core.getInstance());
        this.open();
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
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
                        PreferencesContainer container = profile.loadPreferencesContainer();
                        switch (evt.getSlot()) {
                            case 20: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                container.changePreference(PreferenceEnum.PLAYER_VISIBILITY);
                                if (!profile.playingGame()) {
                                    profile.refreshPlayers();
                                }
                                new MenuPreferences(profile);
                                break;
                            }

                            case 21: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                container.changePreference(PreferenceEnum.PRIVATE_MESSAGES);
                                new MenuPreferences(profile);
                                break;
                            }

                            case 23: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                container.changePreference(PreferenceEnum.BLOOD_AND_GORE);
                                new MenuPreferences(profile);
                                break;
                            }

                            case 24: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                container.changePreference(PreferenceEnum.PROTECTION_LOBBY);
                                new MenuPreferences(profile);
                                break;
                            }

                            case 40: {
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                new MenuProfile(profile);
                                break;
                            }

                            default: {
                                if (evt.getSlot() == 10 || evt.getSlot() == 11 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 15 || evt.getSlot() == 16) {
                                    EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                }
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
