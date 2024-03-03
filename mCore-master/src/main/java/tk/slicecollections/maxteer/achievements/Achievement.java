package tk.slicecollections.maxteer.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.database.cache.Data;
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
    }

    @SuppressWarnings("unchecked")
    public static <T extends Achievement> List<T> listByClass(Class<T> clazz) {
        return ACHIEVEMENTS.stream().filter(achievement -> achievement.getClass().isAssignableFrom(clazz)).map(achievement -> (T) achievement).collect(Collectors.toList());
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

    private Long id;
    private String name;
    private Class<? extends DataCollection> classCollection;
    private String key;
    private Long reach;
    private String iconBase;
    private AchievementReward reward;
    private Long amountReward;
    private Title titleReward;

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
