package tk.slicecollections.maxteer.lobby.cmd.ml;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.boosters.BoosterType;
import tk.slicecollections.maxteer.lobby.cmd.SubCommand;
import tk.slicecollections.maxteer.player.Profile;

public class GiveCommand extends SubCommand {

  public GiveCommand() {
    super("dar", "dar [jogador] booster", "Dar multiplicadores.", false);
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§3/ml dar [jogador] booster [private/network] [multiplicador] [minutos]\n ");
      return;
    }

    Profile target = Profile.loadProfile(args[0]);
    if (target == null) {
      sender.sendMessage("§cUsuário não encontrado.");
      return;
    }

    String action = args[1];
    if (action.equalsIgnoreCase("booster")) {
      if (args.length < 5) {
        sender.sendMessage("§cUtilize /ml dar [jogador] booster [private/network] [multiplicador] [minutos]");
        return;
      }

      try {
        BoosterType type = BoosterType.valueOf(args[2].toUpperCase());
        try {
          double multiplier = Double.parseDouble(args[3]);
          long minuts = Long.parseLong(args[4]);
          if (multiplier < 1.0D || minuts < 1) {
            throw new Exception();
          }

          target.loadBoosterContainer().addBooster(type, minuts, multiplier);
          sender.sendMessage("§aMultiplicador adicionado.");
        } catch (Exception ex) {
          sender.sendMessage("§cUtilize números válidos.");
        }
      } catch (Exception ex) {
        sender.sendMessage("§cUtilize /ml dar [jogador] booster [private/network] [multiplicador] [minutos]");
      }
    } else {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§3/ml dar [jogador] booster [private/network] [multiplicador] [minutos]\n ");
    }
  }

}
