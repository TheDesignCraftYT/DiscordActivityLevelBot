package de.thedesigncraft.discord.bot.template.methods.commands;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.discordcommands.ISlashCommand;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PingSlashCommand implements ISlashCommand {

    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public @NotNull String description() {
        return "Zeigt dir den Ping des Bots an.";
    }

    @Override
    public @NotNull String category() {
        return CommandCategories.categories().get("misc");
    }

    @Override
    public boolean globalCommand() {
        return true;
    }

    @Override
    public List<Guild> guilds() {
        return null;
    }

    @Override
    public boolean guildOnly() {
        return false;
    }

    @Override
    public List<OptionData> options() {
        return null;
    }

    @Override
    public @NotNull Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F3D3");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ping", ""));

        embedBuilder.addField("Discord API Gateway:", "```" + MainTemplate.jda.getGatewayPing() + "ms```", true);
        embedBuilder.addField("Discord API REST:", "```" + MainTemplate.jda.getRestPing().complete() + "ms```", true);

        event.replyEmbeds(embedBuilder.build()).queue();

    }

}
