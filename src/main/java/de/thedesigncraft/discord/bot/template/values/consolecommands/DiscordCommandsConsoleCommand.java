package de.thedesigncraft.discord.bot.template.values.consolecommands;

import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.consolecommands.ConsoleCommand;

public class DiscordCommandsConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        MainTemplate.jda.retrieveCommands().queue(commands -> commands.forEach(command -> System.out.println(command.getName())));

    }
}
