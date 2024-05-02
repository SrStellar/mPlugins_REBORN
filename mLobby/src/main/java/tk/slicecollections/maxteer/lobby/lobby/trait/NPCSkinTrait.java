package tk.slicecollections.maxteer.lobby.lobby.trait;

import tk.slicecollections.maxteer.libraries.npclib.api.npc.NPC;
import tk.slicecollections.maxteer.libraries.npclib.npc.skin.Skin;
import tk.slicecollections.maxteer.libraries.npclib.npc.skin.SkinnableEntity;
import tk.slicecollections.maxteer.libraries.npclib.trait.NPCTrait;

public class NPCSkinTrait extends NPCTrait {

  private final Skin skin;
  
  public NPCSkinTrait(NPC npc, String value, String signature) {
    super(npc);
    this.skin = Skin.fromData(value, signature);
  }
  
  @Override
  public void onSpawn() {
    this.skin.apply((SkinnableEntity) this.getNPC().getEntity());
  }
}
