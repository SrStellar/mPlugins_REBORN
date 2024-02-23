package tk.slicecollections.maxteer.deliveries;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import tk.slicecollections.maxteer.player.Profile;

public class DeliveryReward {

    private RewardType type;
    private Object[] values;

    public DeliveryReward(String reward) {
        if (reward == null) {
            reward = "";
        }

        String[] splitter = reward.split(">");
        RewardType type = RewardType.from(splitter[0]);
        if (type == null || reward.replace(splitter[0] + ">", "").split(":").length < type.getParameters()) {
            this.type = RewardType.COMANDO;
            this.values = new Object[]{"tell {name} §cPrêmio \"" + reward + "\" inválido!"};
            return;
        }

        this.type = type;
        try {
            this.values = type.parseValues(reward.replace(splitter[0] + ">", ""));
        } catch (Exception ex) {
            ex.printStackTrace();
            this.type = RewardType.COMANDO;
            this.values = new Object[]{"tell {name} §cPrêmio \"" + reward + "\" inválido!"};
        }
    }

    @SneakyThrows
    public void dispatch(Profile profile) {
        switch (this.type) {
            case COMANDO: {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ((String) this.values[0]).replace("{name}", profile.getName()));
                break;
            }

            case CASH: {
                profile.getCashManager().addCash((long) this.values[0]);
                break;
            }
        }
    }
}
