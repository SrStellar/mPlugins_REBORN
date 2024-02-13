package tk.slicecollections.maxteer.database.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum DataTypes {

    MYSQL("MySQL");

    private final String type;

    public static DataTypes findByType(String type) {
        return Arrays.stream(DataTypes.values()).filter(dataTypes -> dataTypes.getType().equalsIgnoreCase(type)).findFirst().orElse(DataTypes.MYSQL);
    }

}
