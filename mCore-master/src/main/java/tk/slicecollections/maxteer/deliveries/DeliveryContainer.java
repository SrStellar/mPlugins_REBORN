package tk.slicecollections.maxteer.deliveries;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.database.cache.ContainerAbstract;
import tk.slicecollections.maxteer.database.cache.collections.ProfileInformation;
import tk.slicecollections.maxteer.database.cache.types.ProfileCache;
import tk.slicecollections.maxteer.player.Profile;

@SuppressWarnings("unchecked")
public class DeliveryContainer extends ContainerAbstract {

    public DeliveryContainer(Profile profile) {
        super(profile, ProfileCache.class);
    }

    @Override
    public void load() {}

    @SneakyThrows
    public void claimDelivery(long id, long time) {
        JSONObject deliveries = loadDeliveryJSON();
        deliveries.put(String.valueOf(id), System.currentTimeMillis() + time);
        updateInformation(deliveries);
        deliveries.clear();
    }

    @SneakyThrows
    public long getClaimTime(long id) {
        JSONObject deliveries = loadDeliveryJSON();
        return deliveries.containsKey(String.valueOf(id)) ? (long) deliveries.get(String.valueOf(id)) : 0;
    }

    public boolean alreadyClaimed(long id) {
        return this.getClaimTime(id) > System.currentTimeMillis();
    }

    private ProfileInformation loadProfileInformation() {
        return loadCollection(ProfileInformation.class);
    }

    private JSONObject loadDeliveryJSON() throws ParseException {
        return (JSONObject) loadProfileInformation().getAsJsonObject().get("deliveries");
    }

    @SneakyThrows
    public void updateInformation(JSONObject object) {
        loadProfileInformation().updateValue("deliveries", object);
    }
}
