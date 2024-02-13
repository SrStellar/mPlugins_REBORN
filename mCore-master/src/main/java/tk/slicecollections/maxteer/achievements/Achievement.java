package tk.slicecollections.maxteer.achievements;

import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.achievements.types.MurderAchievement;
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.achievements.types.TheBridgeAchievement;
import tk.slicecollections.maxteer.player.Profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maxter
 */
public abstract class Achievement {

  private String id;
  private String name;

  public Achievement(String id, String name) {
    this.id = id;
    this.name = name;
  }

  protected abstract void give(Profile profile);

  protected abstract boolean check(Profile profile);

  public abstract ItemStack getIcon(Profile profile);

  public void complete(Profile profile) {
    profile.getAchievementsContainer().complete(this);
    this.give(profile);
  }

  public boolean canComplete(Profile profile) {
    return profile.isOnline() && !this.isCompleted(profile) && this.check(profile);
  }

  public boolean isCompleted(Profile profile) {
    return profile.getAchievementsContainer().isCompleted(this);
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  private static final List<Achievement> ACHIEVEMENTS = new ArrayList<>();

  public static void setupAchievements() {
    SkyWarsAchievement.setupAchievements();
    TheBridgeAchievement.setupAchievements();
    MurderAchievement.setupAchievements();
  }

  public static void addAchievement(Achievement achievement) {
    ACHIEVEMENTS.add(achievement);
  }

  public static Collection<Achievement> listAchievements() {
    return ACHIEVEMENTS;
  }

  @SuppressWarnings("unchecked")
  public static <T extends Achievement> List<T> listAchievements(Class<T> type) {
    return listAchievements().stream().filter(achievement -> achievement.getClass().equals(type)).map(achievement -> (T) achievement).collect(Collectors.toList());
  }
}
