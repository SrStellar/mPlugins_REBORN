package tk.slicecollections.maxteer.player.preferences;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.ContainerAbstract;
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

    @Override
    @SneakyThrows
    public void load() {
        JSONObject preferencesCurrent = loadPreferencesJSON();
        List<Integer> noHasID = Arrays.stream(PreferenceEnum.values()).map(PreferenceEnum::getId).filter(id -> !preferencesCurrent.containsKey(String.valueOf(id))).collect(Collectors.toList());
        noHasID.forEach(integer -> preferencesCurrent.put(integer, true));
        loadProfileInformation().updateValue("preferences", preferencesCurrent);
    }

    public boolean getPreference(PreferenceEnum preference) {
        try {
            JSONObject preferencesJSON = loadPreferencesJSON();
            return (boolean) preferencesJSON.get(String.valueOf(preference.getId()));
        } catch (ParseException e) {
            return true;
        }
    }

    public String getStateName(PreferenceEnum preferenceEnum) {
        boolean state = getPreference(preferenceEnum);
        return state ? "§aAtivado" : "§cDesativado";
    }

    public String getStateName(PreferenceEnum preferenceEnum, boolean noColors) {
        boolean state = getPreference(preferenceEnum);
        return state ? "Ativado" : "Desativado";
    }

    public String getInkColor(PreferenceEnum preferenceEnum) {
        boolean state = getPreference(preferenceEnum);
        return state ? "10" : "1";
    }

    public String getGlassColor(PreferenceEnum preferenceEnum) {
        boolean state = getPreference(preferenceEnum);
        return state ? "5" : "14";
    }

    @SneakyThrows
    public void changePreference(PreferenceEnum preference) {
        JSONObject preferencesJSON = loadPreferencesJSON();
        if (getPreference(preference)) {
            preferencesJSON.put(preference.getId(), false);
        } else {
            preferencesJSON.put(preference.getId(), true);
        }

        updateInformation(preferencesJSON);
    }

    private ProfileInformation loadProfileInformation() {
        return loadCollection(ProfileInformation.class);
    }

    private JSONObject loadPreferencesJSON() throws ParseException {
        return (JSONObject) loadProfileInformation().getAsJsonObject().get("preferences");
    }

    @SneakyThrows
    private void updateInformation(JSONObject object) {
        loadProfileInformation().updateValue("preferences", object);
    }
}
