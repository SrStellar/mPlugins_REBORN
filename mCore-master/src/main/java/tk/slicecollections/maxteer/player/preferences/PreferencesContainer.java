package tk.slicecollections.maxteer.player.preferences;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.ContainerAbstract;
import tk.slicecollections.maxteer.database.cache.DataCache;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oJVzinn
 */
@SuppressWarnings("unchecked")
public class PreferencesContainer extends ContainerAbstract {

    public PreferencesContainer(Profile profile) {
        super(profile, ProfileCache.class);
    }

    public void load() {
        try {
            JSONObject preferencesCurrent = loadPreferencesJSON();
            List<Integer> noHasID = Arrays.stream(PreferenceEnum.values()).map(PreferenceEnum::getId).filter(id -> !preferencesCurrent.containsKey(id)).collect(Collectors.toList());
            noHasID.forEach(integer -> preferencesCurrent.put(integer, true));
            loadProfileInformation().updateValue("preferences", preferencesCurrent);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getPreference(PreferenceEnum preference) {
        try {
            JSONObject preferencesJSON = loadPreferencesJSON();
            return (boolean) preferencesJSON.get(String.valueOf(preference.getId()));
        } catch (ParseException e) {
            return true;
        }
    }

    public void changePreference(PreferenceEnum preference) {
        try {
            JSONObject preferencesJSON = loadPreferencesJSON();
            if (getPreference(preference)) {
                preferencesJSON.put(preference.getId(), false);
                return;
            }

            preferencesJSON.put(preference.getId(), true);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private ProfileInformation loadProfileInformation() {
        return loadCollection(ProfileInformation.class);
    }

    private JSONObject loadPreferencesJSON() throws ParseException {
        return (JSONObject) loadProfileInformation().getAsJsonObject().get("preferences");
    }
}
