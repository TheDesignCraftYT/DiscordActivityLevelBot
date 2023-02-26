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
        returnList.add(new String[]{Versions.versions().get("v100a1"), "BotBio", "Erste Version der Bio."});
        returnList.add(new String[]{Versions.versions().get("v100a2"), "BotLevel", "Das Levelsystem gilt nun nur noch für normale Nutzer. Bots sind nun ausgeschlossen."});
        returnList.add(new String[]{Versions.versions().get("v100a2"), "/level", "Fehler bei der Anzeige behoben."});

        return returnList;

    }

}
