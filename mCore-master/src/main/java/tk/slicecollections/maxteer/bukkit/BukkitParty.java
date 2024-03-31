package tk.slicecollections.maxteer.bukkit;

import tk.slicecollections.maxteer.party.Party;

public class BukkitParty extends Party {

  public BukkitParty(String leader, int slots) {
    super(leader, slots);
  }

  @Override
  public void delete() {
    BukkitPartyManager.getBUKKIT_PARTIES().remove(this);
    this.destroy();
  }
}
