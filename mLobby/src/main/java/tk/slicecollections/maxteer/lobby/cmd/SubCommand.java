package tk.slicecollections.maxteer.lobby.cmd;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

  @Getter
  private String name;
  @Getter
  private String usage;
  @Getter
  private String description;
  private final boolean onlyForPlayer;

  public SubCommand(String name, String usage, String description, boolean onlyForPlayer) {
    this.name = name;
    this.usage = usage;
    this.description = description;
    this.onlyForPlayer = onlyForPlayer;
  }

  public void perform(CommandSender sender, String[] args) {}

  public void perform(Player player, String[] args) {}

  public boolean onlyForPlayer() {
    return this.onlyForPlayer;
  }
}
