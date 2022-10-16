package de.thedesigncraft.discord.bot.constants.methods;

import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Levels {

    @NotNull
    static ArrayList<String> levelStrings(@NotNull Guild guild) {

        ArrayList<String> values = new ArrayList<>();

        values.add("number");
        values.add("name");

        ResultSet resultSet = SQLite.getData(values, "levels", "guildid", guild.getId());

        ArrayList<String> names = new ArrayList<>();

        while (true) {
            try {
                if (!resultSet.next()) break;

                int number = resultSet.getInt("number");
                String name = resultSet.getString("name");

                names.add(number, name);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return names;

    }

    @NotNull
    static ArrayList<Integer> levelMins(@NotNull Guild guild) {

        ArrayList<String> values = new ArrayList<>();

        values.add("number");
        values.add("minvalue");

        ResultSet resultSet = SQLite.getData(values, "levels", "guildid", guild.getId());

        ArrayList<Integer> names = new ArrayList<>();

        while (true) {
            try {
                if (!resultSet.next()) break;

                int number = resultSet.getInt("number");
                int minvalue = resultSet.getInt("minvalue");

                names.add(number, minvalue);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return names;

    }

    @NotNull
    static String getLevelEmoji(int level) {

        if (level == 1)
            return "<:lvl1:1030455318687268894>";

        if (level == 2)
            return "<:lvl2:1030455320046211083>";

        if (level == 3)
            return "<:lvl3:1030455317076652092>";

        if (level == 4)
            return "<:lvl4:1030455315893854208>";

        if (level == 5)
            return "<:lvl5:1030455314694295562>";

        if (level == 6)
            return "<:lvl6:1030455313373073428>";

        if (level == 7)
            return "<:lvl7:1030455312144150619>";

        if (level == 8)
            return "<:lvl8:1030455310537728061>";

        if (level == 9)
            return "<:lvl9:1030455309287825478>";

        if (level == 10)
            return "<:lvl10:1030455307975008367>";

        return String.valueOf(level);

    }

    @NotNull
    static ArrayList<String> standardLevelStrings() {

        ArrayList<String> returnList = new ArrayList<>();

        returnList.add("Level 1");
        returnList.add("Level 2");
        returnList.add("Level 3");
        returnList.add("Level 4");
        returnList.add("Level 5");
        returnList.add("Level 6");
        returnList.add("Level 7");
        returnList.add("Level 8");
        returnList.add("Level 9");
        returnList.add("Level 10");

        return returnList;

    }

    @NotNull
    static ArrayList<Integer> standardLevelMins() {

        ArrayList<Integer> returnList = new ArrayList<>();

        returnList.add(1);
        returnList.add(5);
        returnList.add(10);
        returnList.add(25);
        returnList.add(50);
        returnList.add(100);
        returnList.add(150);
        returnList.add(200);
        returnList.add(500);
        returnList.add(1000);

        return returnList;

    }

    static int getLevel(int amount, @NotNull Guild guild) {

        for (int i = 0; i != (levelMins(guild).size() - 1); i++) {

            int minValue = levelMins(guild).get(i);

            if (amount >= minValue && amount < levelMins(guild).get(i + 1))
                return i;

        }

        if (amount == 0)
            return 0;

        return -1;

    }

}
