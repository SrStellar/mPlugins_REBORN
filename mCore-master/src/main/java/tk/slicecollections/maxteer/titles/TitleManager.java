package tk.slicecollections.maxteer.titles;

import org.bukkit.Bukkit;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.database.cache.collections.SelectedInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleManager {

    private static final List<Title> TITLES_CACHE = new ArrayList<>();

    public static void setupTitles() {
        Arrays.stream(Titles.values()).forEach(titles -> TITLES_CACHE.add(new Title(titles.getId(), titles.getIcon(), titles.getTitle())));
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Core.getInstance(), ()-> Title.SELECTED_TITLES.keySet().forEach(key -> Title.SELECTED_TITLES.get(key).updateLocation()), 0L, 10L);
    }

    public static void joinLobby(Profile profile) {
        try {
            Long titleID = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle();
            if (titleID != -1) {
                Title title = findByID(titleID);
                title.setSelected(profile);
                Title.SELECTED_TITLES.values().forEach(titleController -> {
                    if (!titleController.getOwner().equals(profile.getPlayer())) {
                        titleController.showToPlayer(profile.getPlayer());
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Title getTitleSelected(Profile profile) {
        return TITLES_CACHE.stream().filter(title -> title.isSelected(profile)).findFirst().orElse(null);
    }

    public static void joinGame(Profile profile) {
        try {
            Long titleID = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle();
            if (titleID != -1) {
                Title title = findByID(titleID);
                title.setDisable(profile);
                Title.SELECTED_TITLES.values().forEach(titleController -> {
                    if (!titleController.getOwner().equals(profile.getPlayer())) {
                        titleController.hideToPlayer(profile.getPlayer());
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void leaveServer(Profile profile) {
        try {
            Long titleID = profile.getCache().loadTableCache(ProfileCache.class).loadCollection(SelectedInformation.class).getSelectedTitle();
            if (titleID != -1) {
                Title title = findByID(titleID);
                title.destroy(profile);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Title findByID(Long id) {
        return TITLES_CACHE.stream().filter(title -> title.getId().equals(id)).findFirst().orElse(null);
    }

    public static List<Title> listTiles() {
        return TITLES_CACHE;
    }
}
