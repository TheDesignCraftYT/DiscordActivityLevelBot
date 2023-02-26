package de.thedesigncraft.discord.bot.template.methods.manage.commands.types;

import de.thedesigncraft.discord.bot.template.methods.manage.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public interface ConsoleCommand {

    void code();

    @NotNull
    static ConcurrentHashMap<String, ConsoleCommand> getConsoleCommands() {

        ConcurrentHashMap<String, ConsoleCommand> returnMap = new ConcurrentHashMap<>();

        Manager.consoleCommands().forEach(consoleCommand -> {

            String name = consoleCommand.getClass().getSimpleName().replace("ConsoleCommand", "").toLowerCase();

            returnMap.put(name, consoleCommand);

        });

        return returnMap;

    }

}
