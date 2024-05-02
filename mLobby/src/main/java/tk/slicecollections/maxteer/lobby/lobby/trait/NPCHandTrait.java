package tk.slicecollections.maxteer.lobby.lobby.trait;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.slicecollections.maxteer.libraries.npclib.api.npc.NPC;
import tk.slicecollections.maxteer.libraries.npclib.trait.NPCTrait;

public class NPCHandTrait extends NPCTrait {

  private final ItemStack inHand;

  public NPCHandTrait(NPC npc, ItemStack inHand) {
    super(npc);
    this.inHand = inHand;
  }

  @Override
  public void onSpawn() {
    ((Player) this.getNPC().getEntity()).setItemInHand(this.inHand);
  }
}
