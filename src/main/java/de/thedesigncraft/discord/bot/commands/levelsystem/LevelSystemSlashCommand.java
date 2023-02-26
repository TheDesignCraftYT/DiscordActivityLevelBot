package de.thedesigncraft.discord.bot.commands.levelsystem;

import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LevelSystemSlashCommand implements ISlashCommand {
    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public @NotNull String description() {
        return "Öffnet das Einstellungsmenü des Levelsystems.";
    }

    @Override
    public @NotNull String category() {
        return CommandCategories.categories().get("configuration");
    }

    @Override
    public boolean globalCommand() {
        return true;
    }

    @Override
    public @Nullable List<Guild> guilds() {
        return null;
    }

    @Override
    public boolean guildOnly() {
        return true;
    }

    @Override
    public @Nullable List<OptionData> options() {
        return null;
    }

    @Override
    public @NotNull Emoji commandEmoji() {
        return Emoji.fromUnicode("U+2699");
    }

    @Override
    public @Nullable List<Permission> requiredPermissions() {
        return Collections.singletonList(Permission.ADMINISTRATOR);
    }

    @Override
    public void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        event.replyEmbeds(LevelSystemEmbeds.mainPage(commandEmoji())).setComponents(LevelSystemActionRows.mainPage(event.getMember())).queue();

    }
}
