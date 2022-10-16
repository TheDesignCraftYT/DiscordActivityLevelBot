package de.thedesigncraft.discord.bot.template.values;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public interface CommandCategories {

    @NotNull
    static ConcurrentHashMap<String, String> categories() {

        ConcurrentHashMap<String, String> returnList = new ConcurrentHashMap<>();

        returnList.put("misc", "Nützliches");
        returnList.put("configuration", "Einstellungen");
        returnList.put("info", "Informationen");

        return returnList;

    }

}
