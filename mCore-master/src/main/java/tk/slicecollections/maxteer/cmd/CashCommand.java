package tk.slicecollections.maxteer.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.cash.CashManager;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;

import java.util.Objects;

public class CashCommand extends Commands {

    public CashCommand() {
        super("cash");
    }

    @Override
    @SuppressWarnings("all")
    public void perform(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Role.findRoleByPermission(player).getId() > 1 || args.length < 1) {
                sender.sendMessage("§eSeu cash: §b" + new CashManager(Objects.requireNonNull(Profile.loadProfile(sender.getName()))).getCash());
                return;
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage("§cArgumentos insuficientes!");
                return;
            }
        }

        String options = args[0];
        switch (options.toLowerCase()) {
            case "add": {
                if (args.length < 3) {
                    sender.sendMessage("§cUtilize \"/cash add <jogador> [quantia]\" para adicionar cash.");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                try {
                    Profile.loadProfile(targetP.getName()).getCashManager().addCash(amount);
                } catch (Exception e) {
                    sender.sendMessage("§cEsse usuário não está online!");
                }
                sender.sendMessage("§aQuantia de cash setada com sucesso!");
                break;
            }

            case "remove": {
                if (args.length < 3) {
                    sender.sendMessage("§cUtilize \"/cash remove <jogador> [quantia]\" para remover o cash do jogador.");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                CashManager cashManager = new CashManager(Objects.requireNonNull(Profile.loadProfile(targetP.getName())));
                if (cashManager.getCash() - amount < 0) {
                    sender.sendMessage("§aNão é possível deixar o jogador com cash negativo!");
                    return;
                }

                try {
                    cashManager.removeCash(amount);
                } catch (Exception e) {
                    sender.sendMessage("§cEsse usuário não está online!");
                }
                sender.sendMessage("§aQuantia de cash removida com sucesso!");
                break;
            }

            case "set": {
                if (args.length < 3) {
                    sender.sendMessage("§cUtilize \"/cash set <jogador> [quantia]\" para definir o cash do jogador.");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                try {
                    new CashManager(Objects.requireNonNull(Profile.loadProfile(targetP.getName()))).setCash(amount);
                } catch (Exception e) {
                    sender.sendMessage("§cEsse usuário não está online!");
                }
                sender.sendMessage("§aQuantia de cash setada com sucesso!");
                break;
            }

            default: {
                if (sender instanceof Player) {
                    sender.sendMessage("§eSeu cash: §b" + new CashManager(Objects.requireNonNull(Profile.loadProfile(sender.getName()))).getCash());
                } else {
                    sender.sendMessage("§cArgumentos insuficientes!");
                }
                break;
            }
        }
    }
}
