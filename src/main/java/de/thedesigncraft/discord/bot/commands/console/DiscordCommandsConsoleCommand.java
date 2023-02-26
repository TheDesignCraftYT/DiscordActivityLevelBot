package de.thedesigncraft.discord.bot.commands.console;

import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;

public class DiscordCommandsConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        Main.jda.retrieveCommands().queue(commands -> commands.forEach(command -> System.out.println(command.getName())));

    }
}
