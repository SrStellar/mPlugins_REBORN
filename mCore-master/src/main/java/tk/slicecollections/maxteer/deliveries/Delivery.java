package tk.slicecollections.maxteer.deliveries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.plugin.config.MConfig;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;
import tk.slicecollections.maxteer.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class Delivery {

    private static final List<Delivery> DELIVERIES = new ArrayList<>();

    public static void setupDeliveries() {
        MConfig config = Core.getInstance().getConfig("deliveries");

        for (String key : config.getSection("deliveries").getKeys(false)) {
            try {
                int slot = config.getInt("deliveries." + key + ".slot");
                long days = TimeUnit.DAYS.toMillis(config.getInt("deliveries." + key + ".days"));
                String role = config.getString("deliveries." + key + ".role", null);
                String icon = config.getString("deliveries." + key + ".icon");
                String message = StringUtils.formatColors(config.getString("deliveries." + key + ".message"));
                List<DeliveryReward> rewards = new ArrayList<>();
                for (String reward : config.getStringList("deliveries." + key + ".rewards")) {
                    rewards.add(new DeliveryReward(reward));
                }

                DELIVERIES.add(new Delivery(DELIVERIES.size(), days, slot, role != null ? Role.findByName(role) : null, rewards, icon, message));
            } catch (Exception e) {
                Core.getInstance().getLogger().severe("Ocorreu um erro de configuração ao carregar a key: " + key);
            }
        }
    }

    public static Collection<Delivery> listDeliveries() {
        return DELIVERIES;
    }

    private final long id;
    private final long days;
    private final int slot;
    private final Role role;
    private final List<DeliveryReward> rewards;
    private final String icon;
    private final String message;

    public boolean hasPermission(Player player) {
        return this.role != null && !Role.loadAllRoles(player).contains(role);
    }

    public ItemStack getIcon(Profile profile) {
        Player player = profile.getPlayer();

        String desc = "";
        boolean permission = this.hasPermission(player);
        boolean alreadyClaimed = profile.loadDeliveryContainer().alreadyClaimed(this.id);
        if (permission) {
            desc = this.role != null ? "\n \n&7Exclusivo para grupo " + role.getName() + "&7 ou superior." : "";
        } else if (alreadyClaimed) {
            desc = "\n \n&7Você poderá coletar novamente em &f" + TimeUtils.getTimeUntil(profile.loadDeliveryContainer().getClaimTime(this.id)) + "&7.";
        }

        ItemStack item = BukkitUtils.deserializeItemStack(this.icon.replace("{color}", !permission && !alreadyClaimed ? "&a" : "&c") + desc);
        if (!permission && alreadyClaimed) {
            switch (item.getType()) {
                case STORAGE_MINECART: {
                    item.setType(Material.MINECART);
                    item.setDurability((short) 0);
                    break;
                }

                case POTION: {
                    item.setType(Material.GLASS_BOTTLE);
                    item.setDurability((short) 0);
                    break;
                }
            }
        }
        return item;
    }

}
