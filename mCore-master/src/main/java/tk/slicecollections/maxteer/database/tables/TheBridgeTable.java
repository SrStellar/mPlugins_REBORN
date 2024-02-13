package tk.slicecollections.maxteer.database.tables;

import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.HikariDatabase;
import tk.slicecollections.maxteer.database.MySQLDatabase;
import tk.slicecollections.maxteer.database.data.DataContainer;
import tk.slicecollections.maxteer.database.data.DataTable;
import tk.slicecollections.maxteer.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Maxter
 */
@DataTableInfo(name = "mCoreTheBridge",
    create = "CREATE TABLE IF NOT EXISTS `mCoreTheBridge` (`name` VARCHAR(32), `1v1kills` LONG, `1v1deaths` LONG, `1v1games` LONG, `1v1points` LONG, `1v1wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2games` LONG, `2v2points` LONG, `2v2wins` LONG, `coins` DOUBLE, `winstreak` LONG, `laststreak` LONG, `lastmap` LONG, `hotbar` TEXT, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `mCoreTheBridge` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `mCoreTheBridge` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `mCoreTheBridge` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1games` = ?, `1v1points` = ?, `1v1wins` = ?, `2v2kills` = ?, `2v2deaths` = ?, `2v2games` = ?, `2v2points` = ?, `2v2wins` = ?, `coins` = ?, `winstreak` = ?, `laststreak` = ?, `lastmap` = ?, `hotbar` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?")
public class TheBridgeTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `mCoreTheBridge` LIKE 'hotbar'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `mCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `mCoreTheBridge` LIKE 'hotbar'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `mCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("1v1kills", new DataContainer(0L));
    defaultValues.put("1v1deaths", new DataContainer(0L));
    defaultValues.put("1v1games", new DataContainer(0L));
    defaultValues.put("1v1points", new DataContainer(0L));
    defaultValues.put("1v1wins", new DataContainer(0L));
    defaultValues.put("2v2kills", new DataContainer(0L));
    defaultValues.put("2v2deaths", new DataContainer(0L));
    defaultValues.put("2v2games", new DataContainer(0L));
    defaultValues.put("2v2points", new DataContainer(0L));
    defaultValues.put("2v2wins", new DataContainer(0L));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("winstreak", new DataContainer(0L));
    defaultValues.put("laststreak", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("hotbar", new DataContainer("{}"));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
