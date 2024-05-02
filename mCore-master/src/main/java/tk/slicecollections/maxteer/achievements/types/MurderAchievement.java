package tk.slicecollections.maxteer.achievements.types;

import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.achievements.AchievementReward;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.MurderStatsInformation;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.Objects;

public class MurderAchievement extends Achievement {

    public static void setupAchievements() {
        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Investigador",
                MurderStatsInformation.class,
                "cldetectivewins",
                100L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Trapper",
                MurderStatsInformation.class,
                "clkillerwins",
                100L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Perito Criminal",
                MurderStatsInformation.class,
                "cldetectivewins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &61.500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1500L,
                null));

        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Traidor",
                MurderStatsInformation.class,
                "clkillerwins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &61.500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1500L,
                null));

        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Detetive",
                MurderStatsInformation.class,
                "cldetectivewins",
                400L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &fTítulo: &6Sherlock Holmes\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(6L)));

        Achievement.registerNewAchievement(new MurderAchievement((long) Achievement.listAchievements().size(),
                "Serial Killer",
                MurderStatsInformation.class,
                "clkillerwins",
                400L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &fTítulo: &4Jeff the Killer\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(7L)));
    }

    public MurderAchievement(Long id, String name, Class<? extends DataCollection> classCollection, String key, Long reach, String iconBase, AchievementReward reward, Long amountReward, Title titleReward, String... additionalKeys) {
        super(id, name, classCollection, key, reach, iconBase, reward, amountReward, titleReward, additionalKeys);
    }

    @SneakyThrows
    @Override
    public void check(Profile profile) {
        if (hasCompleted(profile)) {
            return;
        }

        if (Objects.equals(getCurrent(profile), this.reach)) {
            this.reward.setupReward(profile, Double.valueOf(this.amountReward), titleReward, this);
            profile.getPlayer().playSound(profile.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            profile.loadAchievementsContainer().addNewAchievement(this);
        }
    }

    @Override
    public ItemStack getIcon(Profile profile) {
        Long current = getCurrent(profile);
        return BukkitUtils.deserializeItemStack(this.iconBase
                .replace("%material%", Objects.equals(current, this.reach) ? "ENCHANTED_BOOK" : "BOOK")
                .replace("%name%", (Objects.equals(current, this.reach) ? "&a" : "&c") + this.name)
                .replace("%current%", StringUtils.formatNumber(current))
                .replace("%reach%", StringUtils.formatNumber(this.reach))
                .replace("%progress%", (current.equals(this.reach) ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach)
        );
    }

    public Long getCurrent(Profile profile) {
        long current;
        try {
            current = profile.loadTheBridgeStatsContainer().getInformation(this.key);
            for (String additionalKey : additionalKeys) {
                current += profile.loadTheBridgeStatsContainer().getInformation(additionalKey, Long.class);
            }
        } catch (Exception e) {
            current = 0L;
        }

        if (current > this.reach) {
            current = this.reach;
        }

        return current;
    }
}
