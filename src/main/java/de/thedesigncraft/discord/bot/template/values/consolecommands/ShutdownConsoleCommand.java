package de.thedesigncraft.discord.bot.template.values.consolecommands;

import de.thedesigncraft.discord.bot.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import de.thedesigncraft.discord.bot.template.methods.manage.consolecommands.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import net.dv8tion.jda.api.OnlineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        if (MainTemplate.jda != null) {

            System.out.println(MainValues.projectName + ": Fahre herunter...");

            System.out.println("--------------------");

            MainTemplate.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            MainTemplate.jda.shutdown();
            SQLite.disconnect();
            Logger logger = LoggerFactory.getLogger(Main.class);
            logger.info("StatusUpdate: Offline");
            System.exit(0);

        } else {

            System.out.println("JDA ist null.");

        }

        if (MainTemplate.loop != null)
            MainTemplate.loop.interrupt();


    }
}
