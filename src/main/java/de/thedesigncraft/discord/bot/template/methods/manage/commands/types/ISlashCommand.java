package de.thedesigncraft.discord.bot.template.methods.manage.commands.types;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ISlashCommand {

    @NotNull
    String version();

    @NotNull
    String description();

    @NotNull
    String category();

    boolean globalCommand();

    @Nullable
    List<Guild> guilds();

    boolean guildOnly();

    @Nullable
    List<OptionData> options();

    @NotNull
    Emoji commandEmoji();

    @Nullable
    List<Permission> requiredPermissions();

    void performSlashCommand(@NotNull SlashCommandInteractionEvent event);

}
