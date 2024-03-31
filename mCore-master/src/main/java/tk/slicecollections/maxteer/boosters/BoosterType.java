package tk.slicecollections.maxteer.boosters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BoosterType {
    PRIVATE(0L),
    NETWORK(1L);

    private final Long ID;

    public static BoosterType findByID(Long ID) {
        return Arrays.stream(BoosterType.values()).filter(boosterType -> boosterType.getID().equals(ID)).findFirst().orElse(BoosterType.PRIVATE);
    }
}
