package de.thedesigncraft.discord.bot.template.methods.listeners;

import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.OneSelectionMenuCommandTemplate;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StandardActionRowListener extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {

        OneSelectionMenuCommandTemplate.onStringSelectInteraction(event);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Ein SelectMenu mit einer unbekannten Id wurde benutzt.", false));
            embedBuilder.addField("SelectMenuId", "```" + Objects.requireNonNull(event.getSelectMenu().getId()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Unbekanntes SelectMenu");

            MainValues.owners().forEach(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue()));
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        OneSelectionMenuCommandTemplate.onButtonInteraction(event);

        if (ActionRows.proofButton(event, "cancel", user)) {

            if (event.getMessage().getMessageReference() != null) {

                event.getMessage().getMessageReference().resolve().queue(message -> message.delete().queue());

            }

            event.getMessage().delete().queue();

        } else {

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {
            }

            if (!event.isAcknowledged()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Ein Button mit einer unbekannten Id wurde gedrückt.", false));
                embedBuilder.addField("ButtonId", "```" + Objects.requireNonNull(event.getButton().getId()).split("&id=")[0] + "```", true);
                embedBuilder.addField("User", "```" + user.getAsTag() + "```", true);

                if (event.isFromGuild()) {

                    embedBuilder.addField("Server", "```" + event.getGuild().getName() + "```", true);

                } else {

                    embedBuilder.addField("DM User", "```" + event.getUser().getName() + "```", true);

                }

                Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
                logger.error("Unbekannter Button");

                MainValues.owners().forEach(user1 -> user1.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue()));
                event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

            }

        }

    }
}
