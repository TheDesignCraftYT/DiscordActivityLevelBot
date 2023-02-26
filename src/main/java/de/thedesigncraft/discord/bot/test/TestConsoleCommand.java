package de.thedesigncraft.discord.bot.test;

import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConfigurationCommandTemplate;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

public class TestConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        HashMap<String, String> test = new HashMap<>();

        try {

            ResultSetMetaData rsmd = SQLite.getData(Collections.singletonList("*"), "levels", null, null).getMetaData();

            for (int i = rsmd.getColumnCount(); i != 0; i--) {

                test.put(rsmd.getColumnName(i), rsmd.getColumnTypeName(i));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        test.forEach((columnName, columnType) -> {

            System.out.println(columnName + ": " + ConfigurationCommandTemplate.getOptionTypeFromSQLType(columnType));

        });

        System.out.println(ConfigurationCommandTemplate.getOptionTypeFromSQLType("asojdnaikjusnd"));

    }
}
