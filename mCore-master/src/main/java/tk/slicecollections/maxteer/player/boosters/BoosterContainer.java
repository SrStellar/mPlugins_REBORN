package tk.slicecollections.maxteer.player.boosters;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.boosters.Booster;
import tk.slicecollections.maxteer.boosters.BoosterType;
import tk.slicecollections.maxteer.boosters.NetworkManager;
import tk.slicecollections.maxteer.database.cache.ContainerAbstract;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class BoosterContainer extends ContainerAbstract {

    public BoosterContainer(Profile profile) {
        super(profile, ProfileCache.class);
    }

    @Override
    public void load() {}

    @SneakyThrows
    public JSONObject getBoosterActivated() {
        if (!hasBoosterActivated()) {
            return null;
        }

        return (JSONObject) new JSONParser().parse(loadProfileInformation().getInformation("booster").toString());
    }

    @SneakyThrows
    public void activateBooster(Booster booster) {
        if (hasBoosterActivated()) {
            return;
        }

        if (booster.getBoosterType() == BoosterType.PRIVATE) {
            JSONObject response = new JSONObject();
            response.put("time", System.currentTimeMillis() + booster.getTimeMinutes());
            response.put("multiply", booster.getMultiply());
            loadProfileInformation().updateValue("booster", response);
        } else {
            NetworkManager.activateBooster(Core.minigame, booster.getTimeMinutes(), booster.getMultiply(), Role.findRoleByPermission(this.getProfile().getPlayer()).getRoleColor() + this.getProfile().getName());
        }

        removeBooster(booster.getId());
    }

    public String getBoosterTimeRemaining() {
        if (!hasBoosterActivated()) {
            return "0s";
        }

        long totalTime = (Long) getBoosterActivated().get("time") - System.currentTimeMillis();
        return TimeUtils.getTime(totalTime, true);
    }

    @SneakyThrows
    public boolean hasBoosterActivated() {
        JSONObject boosterActivated = loadProfileInformation().getInformation("booster");
        if (boosterActivated.isEmpty()) {
            return false;
        }

        return ((Long) boosterActivated.get("time") - System.currentTimeMillis()) / 1000 >= 0; //Acabou o booster
    }

    @SneakyThrows
    public void addBooster(BoosterType type, Long timeMinutes, Double multiply) {
        JSONObject boosters = loadBoostersJSON();
        long ID = generateID();
        boosters.put(ID, multiply + ":" + TimeUnit.MINUTES.toMillis(timeMinutes) + ":" + type.getID());
        updateInformation(boosters);
    }

    @SneakyThrows
    public void removeBooster(Long ID) {
        JSONObject boosters = loadBoostersJSON();
        boosters.remove(ID.toString());
        updateInformation(boosters);
    }

    @SneakyThrows
    public List<Booster> listAllBoosters() {
        JSONObject boosters = loadBoostersJSON();
        List<Booster> response = new ArrayList<>();
        for (Object boosterObj : boosters.keySet()) {
            Long boosterID = Long.valueOf(boosterObj.toString());

            String boosterConfig = boosters.get(boosterObj).toString();
            Double multiply = Double.valueOf(boosterConfig.split(":")[0]);
            Long time = Long.valueOf(boosterConfig.split(":")[1]);
            Long boosterTypeID = Long.valueOf(boosterConfig.split(":")[2]);
            response.add(new Booster(boosterID, time, multiply, BoosterType.findByID(boosterTypeID)));
        }

        return response;
    }

    @SneakyThrows
    public List<Booster> listAllBoosters(BoosterType type) {
        return listAllBoosters().stream().filter(booster -> booster.getBoosterType().equals(type)).collect(Collectors.toList());
    }

    private ProfileInformation loadProfileInformation() {
        return loadCollection(ProfileInformation.class);
    }

    private JSONObject loadBoostersJSON() throws ParseException {
        return (JSONObject) loadProfileInformation().getAsJsonObject().get("boosters");
    }

    @SneakyThrows
    private void updateInformation(JSONObject object) {
        loadProfileInformation().updateValue("boosters", object);
    }

    private Long generateID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(new Random().nextInt(10));
        }

        while (listAllBoosters().stream().anyMatch(booster -> booster.getId().toString().equals(booster.toString()))) { //Caso haja algum ID igual
            builder.delete(0, 5);
             for (int i = 0; i < 6; i++) {
                builder.append(new Random().nextInt(10));
            }
        }

        return Long.valueOf(builder.toString());
    }
}
