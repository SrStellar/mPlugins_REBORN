package tk.slicecollections.maxteer.player.preferences;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PreferenceEnum {

    BLOOD_AND_GORE(0),
    PLAYER_VISIBILITY(1),
    PRIVATE_MESSAGES(2),
    PROTECTION_LOBBY(3);

    private final Integer id;

    public static PreferenceEnum findByID(Integer id) {
        return Arrays.stream(PreferenceEnum.values()).filter(preferencesEnum -> preferencesEnum.getId().equals(id)).findFirst().orElse(null);
    }
}
