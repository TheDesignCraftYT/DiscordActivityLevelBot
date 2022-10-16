package de.thedesigncraft.discord.bot.template.methods.listeners;

import de.thedesigncraft.discord.bot.Main;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DevCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.PRIVATE))
            return;

        if (MainValues.owners().contains(event.getChannel().asPrivateChannel().getUser())) {

            if (event.getMessage().getContentDisplay().startsWith("dev.")) {

                executeCommand(event, event.getMessage().getContentDisplay().replaceFirst("dev.", ""));

            }

        }

    }

    private void executeCommand(@NotNull MessageReceivedEvent event, @NotNull String command) {

        if (command.startsWith("sendNewUpdateMessage")) {

            sendNewUpdateMessage(event);

        } else if (command.startsWith("test")) {

            test(event);

        } else if (command.startsWith("shutdown")) {

            shutdown(event);

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein registrierter DevCommand.", false)).queue();

        }

    }

    private void shutdown(@NotNull MessageReceivedEvent event) {

        JDA jda = MainTemplate.jda;

        if (jda != null) {

            System.out.println("-------------------------");
            System.out.println(MainValues.name + ": Fahre herunter...");
            System.out.println("-------------------------");

            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            event.getMessage().reply("`StatusUpdate: Offline`").queue();
            jda.shutdown();
            SQLite.disconnect();
            Logger logger = LoggerFactory.getLogger(Main.class);
            logger.info("StatusUpdate: Offline");
            System.exit(0);

        } else {

            Logger logger = LoggerFactory.getLogger(Main.class);
            logger.error("JDA ist null");

        }

    }

    private void test(@NotNull MessageReceivedEvent event) {

        event.getMessage().reply("Test").queue();

    }

    private void sendNewUpdateMessage(@NotNull MessageReceivedEvent event) {

        try {

            String arg = event.getMessage().getContentDisplay().split(" ")[1];

            String version = Versions.versions().get(arg);

            NewUpdateListener.sendNewUpdate(version, false);

        } catch (ArrayIndexOutOfBoundsException e) {

            NewUpdateListener.sendNewUpdate(Versions.currentVersion(), false);

        }

    }

}
