package tk.slicecollections.maxteer.bungee.cmd;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import tk.slicecollections.maxteer.Manager;
import tk.slicecollections.maxteer.bungee.party.BungeeParty;
import tk.slicecollections.maxteer.bungee.party.BungeePartyManager;
import tk.slicecollections.maxteer.party.Party;
import tk.slicecollections.maxteer.party.PartyRole;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oJVzinn
 */
public class PartyCommand extends Commands {

    public PartyCommand() {
        super("party");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(" \n§3/p [mensagem] §f- §7Comunicar-se com os membros.\n§3/party abrir §f- §7Tornar a party pública.\n§3/party fechar §f- §7Tornar a party privada.\n§3/party entrar [jogador] §f- §7Entrar em uma party pública.\n§3/party aceitar [jogador] §f- §7Aceitar uma solicitação.\n§3/party ajuda §f- §7Mostrar essa mensagem de ajuda.\n§3/party convidar [jogador] §f- §7Convidar um jogador.\n§3/party deletar §f- §7Deletar a party.\n§3/party expulsar [jogador] §f- §7Expulsar um membro.\n§3/party info §f- §7Informações da sua Party.\n§3/party negar [jogador] §f- §7Negar uma solicitação.\n§3/party sair §f- §7Sair da Party.\n§3/party transferir [jogador] §f- §7Transferir a Party para outro membro.\n "));
            return;
        }

        BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
        String action = args[0];
        if (party != null) { //Em casos que a party exista
            if (Arrays.asList("negar", "aceitar", "entrar").contains(action)) {
                player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
                return;
            }

            switch (action.toLowerCase()) {
                case "abrir": {
                    if (!isLeader(party, player)) {
                        return;
                    }

                    if (party.isOpen()) {
                        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é pública."));
                        return;
                    }

                    party.setIsOpen(true);
                    player.sendMessage(TextComponent.fromLegacyText("§aVocê abriu a party para qualquer jogador."));
                    return;
                }

                case "fechar": {
                    if (!isLeader(party, player)) {
                        return;
                    }

                    if (!party.isOpen()) {
                        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é privada."));
                        return;
                    }

                    party.setIsOpen(false);
                    player.sendMessage(TextComponent.fromLegacyText("§cVocê fechou a party para apenas convidados."));
                    return;
                }

                case "deletar": {
                    if (!isLeader(party, player)) {
                        return;
                    }

                    party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §adeletou a Party!\n ", true);
                    party.delete();
                    player.sendMessage(TextComponent.fromLegacyText("§aVocê deletou a Party."));
                    return;
                }

                case "expulsar": {
                    if (args.length == 1) {
                        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party expulsar [jogador]"));
                        return;
                    }

                    if (!isLeader(party, player)) {
                        return;
                    }

                    String target = args[1];
                    if (target.equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode se expulsar."));
                        return;
                    }

                    if (!party.isMember(target)) {
                        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
                        return;
                    }

                    target = party.getName(target);
                    party.kick(target);
                    party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §aexpulsou " + Role.getPrefixed(target) + " §ada Party!\n ");
                    return;
                }

                case "info": {
                    List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != PartyRole.LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
                    player.sendMessage(TextComponent.fromLegacyText(
                            " \n§6Líder: " + Role.getPrefixed(party.getLeader()) + "\n§6Pública: " + (party.isOpen() ? "§aSim" : "§cNão") + "\n§6Limite de Membros: §f" + party.listMembers()
                                    .size() + "/" + party.getSlots() + "\n§6Membros: " + StringUtils.join(members, "§7, ") + "\n "));
                    return;
                }

                case "sair": {
                    party.leave(player.getName());
                    player.sendMessage(TextComponent.fromLegacyText("§aVocê saiu da Party!"));
                    return;
                }

                case "transferir": {
                    if (args.length == 1) {
                        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party transferir [jogador]"));
                        return;
                    }

                    if (!isLeader(party, player)) {
                        return;
                    }

                    String target = args[1];
                    if (target.equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode transferir a Party para você mesmo."));
                        return;
                    }

                    if (!party.isMember(target)) {
                        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
                        return;
                    }

                    target = party.getName(target);
                    party.transfer(target);
                    party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §atransferiu a liderança da Party para " + Role.getPrefixed(target) + "§a!\n ");
                    return;
                }
            }
        }

        if (action.length() > 1 && Arrays.asList("abrir", "fechar", "deletar", "expulsar", "info", "sair", "transferir").contains(action)) {
            player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
            return;
        }

        switch (action) {
            case "convidar": {
                if (args.length == 1) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party convidar [jogador]"));
                    return;
                }

                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
                    return;
                }

                invitePlayer(player, target, party);
                return;
            }

            case "entrar": {
                if (args.length == 1) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party entrar [jogador]"));
                    return;
                }

                String target = args[1];
                if (target.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode entrar na party de você mesmo."));
                    return;
                }

                party = BungeePartyManager.getLeaderParty(target);
                if (party == null) {
                    player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
                    return;
                }

                target = party.getName(target);
                if (!party.isOpen()) {
                    player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está fechada apenas para convidados."));
                    return;
                }

                if (canJoin(party)) {
                    player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
                    return;
                }

                party.join(player.getName());
                player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Role.getPrefixed(target) + "§a!\n "));
                return;
            }

            case "negar": {
                if (args.length == 1) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party negar [jogador]"));
                    return;
                }

                String target = args[1];
                if (target.equals(player.getName())) {
                    player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode negar convites de você mesmo."));
                    return;
                }

                party = BungeePartyManager.findPartyByPlayer(target);
                if (party == null) {
                    player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
                    return;
                }

                target = party.getName(target);
                if (!party.isInvited(player.getName())) {
                    player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
                    return;
                }

                party.reject(player.getName());
                player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê negou o convite de Party de " + Role.getPrefixed(target) + "§a!\n "));
                return;
            }

            case "aceitar": {
                if (args.length == 1) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party aceitar [jogador]"));
                    return;
                }

                String target = args[1];
                if (target.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode aceitar convites de você mesmo."));
                    return;
                }

                Party targetParty = BungeePartyManager.findPartyByPlayer(target);
                if (targetParty == null) {
                    player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não possui uma party ativa no momento."));
                    return;
                }

                target = targetParty.getName(target);
                if (!targetParty.isInvited(player.getName())) {
                    player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
                    return;
                }

                if (!canJoin(targetParty)) {
                    player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
                    return;
                }

                targetParty.join(player.getName());
                player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Role.getPrefixed(target) + "§a!\n "));
                return;
            }

            case "ajuda": {
                player.sendMessage(TextComponent.fromLegacyText(
                        " \n§3/p [mensagem] §f- §7Comunicar-se com os membros.\n§3/party abrir §f- §7Tornar a party pública.\n§3/party fechar §f- §7Tornar a party privada.\n§3/party entrar [jogador] §f- §7Entrar em uma party pública.\n§3/party aceitar [jogador] §f- §7Aceitar uma solicitação.\n§3/party ajuda §f- §7Mostrar essa mensagem de ajuda.\n§3/party convidar [jogador] §f- §7Convidar um jogador.\n§3/party deletar §f- §7Deletar a party.\n§3/party expulsar [jogador] §f- §7Expulsar um membro.\n§3/party info §f- §7Informações da sua Party.\n§3/party negar [jogador] §f- §7Negar uma solicitação.\n§3/party sair §f- §7Sair da Party.\n§3/party transferir [jogador] §f- §7Transferir a Party para outro membro.\n "));
                return;
            }

            default: {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
                if (target == null) {
                    player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
                    return;
                }

                invitePlayer(player, target, party);
                break;
            }
        }
    }

    private boolean canJoin(Party party) {
        return party.canJoin();
    }

    private boolean isLeader(Party party, ProxiedPlayer target) {
        target.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return party.isLeader(target.getName());
    }

    public void invitePlayer(ProxiedPlayer owner, ProxiedPlayer target, Party party) {
        if (target.getName().equalsIgnoreCase(owner.getName())) {
            owner.sendMessage(TextComponent.fromLegacyText("§cVocê não pode enviar convites para você mesmo."));
            return;
        }

        if (party == null) {
            party = BungeePartyManager.createParty(owner);
        }

        if (!isLeader(party, owner)) {
            return;
        }

        if (canJoin(party)) {
            owner.sendMessage(TextComponent.fromLegacyText("§cA sua Party está lotada."));
            return;
        }

        if (party.isInvited(target.getName())) {
            owner.sendMessage(TextComponent.fromLegacyText("§cVocê já enviou um convite para " + Manager.getCurrent(target.getName()) + "."));
            return;
        }

        if (BungeePartyManager.getMemberParty(target.getName()) != null) {
            owner.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target.getName()) + " já pertence a uma Party."));
            return;
        }

        party.invite(target);
        owner.sendMessage(
                TextComponent.fromLegacyText(" \n" + Role.getPrefixed(target.getName()) + " §afoi convidado para a Party. Ele tem 60 segundos para aceitar ou negar esta solicitação.\n "));
    }

}