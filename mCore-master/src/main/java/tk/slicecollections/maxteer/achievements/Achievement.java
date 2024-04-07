package tk.slicecollections.maxteer.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.achievements.types.MurderAchievement;
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.achievements.types.TheBridgeAchievement;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.cache.collections.AchievementsInformation;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public abstract class Achievement {

    private static final List<Achievement> ACHIEVEMENTS = new ArrayList<>();

    public static void setupAchievements() {
        SkyWarsAchievement.setupAchievements();
        TheBridgeAchievement.setupAchievements();
        MurderAchievement.setupAchievements();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Achievement> List<T> listByClass(Class<T> clazz) {
        return ACHIEVEMENTS.stream().filter(achievement -> achievement.getClass().isAssignableFrom(clazz)).map(achievement -> (T) achievement).collect(Collectors.toList());
    }

    public static List<Achievement> listAllByClass(Class<? extends Achievement> clazz) {
        return ACHIEVEMENTS.stream().filter(achievement -> achievement.getClass().isAssignableFrom(clazz)).collect(Collectors.toList());
    }

    public static Achievement findById(Long ID) {
        return ACHIEVEMENTS.stream().filter(achievement -> achievement.getId().equals(ID)).findFirst().orElse(null);
    }

    public static List<Achievement> listAchievements() {
        return ACHIEVEMENTS;
    }

    public static void registerNewAchievement(Achievement achievement) {
        ACHIEVEMENTS.add(achievement);
    }

    public static void removeAchievement(Achievement achievement) {
        ACHIEVEMENTS.remove(achievement);
    }

    protected Long id;
    protected String name;
    protected Class<? extends DataCollection> classCollection;
    protected String key;
    protected Long reach;
    protected String iconBase;
    protected AchievementReward reward;
    protected Long amountReward;
    protected Title titleReward;
    protected String[] additionalKeys;

    public abstract void check(Profile profile);
    public abstract ItemStack getIcon(Profile profile);

    public boolean hasCompleted(Profile profile) {
        AchievementsInformation information = profile.loadAchievementsContainer();
        try {
            return information.hasAchievement(this);
        } catch (ParseException e) {
            return false;
        }
    }

}
