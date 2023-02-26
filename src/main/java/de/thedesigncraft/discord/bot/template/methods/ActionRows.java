package de.thedesigncraft.discord.bot.template.methods;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

public interface ActionRows {

    @NotNull
    static Button cancelButton(User user) {

        return Button.of(ButtonStyle.SECONDARY, "cancel&id=" + user.getId(), "Abbrechen");

    }

    static boolean proofButton(ButtonInteractionEvent buttonInteractionEvent, String proofActionRowId, User proofActionRowUser) {

        String[] actionRowIdTiles = buttonInteractionEvent.getButton().getId().replace("&id=", "III").split("III");

        if (actionRowIdTiles[0].split("&")[0].equals(proofActionRowId)) {

            if (proofActionRowUser.getId().equals(actionRowIdTiles[1])) {

                return true;

            } else {

                buttonInteractionEvent.replyEmbeds(EmbedTemplates.issueEmbed("Nur **<@" + actionRowIdTiles[1] + ">** hat Zugriff auf diesen Button.", false)).setEphemeral(true).queue();

            }

        }

        return false;

    }

    static boolean proofSelectMenu(StringSelectInteractionEvent event, String proofActionRowId, User proofActionRowUser) {

        String[] actionRowIdTiles = event.getSelectMenu().getId().replace("&id=", "III").split("III");

        if (actionRowIdTiles[0].split("&")[0].equals(proofActionRowId)) {

            if (proofActionRowUser.getId().equals(actionRowIdTiles[1])) {

                return true;

            } else {

                event.replyEmbeds(EmbedTemplates.issueEmbed("Nur **<@" + actionRowIdTiles[1] + ">** hat Zugriff auf diesen Button.", false)).setEphemeral(true).queue();

            }

        }

        return false;

    }

}
