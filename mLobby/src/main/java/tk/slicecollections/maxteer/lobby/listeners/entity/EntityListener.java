package tk.slicecollections.maxteer.lobby.listeners.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.player.Profile;

public class EntityListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityDamage(EntityDamageEvent evt) {
    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();

      Profile profile = Profile.loadProfile(player.getName());
      if (profile != null) {
        Game<?> game = profile.getGame();
        if (game == null) {
          evt.setCancelled(true);
          if (evt.getCause() == DamageCause.VOID) {
            player.teleport(Core.getLobby());
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCreatureSpawn(CreatureSpawnEvent evt) {
    evt.setCancelled(evt.getSpawnReason() != SpawnReason.CUSTOM);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFoodLevelChange(FoodLevelChangeEvent evt) {
    evt.setCancelled(true);
  }
}
