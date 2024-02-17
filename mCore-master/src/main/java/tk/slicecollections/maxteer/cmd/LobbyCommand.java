package tk.slicecollections.maxteer.cmd;

import org.bukkit.command.CommandSender;

public class LobbyCommand extends Commands{
    public LobbyCommand() {
        super("lobby");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        sender.sendMessage("§aFOI");
    }
}
