package tk.slicecollections.maxteer.database.cache.collections;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import tk.slicecollections.maxteer.achievements.Achievement;
import tk.slicecollections.maxteer.database.Database;
import tk.slicecollections.maxteer.database.cache.DataCollection;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.types.MySQL;
import tk.slicecollections.maxteer.titles.Title;

public class AchievementsInformation extends DataCollection {
    public AchievementsInformation(String playerKey) {
        super("achievements", "mCoreProfile", null, playerKey);
    }

    @Override
    public void setupColumn() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            if (!mySQL.existsColumn("mCoreProfile", "achievements")) {
                mySQL.addColumn("mCoreProfile", "achievements");
            }
        }
    }

    @Override
    public Object getDefaultValue() {
        return new JSONArray();
    }

    @Override
    public void saveValue() {
        DataTypes type = Database.getInstance().getType();
        if (type.equals(DataTypes.MYSQL)) {
            MySQL mySQL = ((MySQL) Database.getInstance());
            mySQL.updateColumn(this.getTableName(), this.getColumnName(), this.getValue(), "name = '" + this.getPlayerKey() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    public void addNewAchievement(Achievement achievement) throws ParseException {
        JSONArray currentAchievements = this.getAsJsonArray();
        currentAchievements.add(achievement.getId());
        this.updateValue(currentAchievements);
    }

    public void removeAchievement(Achievement achievement) throws ParseException {
        JSONArray currentAchievements = this.getAsJsonArray();
        currentAchievements.remove(achievement.getId());
        this.updateValue(currentAchievements);
    }

    public boolean hasAchievement(Achievement achievement) throws ParseException {
        JSONArray currentAchievements = this.getAsJsonArray();
        return currentAchievements.contains(String.valueOf(achievement.getId()));
    }
}
