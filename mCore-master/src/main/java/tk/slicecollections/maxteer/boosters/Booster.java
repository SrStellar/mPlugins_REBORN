package tk.slicecollections.maxteer.boosters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.slicecollections.maxteer.utils.TimeUtils;

@AllArgsConstructor
@Getter
public class Booster {

    private Long id;
    private Long timeMinutes;
    private Double multiply;
    private BoosterType boosterType;

    public String getTimeFormatted() {
        return TimeUtils.getTime(this.timeMinutes, true);
    }
}
