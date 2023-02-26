package de.thedesigncraft.discord.bot.commands.levelsystem;

import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.jetbrains.annotations.NotNull;

public interface LevelSystemEmbeds {

    @NotNull
    static MessageEmbed mainPage(@NotNull Emoji commandEmoji) {

        return EmbedTemplates.standardEmbed(commandEmoji.getName() + " LevelSystem Einstellungen", "In diesem Menü kannst du alles wichtige zum LevelSystem einstellen. Wähle einfach unten im SelectMenu ein Level aus, dass du bearbeiten möchtest.");

    }

    @NotNull
    static MessageEmbed levelSettings(@NotNull StringSelectInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed("'" + event.getSelectedOptions().get(0).getLabel() + "' Einstellungen", "In diesem Embed kannst du Einstellungen an dem Level '" + event.getSelectedOptions().get(0).getLabel() + "' vornehmen. In den einzelnen Feldern unten siehst du die aktuellen Einstellungen dieses Levels. Um eine Einstellung zu ändern, musst du nur auf den jeweiligen Button unter diesem Embed klicken. Wenn du fertig bist, klicke einfach auf den Fertig-Button unten."));

        embedBuilder.addField("Nachrichten-Mindestanzahl", "```" + Levels.levelMins(event.getGuild()).get(Integer.parseInt(event.getSelectedOptions().get(0).getValue())) + "```", true);
        embedBuilder.addField("Name", "```" + Levels.levelStrings(event.getGuild()).get(Integer.parseInt(event.getSelectedOptions().get(0).getValue())) + "```", true);

        return embedBuilder.build();

    }

    @NotNull
    static MessageEmbed editValue(@NotNull String buttonId) {

        // 0: "levelSystem" | 1: "settings" | 2: index | 3: type
        String[] args = buttonId.split("&id=")[0].replace(".", "III").split("III");

        return EmbedTemplates.standardEmbed("Einstellungen: '" + args[3] + "'", "Antworte einfach auf diese Nachricht mit dem Wert, den du festlegen möchtest. Wenn du fertig bist, klicke einfach unten auf 'Fertig'.");

    }
}
