package de.thedesigncraft.discord.bot.template.values;

import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;

public interface SQLManager {

    static void onCreate() {

        // SQLite.createTable("name", "value TYPE, value TYPE");
        SQLite.createTable("messages", "guildid INTEGER, userid INTEGER, timestamp INTEGER");
        SQLite.createTable("levels", "guildid INTEGER, number INTEGER, minvalue INTEGER, name STRING");

    }

}
