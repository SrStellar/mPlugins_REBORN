package tk.slicecollections.maxteer.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Property;
import tk.slicecollections.maxteer.bungee.Bungee;
import tk.slicecollections.maxteer.bungee.party.BungeeParty;
import tk.slicecollections.maxteer.bungee.party.BungeePartyManager;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.preferences.PreferenceEnum;
import tk.slicecollections.maxteer.utils.StringUtils;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Maxter
 */
public class Listeners implements Listener {

    private static final Map<String, Property[]> PROPERTY_BACKUP = new HashMap<>();

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent evt) {
    }

    @EventHandler
    public void onPostLogin(PreLoginEvent evt) {
        String playerName = evt.getConnection().getName();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent evt) {
        if (evt.getSender() instanceof ServerConnection && evt.getReceiver() instanceof ProxiedPlayer) {
            if (evt.getTag().equalsIgnoreCase("mCore")) {
                ProxiedPlayer player = (ProxiedPlayer) evt.getReceiver();

                ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
                String subChannel = in.readUTF();
                if (subChannel.equalsIgnoreCase("FAKE_SKIN")) {
                    LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
                    if (profile != null) {
                        try {
                            String[] data = in.readUTF().split(":");
                            PROPERTY_BACKUP.put(player.getName().toLowerCase(), profile.getProperties());
                            this.modifyProperties(profile, data);
                        } catch (Exception ex) {
                            Property[] properties = PROPERTY_BACKUP.remove(player.getName().toLowerCase());
                            if (properties != null) {
                                profile.setProperties(properties);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = (byte) 128)
    public void onServerConnected(ServerConnectedEvent evt) {
        ProxiedPlayer player = evt.getPlayer();

        BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
        if (party != null) {
            party.sendData(evt.getServer().getInfo());
        }

        if (Bungee.isFake(player.getName())) {
            String skin = Bungee.getSkin(player.getName());
            // Enviar dados desse jogador que está utilizando Fake para o servidor processar.
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FAKE");
            out.writeUTF(player.getName());
            out.writeUTF(Bungee.getFake(player.getName()));
            out.writeUTF(StringUtils.stripColors(Bungee.getRole(player.getName()).getName()));
            out.writeUTF(skin);
            evt.getServer().sendData("mCore", out.toByteArray());

            LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
            if (profile != null) {
                this.modifyProperties(profile, skin.split(":"));
            }
        }
    }

    @EventHandler(priority = (byte) 128)
    public void onChat(ChatEvent evt) {
        if (evt.getSender() instanceof ProxiedPlayer) {
            if (evt.isCommand()) {
                ProxiedPlayer player = (UserConnection) evt.getSender();
                String[] args = evt.getMessage().replace("/", "").split(" ");
                String command = args[0];
                if (ProxyServer.getInstance().getPluginManager().getCommands().stream().anyMatch(stringCommandEntry ->
                        stringCommandEntry.getValue().getName().equals("tell")) && args.length > 1 && command.equals("tell") && !args[1]
                        .equalsIgnoreCase(player.getName())) {
                    if (!this.canReceiveTell(args[1].toLowerCase())) {
                        evt.setCancelled(true);
                        player.sendMessage(TextComponent.fromLegacyText("§cEste usuário desativou as mensagens privadas."));
                    }
                }
            }
        }
    }

    private void modifyProperties(LoginResult profile, String[] data) {
        List<Property> properties = new ArrayList<>();
        for (Property property : profile.getProperties() == null ? new ArrayList<Property>() : Arrays.asList(profile.getProperties())) {
            if (property.getName().equalsIgnoreCase("textures")) {
                continue;
            }

            properties.add(property);
        }

        properties.add(new Property("textures", data[0], data[1]));
        profile.setProperties(properties.toArray(new Property[0]));
    }

    private boolean canReceiveTell(String name) {
        Profile profile = Profile.loadProfile(name);
        if (profile == null) {
            return true;
        }

        return true;
    }

}