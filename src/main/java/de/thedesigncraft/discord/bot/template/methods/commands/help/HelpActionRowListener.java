package de.thedesigncraft.discord.bot.template.methods.commands.help;

import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.commands.help.methods.HelpActionRows;
import de.thedesigncraft.discord.bot.template.methods.commands.help.methods.HelpEmbeds;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.CommandManager;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IMessageContextMenu;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IUserContextMenu;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class HelpActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (ActionRows.proofButton(event, "help.goToCommand", user)) {

            String[] commandArgs = event.getButton().getId().split("&")[1].replace("command=", "").split("/");

            ISlashCommand slashCommand = CommandManager.slashCommandsMap.get(commandArgs[1]);

            IUserContextMenu userContextMenu = CommandManager.userContextMenuMap.get(commandArgs[1]);

            IMessageContextMenu messageContextMenu = CommandManager.messageContextMenuMap.get(commandArgs[1]);

            if (commandArgs.length == 2 && commandArgs[0].equals("")) {

                event.editMessageEmbeds(HelpEmbeds.slashCommand(slashCommand, event.getChannel())).setActionRow(HelpActionRows.command(slashCommand.category(), event.getUser().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("USER")) {

                event.editMessageEmbeds(HelpEmbeds.userCommand(userContextMenu)).setActionRow(HelpActionRows.command(userContextMenu.category(), event.getMember().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("MESSAGE")) {

                event.editMessageEmbeds(HelpEmbeds.messageCommand(messageContextMenu)).setActionRow(HelpActionRows.command(messageContextMenu.category(), event.getMember().getIdLong())).queue();

            } else {

                event.editMessageEmbeds(EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.\n\nWenn dies passiert, melde es bitte umgehend dem Entwickler des Bots.", false)).queue();

            }

        } else if (ActionRows.proofButton(event, "help.goToCategory", user)) {

            String arg = event.getButton().getId().split("&")[1].replace("category=", "");

            event.editMessageEmbeds(HelpEmbeds.category(arg)).setComponents(HelpActionRows.category(arg, user.getIdLong(), event.getGuild())).queue();

        } else if (ActionRows.proofButton(event, "help.goToMainPage", user)) {

            event.editMessageEmbeds(HelpEmbeds.mainPage()).setComponents(HelpActionRows.mainPage(user)).queue();

        }

    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {

        if (ActionRows.proofSelectMenu(event, "help.goToCategory", event.getUser())) {

            String arg = event.getSelectedOptions().get(0).getLabel();

            event.editMessageEmbeds(HelpEmbeds.category(arg)).setComponents(HelpActionRows.category(arg, event.getUser().getIdLong(), event.getGuild())).queue();

        }

    }
}
