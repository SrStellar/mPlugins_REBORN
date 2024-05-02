package tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container.collections;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container.ProxiedContainer;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;

public class ProfileInfoContainer extends ProxiedContainer {

    public ProfileInfoContainer() {
        super("profileinfo");
    }

    @SneakyThrows
    public JSONObject getPreferences() {
        return (JSONObject) this.getAsJsonObject().get("preferences");
    }

    public boolean getPreference(PreferenceEnum preference) {
        try {
            JSONObject preferencesJSON = getPreferences();
            return (boolean) preferencesJSON.get(String.valueOf(preference.getId()));
        } catch (Exception e) {
            return true;
        }
    }

}
