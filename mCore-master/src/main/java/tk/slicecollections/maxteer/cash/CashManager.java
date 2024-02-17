package tk.slicecollections.maxteer.cash;

import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;

@AllArgsConstructor
public class CashManager {

    private Profile profile;

    public Long getCash() {
        if (this.profile == null) {
            return 0L;
        }

        try {
            return (Long) this.profile.getCache().loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class).getAsJsonObject().get("cash");
        } catch (ParseException e) {
            return 0L;
        }
    }

    public void addCash(Long amount) throws Exception {
        if (this.profile == null) {
            return;
        }

        ProfileInformation information = this.profile.getCache().loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class);
        Long cashCurrent = getCash();
        information.updateValue("cash", cashCurrent + amount);
        System.out.println(cashCurrent + amount);
    }

    public void removeCash(Long amount) throws Exception {
        if (this.profile == null) {
            return;
        }

        ProfileInformation information = this.profile.getCache().loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class);
        Long cashCurrent = getCash();
        information.updateValue("cash", cashCurrent - amount);
    }

    public void setCash(Long amount) throws Exception {
        if (this.profile == null) {
            return;
        }

        ProfileInformation information = this.profile.getCache().loadTableCache(ProfileCache.class).loadCollection(ProfileInformation.class);
        information.updateValue("cash", amount);
    }

}
