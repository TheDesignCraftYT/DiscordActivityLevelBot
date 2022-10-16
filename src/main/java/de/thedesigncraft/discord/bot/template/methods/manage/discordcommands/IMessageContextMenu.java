package de.thedesigncraft.discord.bot.template.methods.manage.discordcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMessageContextMenu {

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

    @NotNull
    Emoji commandEmoji();

    @Nullable
    List<Permission> requiredPermissions();

    void performMessageContextMenu(@NotNull MessageContextInteractionEvent event);

}
