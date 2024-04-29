package tk.slicecollections.maxteer.bungee.proxiedplayer.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.channels.Channel;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.channels.collections.ProfileInfoChannel;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container.ProxiedContainer;
import tk.slicecollections.maxteer.bungee.proxiedplayer.cache.container.collections.ProfileInfoContainer;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProxiedCache {

    private final List<Channel> CHANNELS = new ArrayList<>();
    private final List<ProxiedContainer> CONTAINERS = new ArrayList<>();

    @NonNull
    @Getter
    private String name;

    public void load() {
        CONTAINERS.add(new ProfileInfoContainer());
        CHANNELS.add(new ProfileInfoChannel(this));
    }

    public Channel findChannelByName(String channelName) {
        return CHANNELS.stream().filter(channel -> channel.getName().equalsIgnoreCase(channelName)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Channel> T findChannel(Class<T> clazz) {
        return (T) CHANNELS.stream().filter(channel -> channel.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    public ProxiedContainer findContainerByName(String name) {
        return CONTAINERS.stream().filter(channel -> channel.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ProxiedContainer> T findContainer(Class<T> clazz) {
        return (T) CONTAINERS.stream().filter(container -> container.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    public List<Channel> listChannels() {
        return CHANNELS;
    }

    public List<ProxiedContainer> listContainers() {
        return CONTAINERS;
    }
}
