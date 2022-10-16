package de.thedesigncraft.discord.bot.template.methods;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public interface Versions {

    @NotNull
    static ConcurrentHashMap<String, String> versions() {

        ConcurrentHashMap<String, String> returnMap = new ConcurrentHashMap<>();

        de.thedesigncraft.discord.bot.template.values.Versions.versionNames().forEach(s -> {

            if (s.replace(".", "III").split("III")[2].endsWith("alpha")) {

                returnMap.put(s.replace("lpha", "").replace(".", "").replace("-", ""), s);

            } else if (s.replace(".", "III").split("III")[2].endsWith("beta")) {

                returnMap.put(s.replace("eta", "").replace(".", "").replace("-", ""), s);

            } else {

                returnMap.put(s.replace(".", ""), s);

            }

        });

        return returnMap;

    }

    @NotNull
    static String currentVersion() {

        return de.thedesigncraft.discord.bot.template.values.Versions.versionNames().get(de.thedesigncraft.discord.bot.template.values.Versions.versionNames().size() - 1);

    }

}
