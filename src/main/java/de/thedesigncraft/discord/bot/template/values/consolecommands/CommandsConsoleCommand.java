package de.thedesigncraft.discord.bot.template.values.consolecommands;

import de.thedesigncraft.discord.bot.template.methods.manage.consolecommands.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.consolecommands.ConsoleCommands;
import de.thedesigncraft.discord.bot.template.values.MainValues;

public class CommandsConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        System.out.println(MainValues.name + ": Hier ist eine Liste von allen Konsolbefehlen: ");

        ConsoleCommands.consoleCommands().forEach((s, consoleCommand) -> System.out.println("| - " + s));

    }
}
