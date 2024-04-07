package tk.slicecollections.maxteer.achievements.types;

import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.achievements.AchievementReward;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.SkyWarsStatsInformation;
import tk.slicecollections.maxteer.database.cache.collections.TheBridgeStatsInformation;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.Objects;

public class TheBridgeAchievement extends Achievement {

    public static void setupAchievements() {
        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Assassino (Solo)",
                TheBridgeStatsInformation.class,
                "1v1kills",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Assassino Mestre (Solo)",
                TheBridgeStatsInformation.class,
                "1v1kills",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso (Solo)",
                TheBridgeStatsInformation.class,
                "1v1wins",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso Mestre (Solo)",
                TheBridgeStatsInformation.class,
                "1v1wins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Pontuador (Solo)",
                TheBridgeStatsInformation.class,
                "1v1points",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7pontos para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Pontuador Mestre (Solo)",
                TheBridgeStatsInformation.class,
                "1v1points",
                1000L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7pontos para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Persistente (Solo)",
                TheBridgeStatsInformation.class,
                "1v1games",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Assassino (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2kills",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Assassino Mestre (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2kills",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2wins",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso Mestre (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2wins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Pontuador (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2points",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7pontos para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Pontuador Mestre (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2points",
                1000L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7pontos para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Persistente (Dupla)",
                TheBridgeStatsInformation.class,
                "2v2games",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Assassino das Pontes",
                TheBridgeStatsInformation.class,
                "1v1kills",
                500L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &fTítulo: &cSentinela da Ponte\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(0L),
                "2v2kills"));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Glorioso sobre Pontes",
                TheBridgeStatsInformation.class,
                "1v1wins",
                400L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &fTítulo: &6Líder da Ponte\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(1L),
                "2v2wins"));

        Achievement.registerNewAchievement(new TheBridgeAchievement((long) Achievement.listAchievements().size(),
                "Maestria em Pontuação",
                TheBridgeStatsInformation.class,
                "1v1points",
                500L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &fTítulo: &ePontuador Mestre\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(2L),
                "2v2points"));
    }

    public TheBridgeAchievement(Long id, String name, Class<? extends DataCollection> classCollection, String key, Long reach, String iconBase, AchievementReward reward, Long amountReward, Title titleReward, String... additionalKeys) {
        super(id, name, classCollection, key, reach, iconBase, reward, amountReward, titleReward, additionalKeys);
    }

    @SneakyThrows
    @Override
    public void check(Profile profile) {
        if (hasCompleted(profile)) {
            return;
        }

        if (Objects.equals(getCurrent(profile), this.reach)) {
            this.reward.setupReward(profile, Double.valueOf(this.amountReward), titleReward);
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
