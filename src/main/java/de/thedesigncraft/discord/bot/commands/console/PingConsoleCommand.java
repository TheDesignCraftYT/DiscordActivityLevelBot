package de.thedesigncraft.discord.bot.commands.console;

import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.values.MainValues;

import static de.thedesigncraft.discord.bot.template.methods.manage.Main.jda;

public class PingConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        System.out.println(MainValues.name + ": Bot Status:");
        System.out.println("Interne Berechnungen:");
        System.out.println("| " + jda.getResponseTotal() + "ms");
        System.out.println("Discord API Gateway:");
        System.out.println("| " + jda.getGatewayPing() + "ms");
        System.out.println("Discord API REST:");
        System.out.println("| " + jda.getRestPing().complete() + "ms");

    }
}
