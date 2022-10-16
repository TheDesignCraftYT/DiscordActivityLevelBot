package de.thedesigncraft.discord.bot.template.methods.manage.consolecommands;

import de.thedesigncraft.discord.bot.template.methods.manage.Manager;

import java.util.concurrent.ConcurrentHashMap;

public interface ConsoleCommands {

    static ConcurrentHashMap<String, ConsoleCommand> consoleCommands() {

        ConcurrentHashMap<String, ConsoleCommand> returnMap = new ConcurrentHashMap<>();

        Manager.consoleCommands().forEach(consoleCommand -> {

            String name = consoleCommand.getClass().getSimpleName().replace("ConsoleCommand", "").toLowerCase();

            returnMap.put(name, consoleCommand);

        });

        return returnMap;

    }

}
