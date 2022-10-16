package de.thedesigncraft.discord.bot.listeners;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedEventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        if (event.getAuthor().equals(MainTemplate.jda.getSelfUser()))
            return;

        ActivityMethods.addNewMessage(event.getMessage());

        int messageCount = ActivityMethods.getMessages(event.getMember());

        int level = Levels.getLevel(messageCount, event.getGuild());

        if (level != Levels.getLevel(messageCount - 1, event.getGuild())) {

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(Levels.getLevelEmoji(level + 1) + " LevelUp");
            embedBuilder.setColor(0x2F3136);
            embedBuilder.setDescription("Du hast nun **" + messageCount + "** Nachrichten in der letzten Woche geschrieben und bist somit auf dem Level **" + (level + 1) + " (" + Levels.levelStrings(event.getGuild()).get(level) + ")**. Herzlichen Glückwunsch! <a:tada_animated:1030459209780838430>");

            try {
                event.getMessage().replyEmbeds(embedBuilder.build()).queue();
            } catch (IndexOutOfBoundsException ignored) {
            }

        }

    }

}
