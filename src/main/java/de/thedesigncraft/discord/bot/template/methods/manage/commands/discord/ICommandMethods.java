package de.thedesigncraft.discord.bot.template.methods.manage.commands.discord;

import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IMessageContextMenu;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IUserContextMenu;
import org.jetbrains.annotations.NotNull;

public interface ICommandMethods {

    static String getSlashCommandName(@NotNull ISlashCommand serverCommand) {

        return serverCommand.getClass().getSimpleName().split("SlashCommand")[0].toLowerCase();

    }

    static String getUserContextMenuName(@NotNull IUserContextMenu IUserContextMenu) {

        return IUserContextMenu.getClass().getSimpleName().split("UserContextMenu")[0].toLowerCase();

    }

    static String getMessageContextMenuName(@NotNull IMessageContextMenu IMessageContextMenu) {

        return IMessageContextMenu.getClass().getSimpleName().split("MessageContextMenu")[0].toLowerCase();

    }

}
