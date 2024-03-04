package tk.slicecollections.maxteer.achievements.types;

import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.achievements.AchievementReward;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.SkyWarsStatsInformation;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;
import tk.slicecollections.maxteer.titles.TitleManager;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.Objects;

public class SkyWarsAchievement extends Achievement {

    public static void setupAchievements() {
        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assassino (Solo)",
                SkyWarsStatsInformation.class,
                "1v1kills",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assassino Mestre (Solo)",
                SkyWarsStatsInformation.class,
                "1v1kills",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso (Solo)",
                SkyWarsStatsInformation.class,
                "1v1wins",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso Mestre (Solo)",
                SkyWarsStatsInformation.class,
                "1v1wins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assistente (Solo)",
                SkyWarsStatsInformation.class,
                "1v1assists",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assistente Mestre (Solo)",
                SkyWarsStatsInformation.class,
                "1v1assists",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Persistente (Solo)",
                SkyWarsStatsInformation.class,
                "1v1games",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assassino (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2kills",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assassino Mestre (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2kills",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2wins",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Vitorioso Mestre (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2wins",
                200L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61000 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                1000L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assistente (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2assists",
                50L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &6100 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                100L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Assistente Mestre (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2assists",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &6500 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                500L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Persistente (Dupla)",
                SkyWarsStatsInformation.class,
                "2v2games",
                250L,
                "%material% : 1 : nome>%name% : desc>&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins\n \n&fProgresso: %progress%",
                AchievementReward.COINS,
                250L,
                null));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Traidor Celestial",
                SkyWarsStatsInformation.class,
                "1v1kills",
                500L,
                "%material% : 1 : nome>%name% : desc>&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &fTítulo: &cAnjo da Morte\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(3L),
                "2v2kills"));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Destrono Celestial",
                SkyWarsStatsInformation.class,
                "1v1games",
                400L,
                "%material% : 1 : nome>%name% : desc>&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &fTítulo: &bRei Celestial\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(4L),
                "2v2games"));

        Achievement.registerNewAchievement(new SkyWarsAchievement((long) Achievement.listAchievements().size(),
                "Anjo Guardião",
                SkyWarsStatsInformation.class,
                "1v1assists",
                500L,
                "%material% : 1 : nome>%name% : desc>&7Consiga um total de %reach%\n&7assistências para receber:\n \n &8• &fTítulo: &6Companheiro de Asas\n \n&fProgresso: %progress%",
                AchievementReward.TITLE,
                0L,
                TitleManager.findByID(5L),
                "2v2assists"));
    }

    public SkyWarsAchievement(Long id, String name, Class<? extends DataCollection> classCollection, String key, Long reach, String iconBase, AchievementReward reward, Long amountReward, Title titleReward, String... additionalKeys) {
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
            current = profile.loadSkyWarsStatsContainer().getInformation(this.key);
            for (String additionalKey : additionalKeys) {
                current += (Long) profile.loadSkyWarsStatsContainer().getInformation(additionalKey);
            }
        } catch (Exception e) {
            current = 0L;
            e.printStackTrace();
        }

        if (current > this.reach) {
            current = this.reach;
        }

        return current;
    }
}
