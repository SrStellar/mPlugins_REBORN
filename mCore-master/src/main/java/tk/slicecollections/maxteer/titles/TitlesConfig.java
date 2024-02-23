package tk.slicecollections.maxteer.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TitlesConfig {

    MATERIAL("EMPTY_MAP", "STAINED_GLASS_PANE", "MAP"),
    DURABILITY("0", "14", "0"),
    COLOR("§a", "§c", "§6"),
    ACTION_TITLE("&eClique para selecionar!", "&cVocê não possui este título.", "&eClique para deselecionar!");

    private final String valueHas;
    private final String valueNoHas;
    private final String valueSelected;
}
