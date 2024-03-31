package tk.slicecollections.maxteer.boosters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tk.slicecollections.maxteer.utils.TimeUtils;

@AllArgsConstructor
@Getter
@Setter
public class BoosterNetwork {

    private Long time;
    private Double multiply;
    private String owner;
    private String minigame;

    public String getRemainingTime() {
        long finalTime = (time - System.currentTimeMillis());
        return TimeUtils.getTime(finalTime, true);
    }

    public boolean hasBoosterActivated() {
        return time > 0L && (time - System.currentTimeMillis()) > 0;
    }
}
