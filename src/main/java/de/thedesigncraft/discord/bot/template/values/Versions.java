package de.thedesigncraft.discord.bot.template.values;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Versions {

    @NotNull
    static List<String> versionNames() {

        List<String> returnList = new ArrayList<>();

        // Versionen zur returnList hinzufügen
        returnList.add("v1.0.0-alpha.1");

        return returnList;

    }

}
