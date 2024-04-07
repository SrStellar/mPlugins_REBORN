package tk.slicecollections.maxteer.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.slicecollections.maxteer.achievements.types.SkyWarsAchievement;
import tk.slicecollections.maxteer.achievements.types.TheBridgeAchievement;
import tk.slicecollections.maxteer.database.cache.types.MurderCache;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.database.cache.types.TheBridgeCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;

@AllArgsConstructor
@Getter
public enum AchievementReward {

    COINS("coins"),
    TITLE("title"),
    CASH("cash");

    private final String name;

    public void setupReward(Profile profile, Double amount, Title title, Achievement achievement) {
        switch (this.name) {
            case "coins": {
                if (achievement instanceof SkyWarsAchievement) {
                    profile.loadCoinsContainer(SkyWarsCache.class).addCoins(amount);
                } else if (achievement instanceof TheBridgeAchievement) {
                    profile.loadCoinsContainer(TheBridgeCache.class).addCoins(amount);
                } else {
                    profile.loadCoinsContainer(MurderCache.class).addCoins(amount);
                }

                break;
            }

            case "cash": {
                try {
                    profile.getCashManager().addCash(amount.longValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            case "title": {
                title.addTitleToPlayer(profile);
                break;
            }
        }
    }
}
