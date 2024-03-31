package tk.slicecollections.maxteer.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.boosters.Booster;
import tk.slicecollections.maxteer.boosters.BoosterType;
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

            case "addBooster": {
                profile.loadBoosterContainer().addBooster(BoosterType.PRIVATE, 1L, 2.0);
                sender.sendMessage("§aBooster adicionado a essa conta com sucesso!");
                break;
            }

            case "addBoosterNet": {
                profile.loadBoosterContainer().addBooster(BoosterType.NETWORK, 1L, 2.0);
                sender.sendMessage("§aBooster adicionado a essa conta com sucesso!");
                break;
            }

            case "listBoosters": {
                StringBuilder sb = new StringBuilder();
                for (Booster booster : profile.loadBoosterContainer().listAllBoosters()) {
                    sb.append("§fTipo: §a").append(booster.getBoosterType().name().toUpperCase())
                            .append("\n")
                            .append("§fID: §a").append(booster.getId())
                            .append("\n")
                            .append("§fTempo: §a").append(booster.getTimeFormatted())
                            .append("\n")
                            .append("§fMultiplicador: §a").append(booster.getMultiply())
                            .append("\n");
                }

                sender.sendMessage("§aSeus boosters:\n" + sb);
                break;
            }

            case "ativarBooster": {
                if (profile.loadBoosterContainer().listAllBoosters(BoosterType.PRIVATE).isEmpty()) {
                    sender.sendMessage("§cVocê não possui nenhum booster ativo!");
                    return;
                }

                if (profile.loadBoosterContainer().hasBoosterActivated()) {
                    sender.sendMessage("§eVocê já possui um booster ativo!");
                    return;
                }

                profile.loadBoosterContainer().activateBooster(profile.loadBoosterContainer().listAllBoosters(BoosterType.PRIVATE).get(0));
                sender.sendMessage("§aBooster ativo com sucesso!");
                break;
            }

            case "ativarBoosterNet": {
                if (profile.loadBoosterContainer().listAllBoosters(BoosterType.NETWORK).isEmpty()) {
                    sender.sendMessage("§cVocê não possui nenhum booster ativo!");
                    return;
                }

                if (profile.loadBoosterContainer().hasBoosterActivated()) {
                    sender.sendMessage("§eVocê já possui um booster ativo!");
                    return;
                }

                profile.loadBoosterContainer().activateBooster(profile.loadBoosterContainer().listAllBoosters(BoosterType.NETWORK).get(0));
                sender.sendMessage("§aBooster ativo com sucesso!");
                break;
            }

            case "verBoosterAtivo": {
                JSONObject response = profile.loadBoosterContainer().getBoosterActivated();
                if (response == null) {
                    sender.sendMessage("§cVocê não possui nenhum booster ativo! :(");
                    return;
                }

                sender.sendMessage("§fBooster ativo:\n§fTempo restante: §a" + profile.loadBoosterContainer().getBoosterTimeRemaining() + "\n§fMultiplicador: §a" + response.get("multiply") + "x");
                break;
            }
        }
    }
}
