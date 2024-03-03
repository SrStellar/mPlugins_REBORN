package tk.slicecollections.maxteer.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.database.cache.collections.CoinsGenericInformation;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.player.Profile;

public class CoinsCommand extends Commands{
    public CoinsCommand() {
        super("coins");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cComando exclusivo para jogadores!");
            return;
        }

        Profile profile = Profile.loadProfile(sender.getName());
        sender.sendMessage("§fSeus coins:\n \n §8• §fSkyWars: §e" + profile.getCache().loadTableCache(SkyWarsCache.class).loadCollectionGeneric(CoinsGenericInformation.class, "coins").getCoins());
    }
}
