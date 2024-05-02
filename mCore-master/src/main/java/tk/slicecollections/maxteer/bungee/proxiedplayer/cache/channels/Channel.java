package tk.slicecollections.maxteer.bungee.proxiedplayer.cache.channels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.slicecollections.maxteer.bungee.Bungee;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.ProxiedCache;

@AllArgsConstructor
@Getter
public abstract class Channel {

    private String name;
    private ProxiedCache cache;

    public void register() {
        Bungee.getInstance().getProxy().registerChannel(this.name);
    }

    public abstract void action(Object value);
}
