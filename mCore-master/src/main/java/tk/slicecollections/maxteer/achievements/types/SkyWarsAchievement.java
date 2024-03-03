package tk.slicecollections.maxteer.achievements.types;

import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.achievements.AchievementReward;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.SkyWarsStatsInformation;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;

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
    }

    public SkyWarsAchievement(Long id, String name, Class<? extends DataCollection> classCollection, String key, Long reach, String iconBase, AchievementReward reward, Long amountReward, Title titleReward) {
        super(id, name, classCollection, key, reach, iconBase, reward, amountReward, titleReward);
    }

    @Override
    public void check(Profile profile) {

    }

    @Override
    public ItemStack getIcon(Profile profile) {
        return null;
    }
}
