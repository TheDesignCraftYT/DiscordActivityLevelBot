package de.thedesigncraft.discord.bot.commands.console;

import de.thedesigncraft.discord.bot.DiscordActivityLevelBot;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import net.dv8tion.jda.api.OnlineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        if (Main.jda != null) {

            System.out.println(MainValues.projectName + ": Fahre herunter...");

            System.out.println("--------------------");

            Main.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            Main.jda.shutdown();
            SQLite.disconnect();
            Logger logger = LoggerFactory.getLogger(DiscordActivityLevelBot.class);
            logger.info("StatusUpdate: Offline");
            System.exit(0);

        } else {

            System.out.println("JDA ist null.");

        }

        if (Main.loop != null)
            Main.loop.interrupt();


    }
}
