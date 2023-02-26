package de.thedesigncraft.discord.bot.template.methods.manage;

import de.thedesigncraft.discord.bot.DiscordActivityLevelBot;
import de.thedesigncraft.discord.bot.constants.values.TOKEN;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.CommandManager;
import de.thedesigncraft.discord.bot.template.values.Loop;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import de.thedesigncraft.discord.bot.template.values.SQLManager;
import de.thedesigncraft.discord.bot.template.values.StartBotValues;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static JDA jda;

    public static Thread loop;

    public static CommandManager serverCmdMan;

    public Main() throws LoginException {

        SQLite.connect();
        SQLManager.onCreate();

        try {

            jda = JDABuilder
                    .createDefault(Main.getToken()[1])
                    .setStatus(OnlineStatus.ONLINE)
                    .setActivity(Activity.listening("/help"))
                    .addEventListeners(Manager.eventListeners())
                    .enableIntents(StartBotValues.intents())
                    .enableCache(StartBotValues.cachFlags())
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .build();

            jda.awaitReady().addEventListener(serverCmdMan = new CommandManager());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger logger = LoggerFactory.getLogger(Versions.class);
        logger.info("BotVersion: '" + Versions.currentVersion() + "'");
        logger.info("Starting '" + Main.getToken()[0] + "' Bot.");

        Logger logger1 = LoggerFactory.getLogger(DiscordActivityLevelBot.class);
        logger1.info("StatusUpdate: Online");

        Loop.onLoop();

        consoleCommands();
        runLoop();

    }

    @NotNull
    public static String[] getToken() {

        try {

            String versionType = Versions.currentVersion().split("-")[1].replace(".", "III").split("III")[0];

            if (versionType.equals("alpha")) {

                return new String[]{"Alpha", TOKEN.alphaToken};

            } else if (versionType.equals("beta")) {

                return new String[]{"Beta", TOKEN.betaToken};

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            return new String[]{"Release", TOKEN.releaseToken};

        }

        return null;

    }

    private void consoleCommands() {

        new Thread(() -> {

            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            try {

                while ((line = bufferedReader.readLine()) != null) {

                    ConsoleCommand consoleCommand;

                    if ((consoleCommand = ConsoleCommand.getConsoleCommands().get(line)) != null) {

                        System.out.println("--------------------");

                        consoleCommand.code();

                        System.out.println("--------------------");

                    } else {

                        System.out.println("--------------------");
                        System.out.println(MainValues.name + ": Das ist kein registrierter Befehl.");
                        System.out.println("Fuer eine Liste an Befehlen, fuehre 'commands' aus.");
                        System.out.println("--------------------");

                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }

    public void runLoop() {

        loop = new Thread(() -> {

            long time = System.currentTimeMillis();

            while (true) {

                int i = Loop.interval * 1000;

                if (System.currentTimeMillis() >= time + i) {

                    time = System.currentTimeMillis();

                    Loop.onLoop();

                }

            }

        });

        loop.setName("Loop");
        loop.start();

    }

}
