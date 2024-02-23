package tk.slicecollections.maxteer.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Titles {

    SENTINELA_DA_PONTE(0L, "§cSentinela da Ponte", "{material}:{durability} : 1 : nome>{color}Sentinela da Ponte : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Assassino das Pontes\"&8.\n \n{action}"),
    LIDER_DA_PONTE(1L, "§6Líder da Ponte", "{material}:{durability} : 1 : nome>{color}Líder da Ponte : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Glorioso sobre Pontes\"&8.\n \n{action}"),
    PONTUADOR_MESTRE(2L, "§ePontuador Mestre", "{material}:{durability} : 1 : nome>{color}Pontuador Mestre : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Maestria em Pontuação\"&8.\n \n{action}"),

    ANJO_DA_MORTE(3L, "§cAnjo da Morte", "{material}:{durability} : 1 : nome>{color}Anjo da Morte : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Traidor Celestial\"&8.\n \n{action}"),
    REI_CELESTIAL(4L, "§bRei Celestial", "{material}:{durability} : 1 : nome>{color}Rei Celestial : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Destrono Celestial\"&8.\n \n{action}"),
    COMPANHEIRO_DE_ASAS(5L, "§6Companheiro de Asas", "{material}:{durability} : 1 : nome>{color}Companheiro de Asas : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Anjo Guardião\"&8.\n \n{action}"),

    SHERLOCK_HOLMES(6L, "§6Sherlock Holmes", "{material}:{durability} : 1 : nome>{color}Sherlock Holmes : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Detetive\"&8.\n \n{action}"),
    JEF_THE_KILLER(7L, "§4Jef the Killer", "{material}:{durability} : 1 : nome>{color}Jef the Killer : desc>&8Pode ser desbloqueado através do\n&8Desafio \"Serial Killer\"&8.\n \n{action}");

    private final Long id;
    private final String title;
    private final String icon;

}
