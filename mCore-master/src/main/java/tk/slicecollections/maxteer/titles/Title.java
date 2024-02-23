package tk.slicecollections.maxteer.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.collections.TitleInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.utils.BukkitUtils;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Title {

    protected static final Map<String, TitleController> SELECTED_TITLES = new HashMap<>();

    private Long id;
    private String icon;
    private String title;

    public ItemStack getIcon(Profile profile) {
        String material = has(profile) ? isSelected(profile) ? TitlesConfig.MATERIAL.getValueSelected() : TitlesConfig.MATERIAL.getValueHas() : TitlesConfig.MATERIAL.getValueNoHas();
        String durability = has(profile) ? TitlesConfig.DURABILITY.getValueSelected() : TitlesConfig.DURABILITY.getValueNoHas();
        String color = has(profile) ? isSelected(profile) ? TitlesConfig.COLOR.getValueSelected() : TitlesConfig.COLOR.getValueHas() : TitlesConfig.COLOR.getValueNoHas();
        String action = has(profile) ? isSelected(profile) ? TitlesConfig.ACTION_TITLE.getValueSelected() : TitlesConfig.ACTION_TITLE.getValueHas() : TitlesConfig.ACTION_TITLE.getValueNoHas();
        return BukkitUtils.deserializeItemStack(this.icon.replace("{material}", material).replace("{durability}", durability).replace("{color}", color).replace("{action}", action));
    }

    public void setSelected(Profile profile) {
        TitleController controller = new TitleController(profile.getPlayer(), this.title);
        controller.enable();
        if (SELECTED_TITLES.containsKey(profile.getName())) {
            destroy(profile);
        }

        SELECTED_TITLES.put(profile.getName(), controller);
    }

    public void destroy(Profile profile) {
        if (SELECTED_TITLES.containsKey(profile.getName())) {
            SELECTED_TITLES.get(profile.getName()).destroy();
            SELECTED_TITLES.remove(profile.getName());
        }
    }

    public void setDisable(Profile profile) {
        TitleController controller = new TitleController(profile.getPlayer(), this.title);
        controller.disable();
    }

    @SneakyThrows
    public boolean isSelected(Profile profile) {
        Long titleID = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle();
        return titleID.equals(this.id);
    }

    @SneakyThrows
    public void addTitleToPlayer(Profile profile) {
        TitleInformation cache = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(TitleInformation.class);
        cache.addNewTitle(this);
    }

    @SneakyThrows
    public void removeTitleToPlayer(Profile profile) {
        TitleInformation cache = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(TitleInformation.class);
        cache.removeTitle(this);
    }

    public boolean has(Profile profile) {
        try {
            return profile.getPlayer().hasPermission("mcore.titles." + title.toLowerCase()) || profile.getCache().loadTableCache(ProfileCache.class).loadCollection(TitleInformation.class).hasTitle(this);
        } catch (ParseException e) {
            return false;
        }
    }
}
