package tk.slicecollections.maxteer.database;

import lombok.Getter;
import tk.slicecollections.maxteer.Manager;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.interfaces.DatabaseInterface;
import tk.slicecollections.maxteer.database.types.MySQL;

public class Database {

    @Getter
    private static DatabaseInterface instance;

    public static void setupDatabase(DataTypes types, String... configs) {
        try {
            if (types.equals(DataTypes.MYSQL)) {
                instance = new MySQL(configs[0], configs[1], configs[2], configs[3], true);
            }
            instance.setupConnection();
        } catch (Exception e) {
            Manager.sendMessageToConsole("§4Ocorreu um erro enquanto nós conectavamos ao database...");
            e.printStackTrace();
            System.exit(0);
        }
    }

}
