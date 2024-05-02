package tk.slicecollections.maxteer.lobby.cmd;

import java.util.Arrays;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import tk.slicecollections.maxteer.Core;

public abstract class Commands extends Command {
  
  public Commands(String name, String... aliases) {
    super(name);
    this.setAliases(Arrays.asList(aliases));
    
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      simpleCommandMap.register(this.getName(), "mlobby", this);
    } catch (ReflectiveOperationException ex) {
      Core.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
    }
  }
  
  public abstract void perform(CommandSender sender, String label, String[] args);
  
  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    this.perform(sender, commandLabel, args);
    return true;
  }
  
  public static void setupCommands() {
    new LobbyCommand();
  }
}
