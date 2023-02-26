package de.thedesigncraft.discord.bot.commands.console;

import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.values.MainValues;

public class CommandsConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        System.out.println(MainValues.name + ": Hier ist eine Liste von allen Konsolbefehlen: ");

        ConsoleCommand.getConsoleCommands().forEach((s, consoleCommand) -> System.out.println("| - " + s));

    }
}
