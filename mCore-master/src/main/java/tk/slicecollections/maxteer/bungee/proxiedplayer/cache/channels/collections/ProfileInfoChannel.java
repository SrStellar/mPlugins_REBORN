package tk.slicecollections.maxteer.bungee.proxiedplayer.cache.channels.collections;

import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.ProxiedCache;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.channels.Channel;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container.ProxiedContainer;

public class ProfileInfoChannel extends Channel {

    public ProfileInfoChannel(ProxiedCache cache) {
        super("updateProfileInfo", cache);
        this.register();
    }

    @Override
    public void action(Object value) {
        ProxiedCache cache = this.getCache();
        cache.findContainerByName("profileinfo").updateValue(value);
    }

}
