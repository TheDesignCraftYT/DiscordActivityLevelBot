package de.thedesigncraft.discord.bot.template.methods.commands;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VersionSlashCommand implements ISlashCommand {

    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public @NotNull String description() {
        return "Gibt die aktuelle Version des Bots zurück.";
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
        return Emoji.fromUnicode("U+1F916");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Bot Version", "Unten siehst du die aktuelle Version des Bots."));
        embedBuilder.addField("Version", Versions.currentVersion(), true);
        embedBuilder.addField("VersionType", Main.getToken()[0], true);

        event.replyEmbeds(embedBuilder.build()).queue();

    }

}
