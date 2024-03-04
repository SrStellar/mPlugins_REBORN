package tk.slicecollections.maxteer.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;

@AllArgsConstructor
@Getter
public enum AchievementReward {

    COINS("coins"),
    TITLE("title"),
    CASH("cash");

    private final String name;

    public void setupReward(Profile profile, Double amount, Title title) {
        switch (this.name) {
            case "coins": {
                profile.loadCoinsContainer(SkyWarsCache.class).addCoins(amount);
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
                System.out.println("a");
                break;
            }
        }
    }
}
