package tk.slicecollections.maxteer.lobby.cmd.ml;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.lobby.cmd.SubCommand;
import tk.slicecollections.maxteer.lobby.lobby.PlayNPC;
import tk.slicecollections.maxteer.lobby.lobby.ServerEntry;

public class NPCPlayCommand extends SubCommand {

  public NPCPlayCommand() {
    super("npcjogar", "npcjogar", "Adicione/remova NPC de Jogar.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eAjuda\n \n§3/ml npcjogar adicionar [id] [entry] §f- §7Adicionar NPC.\n§3/ml npcjogar remover [id] §f- §7Remover NPC.\n ");
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("adicionar")) {
      if (args.length <= 2) {
        player.sendMessage("§cUtilize /ml npcjogar adicionar [id] [entry]");
        return;
      }

      String id = args[1];
      if (PlayNPC.getById(id) != null) {
        player.sendMessage("§cJá existe um NPC de Jogar utilizando \"" + id + "\" como ID.");
        return;
      }

      ServerEntry entry = ServerEntry.getByKey(args[2]);
      if (entry == null) {
        player.sendMessage("§cUtilize /ml npcjogar adicionar [id] [entry]");
        return;
      }

      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      PlayNPC.add(id, location, entry);
      player.sendMessage("§aNPC de Jogar adicionado com sucesso.");
    } else if (action.equalsIgnoreCase("remover")) {
      if (args.length <= 1) {
        player.sendMessage("§cUtilize /ml npcjogar remover [id]");
        return;
      }

      String id = args[1];
      PlayNPC npc = PlayNPC.getById(id);
      if (npc == null) {
        player.sendMessage("§cNão existe um NPC de Jogar utilizando \"" + id + "\" como ID.");
        return;
      }

      PlayNPC.remove(npc);
      player.sendMessage("§cNPC de Jogar removido com sucesso.");
    } else {
      player.sendMessage(" \n§eAjuda - NPC Jogar\n \n§3/ml npcjogar adicionar [id] [entry] §f- §7Adicionar NPC.\n§3/ml npcjogar remover [id] §f- §7Remover NPC.\n ");
    }
  }
}
