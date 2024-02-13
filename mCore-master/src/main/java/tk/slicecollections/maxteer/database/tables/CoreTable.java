package tk.slicecollections.maxteer.database.tables;

import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.HikariDatabase;
import tk.slicecollections.maxteer.database.MySQLDatabase;
import tk.slicecollections.maxteer.database.data.DataContainer;
import tk.slicecollections.maxteer.database.data.DataTable;
import tk.slicecollections.maxteer.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Maxter
 */
@DataTableInfo(
    name = "mCoreProfile",
    create = "CREATE TABLE IF NOT EXISTS `mCoreProfile` (`name` VARCHAR(32), `cash` LONG, `role` TEXT, `deliveries` TEXT, `preferences` TEXT, `titles` TEXT, `boosters` TEXT, `achievements` TEXT, `selected` TEXT, `created` LONG, `lastlogin` LONG, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `mCoreProfile` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `mCoreProfile` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `mCoreProfile` SET `cash` = ?, `role` = ?, `deliveries` = ?, `preferences` = ?, `titles` = ?, `boosters` = ?, `achievements` = ?, `selected` = ?, `created` = ?, `lastlogin` = ? WHERE LOWER(`name`) = ?"
    )
public class CoreTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `mCoreProfile` LIKE 'cash'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `mCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `mCoreProfile` LIKE 'cash'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `mCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("deliveries", new DataContainer("{}"));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"pl\": 0}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("boosters", new DataContainer("{}"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{\"title\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    return defaultValues;
  }
}
