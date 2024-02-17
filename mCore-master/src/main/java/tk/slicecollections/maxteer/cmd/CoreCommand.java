package tk.slicecollections.maxteer.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.Core;

public class CoreCommand extends Commands {
    public CoreCommand() {
        super("mcore", "mc");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("mcore.admin")) {
                player.sendMessage("§6mCoreReborn §bv" + Core.getInstance().getDescription().getVersion() + " §7Criado por §aMaxteer§7 §7e atualizado por §aoJVzinn§7.");
                return;
            }

            if (args.length == 0) {
                player.sendMessage(" \n§6/mc atualizar §f- §7Atualizar o mCore.\n§6/mc converter §f- §7Converter seu Banco de Dados.\n ");
                return;
            }

            String action = args[0];
            if (action.equalsIgnoreCase("atualizar")) {
                player.sendMessage("§aO plugin já se encontra em sua última versão.");
            } else if (action.equalsIgnoreCase("converter")) {
                player.sendMessage("§cO plugin só se encontra com suporte ao banco de dados MySQL.");
            } else {
                player.sendMessage(" \n§6/sc atualizar §f- §7Atualizar o mCore.\n§6/sc converter §f- §7Converter seu Banco de Dados.\n ");
            }
        } else {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
        }
    }
}
