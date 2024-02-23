package tk.slicecollections.maxteer.deliveries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RewardType {
    COMANDO(1),
    CASH(1),
    SkyWars_COINS(1),
    BedWars_Coins(1),
    TheBridge_COINS(1),
    Murder_COINS(1),
    PRIVATE_BOOSTER(3),
    NETWORK_BOOSTER(3);

    private final int parameters;

    public static RewardType from(String name) {
        return Arrays.stream(RewardType.values()).filter(rewardType -> rewardType.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @SneakyThrows
    public Object[] parseValues(String value) {
        switch (this) {
            case COMANDO: {
                return new Object[]{value};
            }

            case CASH: {
                return new Object[]{Long.parseLong(value)};
            }

            default: {
                if (this.name().contains("_COINS")) {
                    return new Object[]{Double.parseDouble(value)};
                } else if (this.name().contains("_BOOSTER")) {
                    String[] values = value.split(":");
                    return new Object[]{Integer.parseInt(values[0]), Double.parseDouble(values[1]), Long.parseLong(values[2])};
                }
            }
        }

        throw new Exception();
    }
}
