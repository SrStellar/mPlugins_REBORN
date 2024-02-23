package tk.slicecollections.maxteer.titles;

import com.comphenix.packetwrapper.WrapperPlayServerAttachEntity;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.reflection.Accessors;
import tk.slicecollections.maxteer.reflection.MinecraftReflection;
import tk.slicecollections.maxteer.reflection.acessors.FieldAccessor;

import java.util.stream.Collectors;

public class TitleController {

    private static final FieldAccessor<Integer> ENTITY_ID = Accessors.getField(MinecraftReflection.getEntityClass(), "entityCount", int.class);

    @Getter
    private Player owner;
    private WrappedDataWatcher watcher;
    private boolean disabled = true;
    private final int entityId;

    public TitleController(Player owner, String title) {
        this.owner = owner;
        this.watcher = new WrappedDataWatcher();
        this.watcher.setObject(0, (byte) 0x20);
        this.watcher.setObject(2, title);
        this.watcher.setObject(3, (byte) 1);
        this.watcher.setObject(5, (byte) 1);
        this.watcher.setObject(10, (byte) 1);
        this.entityId = ENTITY_ID.get(null);
        ENTITY_ID.set(null, this.entityId + 1);
    }

    public void setName(String name) {
        if (this.watcher.getString(2).equals("disabled")) {
            this.watcher.setObject(2, name);
            Profile.listProfiles().forEach(profile -> {
                Player player = profile.getPlayer();
                if (player != null && player.canSee(this.owner)) {
                    showToPlayer(player);
                }
            });
            return;
        }

        this.watcher.setObject(2, name);
        if (name.equals("disabled")) {
            Profile.listProfiles().forEach(profile -> {
                Player player = profile.getPlayer();
                if (player != null && player.canSee(this.owner)) {
                    this.hideToPlayer(player);
                }
            });
            return;
        }

        WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
        metadata.setEntityId(this.entityId);
        metadata.setEntityMetadata(this.watcher.getWatchableObjects());

        Profile.listProfiles().forEach(profile -> {
            Player player = profile.getPlayer();
            if (player != null && player.canSee(this.owner)) {
                metadata.sendPacket(player);
            }
        });
    }

    public void destroy() {
        this.disable();
        this.owner = null;
        this.watcher = null;
    }

    public void enable() {
        if (!this.disabled) {
            return;
        }

        this.disabled = false;
        Profile.listProfiles().forEach(profile -> {
            Player player = profile.getPlayer();
            if (player != null && player.canSee(this.owner)) {
                this.showToPlayer(player);
            }
        });
    }

    public void disable() {
        if (this.disabled) {
            return;
        }

        Profile.listProfiles().forEach(profile -> {
            Player player = profile.getPlayer();
            if (player != null && player.canSee(this.owner)) {
                this.hideToPlayer(player);
            }
        });
        this.disabled = true;
    }

    void showToPlayer(Player player) {
        if (player.equals(this.owner)) {
            return;
        }

        if (!this.disabled && !this.watcher.getString(2).equals("disabled")) {
            WrapperPlayServerSpawnEntityLiving spawn = new WrapperPlayServerSpawnEntityLiving();
            spawn.setType(EntityType.ARMOR_STAND);
            spawn.setEntityID(this.entityId);
            spawn.setMetadata(this.watcher);
            spawn.setX(this.owner.getLocation().getX());
            spawn.setY(this.owner.getLocation().getY());
            spawn.setZ(this.owner.getLocation().getZ());

            WrapperPlayServerAttachEntity attach = new WrapperPlayServerAttachEntity();
            attach.setEntityId(this.entityId);
            attach.setVehicleId(this.owner.getEntityId());

            spawn.sendPacket(player);
            attach.sendPacket(player);
        }
    }

    public void updateLocation() {
        if (!this.disabled && !this.watcher.getString(2).equals("disabled")) {
            WrapperPlayServerSpawnEntityLiving spawn = new WrapperPlayServerSpawnEntityLiving();
            spawn.setEntityID(this.entityId);
            spawn.setMetadata(this.watcher);
            spawn.setX(this.owner.getLocation().getX());
            spawn.setY(this.owner.getLocation().getY());
            spawn.setZ(this.owner.getLocation().getZ());

            WrapperPlayServerAttachEntity attach = new WrapperPlayServerAttachEntity();
            attach.setEntityId(this.entityId);
            attach.setVehicleId(this.owner.getEntityId());

            for (Player online : Profile.listProfiles().stream().filter(profile -> profile.getGame() == null && profile.getPlayer() != this.owner).collect(Collectors.toList()).stream().map(Profile::getPlayer).collect(Collectors.toList())) {
                if (online != null) {
                    spawn.sendPacket(online);
                    attach.sendPacket(online);
                }
            }
        }
    }

    void hideToPlayer(Player player) {
        if (player.equals(this.owner)) {
            return;
        }

        if (!this.disabled) {
            WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
            destroy.setEntities(new int[] {this.entityId});

            destroy.sendPacket(player);
        }
    }
}
