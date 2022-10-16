package de.thedesigncraft.discord.bot.template.methods.manage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class SQLite {

    private static Connection connection;

    private static Statement statement;

    public static void connect() {

        connection = null;

        try {

            File database = new File("database.db");

            if (!database.exists())
                database.createNewFile();

            connection = DriverManager.getConnection("jdbc:sqlite:" + database.getPath());
            Logger logger = LoggerFactory.getLogger(SQLite.class);
            logger.info("StatusUpdate: Online");

            statement = connection.createStatement();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void disconnect() {

        try {

            if (connection != null) {

                connection.close();
                Logger logger = LoggerFactory.getLogger(SQLite.class);
                logger.info("StatusUpdate: Offline");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void onUpdate(@NotNull String sql) {

        try {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    @NotNull
    public static ResultSet onQuery(@NotNull String sql) {

        try {

            return statement.executeQuery(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public static ResultSet getData(@NotNull List<String> values, @NotNull String table, @Nullable String whereValueName, @Nullable String whereValueValue) {

        String valuesToSelect = values.toString().replace("[", "").replace("]", "");

        if (whereValueName != null && whereValueValue != null) {

            return onQuery("SELECT " + valuesToSelect + " FROM " + table + " WHERE " + whereValueName + " = " + whereValueValue);

        } else {

            return onQuery("SELECT " + valuesToSelect + " FROM " + table);

        }

    }

    public static void updateData(@NotNull String table, @NotNull String valueName, @NotNull String valueValue, @NotNull String whereValueName, @NotNull String whereValueValue) {

        onUpdate("UPDATE " + table + " SET " + valueName + " = " + valueValue + " WHERE " + whereValueName + " = " + whereValueValue);

    }

    public static void setData(@NotNull String table, @NotNull List<String> valueNames, @NotNull List<String> valueValues) {

        String valueNamesString = valueNames.toString().replace("[", "").replace("]", "");

        String valueValuesString = valueValues.toString().replace("[", "").replace("]", "");

        onUpdate("INSERT INTO " + table + "(" + valueNamesString + ") VALUES(" + valueValuesString + ")");

    }

    public static void createTable(@NotNull String name, @NotNull String values) {

        onUpdate("CREATE TABLE IF NOT EXISTS " + name + "(" + values + ")");

    }

}
