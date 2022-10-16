package de.thedesigncraft.discord.bot.template.values;

import org.jetbrains.annotations.NotNull;
import de.thedesigncraft.discord.bot.template.methods.Versions;

import java.util.ArrayList;
import java.util.List;

public interface UpdateFunctions {

    @NotNull
    static List<String[]> updateFunctions() {

        List<String[]> returnList = new ArrayList<>();

        // returnList.add(new String[]{Versions.versions().get("VERSION_NAME"), "FUNCTION_NAME", "FUNCTION_DESCRIPTION"});
        returnList.add(new String[]{Versions.versions().get("v100a1"), "LevelSystem", "Das grundlegende System des Bots."});

        return returnList;

    }

}
