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
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.boosters.BoosterNetwork;
import tk.slicecollections.maxteer.boosters.BoosterType;
import tk.slicecollections.maxteer.boosters.NetworkManager;
import tk.slicecollections.maxteer.libraries.menu.PlayerMenu;
import tk.slicecollections.maxteer.menus.MenuProfile;
import tk.slicecollections.maxteer.menus.profile.Achievements.MenuAchievements;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.boosters.BoosterContainer;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.util.List;

public class MenuBoostersList extends PlayerMenu {

    public MenuBoostersList(Profile profile) {
        super(profile.getPlayer(), "Multiplicadores", 4);
        this.setItem(12, BukkitUtils.deserializeItemStack(
                "POTION : 1 : nome>&aMultiplicadores Pessoais : desc>&7Concede um &6Multiplicador de Coins &7apenas\n&7para &bVOCÊ &7em todos os minigames do servidor\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!"));
        this.setItem(14, BukkitUtils.deserializeItemStack(
                "POTION:8232 : 1 : esconder>tudo : nome>&aMultiplicadores Gerais : desc>&7Concede um &6Multiplicador de Coins &7para\n&bTODOS &7os jogadores em apenas um minigame\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!"));

        StringBuilder bGeral = new StringBuilder();
        StringBuilder resultBooster = new StringBuilder();
        BoosterContainer container = profile.loadBoosterContainer();
        for (String minigame : Core.minigames) {
            BoosterNetwork boosterNetwork = NetworkManager.loadBoosterNetwork(minigame);
            bGeral.append(" §8• §b").append(minigame).append("§8: ")
                    .append(boosterNetwork.hasBoosterActivated() ? "§6" + boosterNetwork.getMultiply() + "x " : "§cDesativado")
                    .append(boosterNetwork.hasBoosterActivated() ? "§7por " + boosterNetwork.getOwner() : "")
                    .append(boosterNetwork.hasBoosterActivated() ? " §8(" + boosterNetwork.getRemainingTime() + ")\n" : "\n");
        }

        if (container.hasBoosterActivated()) {
            resultBooster.append(" §8• §6Multiplicador ")
                    .append(container.getBoosterActivated().get("multiply"))
                    .append("x §8(")
                    .append(container.getBoosterTimeRemaining())
                    .append(")\n \n§fCálculo:\n §7Com o multiplicador ativo ao receber §650 Coins §7o\n §7total recebido será equivalente a §6")
                    .append(50 * ((Double) container.getBoosterActivated().get("multiply")))
                    .append(" Coins§7.");
        } else {
            resultBooster.append("§cVocê possui nenhum multiplicador ativo.");
        }

        this.setItem(31, BukkitUtils.deserializeItemStack(
                "PAPER : 1 : nome>&aMultiplicadores de Crédito : desc>&7Os Multiplicadores são acumulativos. Quanto mais\n&7multiplicadores ativos, maior será o bônus recebido.\n \n&fMultiplicadores Gerais:\n"
                        + bGeral + " \n§fMultiplicador Pessoal ativo:\n" + resultBooster));

        this.setItem(30, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"));

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
                            case 12: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                new MenuBoosters(profile, BoosterType.PRIVATE);
                                break;
                            }

                            case 14: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                                new MenuBoosters(profile, BoosterType.NETWORK);
                                break;
                            }

                            case 30: {
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                new MenuProfile(profile);
                                break;
                            }

                            case 31: {
                                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
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
