package tk.slicecollections.maxteer.party;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartyRole {

  MEMBER("Membro"),
  LEADER("Líder");

  private final String name;

}
