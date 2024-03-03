package tk.slicecollections.maxteer.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.collections.CoinsGenericInformation;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.database.cache.types.SkyWarsCache;
import tk.slicecollections.maxteer.menus.MenuDeliveries;
import tk.slicecollections.maxteer.menus.MenuProfile;
import tk.slicecollections.maxteer.menus.profile.MenuPreferences;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.titles.Title;
import tk.slicecollections.maxteer.titles.TitleManager;

public class TestCmd extends Commands{
    public TestCmd() {
        super("a");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cSOMENTE PARA PLAYER O DEGRAÇA");
            return;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUSA ARGUMENTOS FILHO DA PUTA! >:(");
            return;
        }

        String arg = args[0];
        Profile profile = Profile.loadProfile(sender.getName());
        if (profile == null) {
            return;
        }

        switch (arg) {
            case "ativar": {
                Title title = TitleManager.findByID((long) (Math.random() * TitleManager.listTiles().size() - 1));
                try {
                    profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).setSelectedTitle(title);
                    sender.sendMessage("§cTítulo " + title.getTitle() + " foi ativo o seu bosta");
                    title.setSelected(profile);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                break;
            }

            case "desativar": {
                try {
                    Title title = TitleManager.findByID(profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle());
                    profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).setSelectedTitle(null);
                    title.destroy(profile);
                    sender.sendMessage("§cTodos os seus títulos foram desativados, seu zé porra");

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                break;
            }

            case "delivery": {
                new MenuDeliveries(profile);
                break;
            }

            case "profile": {
                new MenuProfile(profile);
                break;
            }

            case "preferences": {
                new MenuPreferences(profile);
                break;
            }

            case "addCoins": {
                profile.getCache().loadTableCache(SkyWarsCache.class).loadCollectionGeneric(CoinsGenericInformation.class, "coins").addCoins(10D);
                sender.sendMessage("§aAdicionado com sucesso!");
                break;
            }
        }
    }
}
