package tk.slicecollections.maxteer.menus.profile.booster;

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
import tk.slicecollections.maxteer.boosters.Booster;
import tk.slicecollections.maxteer.boosters.BoosterType;
import tk.slicecollections.maxteer.boosters.NetworkManager;
import tk.slicecollections.maxteer.libraries.menu.PagedPlayerMenu;
import tk.slicecollections.maxteer.menus.profile.Achievements.MenuAchievementsList;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuBoosters extends PagedPlayerMenu {

    private final Map<ItemStack, Booster> itemBooster = new HashMap<>();

    @SneakyThrows
    public MenuBoosters(Profile profile, BoosterType type) {
        super(profile.getPlayer(), "Multiplicadores " + (type.equals(BoosterType.PRIVATE) ? "Pessoais" : "Gerais"), 6);
        this.previousPage = 36;
        this.nextPage = 44;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"), 49);

        List<ItemStack> items = new ArrayList<>();
        for (Booster booster : profile.loadBoosterContainer().listAllBoosters(type)) {
            ItemStack icon = BukkitUtils.deserializeItemStack(
                    "POTION" + (type == BoosterType.NETWORK ? ":8232" : "") + " : 1 : esconder>tudo : nome>&aMultiplicador " + (type == BoosterType.NETWORK ?
                            "Geral" :
                            "Pessoal") + " : desc>&fMultiplicador: &6" + booster.getMultiply() + "x\n&fDuração: &7" +
                            booster.getTimeFormatted() +
                            "\n \n&eClique para ativar o multiplicador!");

            itemBooster.put(icon, booster);
            items.add(icon);
        }

        if (items.isEmpty()) {
            this.removeSlotsWith(BukkitUtils.deserializeItemStack("WEB : 1 : nome>&cVazio"), 22);
        }

        this.setItems(items);
        this.register(Core.getInstance());
        this.open();
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
        itemBooster.clear();
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
                        if (evt.getSlot() == 49) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuBoostersList(profile);
                        } else if (evt.getSlot() == this.previousPage) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            this.openPrevious();
                        } else if (evt.getSlot() == this.nextPage) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            this.openNext();
                        } else {
                            Booster booster = itemBooster.get(item);
                            if (booster != null) {
                                if (booster.getBoosterType() == BoosterType.NETWORK) {
                                    if (!Core.minigames.contains(Core.minigame)) {
                                        this.player.sendMessage("§cVocê precisa estar em um servidor de Minigame para ativar esse Multiplicador.");
                                        return;
                                    }

                                    if (NetworkManager.hasBoosterActivated(Core.minigame)) {
                                        EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                                        this.player.sendMessage("§cJá existe um Multiplicador Geral para o " + Core.minigame + " ativo.");
                                        this.player.closeInventory();
                                        return;
                                    }

                                    EnumSound.LEVEL_UP.play(this.player, 0.5F, 1.0F);
                                    NetworkManager.activateBooster(Core.minigame, booster.getTimeMinutes(), booster.getMultiply(), Role.findRoleByPermission(player).getRoleColor() + player.getName());
                                    this.player.closeInventory();
                                } else {
                                    if (profile.loadBoosterContainer().hasBoosterActivated()) {
                                        EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                                        this.player.sendMessage("§cVocê já possui um Multiplicador Pessoal ativo.");
                                        this.player.closeInventory();
                                        return;
                                    }

                                    this.player.sendMessage("§aVocê ativou um §6Multiplicador de Coins " + booster.getMultiply() + "x §8(" + booster.getTimeFormatted() + ")§a.");
                                    profile.loadBoosterContainer().activateBooster(booster);
                                    new MenuBoostersList(profile);
                                }
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
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
            this.cancel();
        }
    }
}
