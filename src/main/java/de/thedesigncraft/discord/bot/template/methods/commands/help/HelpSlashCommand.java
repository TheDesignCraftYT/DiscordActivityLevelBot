package de.thedesigncraft.discord.bot.template.methods.commands.help;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.commands.help.methods.HelpActionRows;
import de.thedesigncraft.discord.bot.template.methods.commands.help.methods.HelpEmbeds;
import de.thedesigncraft.discord.bot.template.methods.manage.Manager;
import de.thedesigncraft.discord.bot.template.methods.manage.discordcommands.*;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HelpSlashCommand implements ISlashCommand {

    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public @NotNull String description() {
        return "Zeigt das Help-Menü des Bots an.";
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
        List<OptionData> options = new ArrayList<>();
        OptionData command = new OptionData(OptionType.STRING, "command", "Welchen Command möchtest du öffnen?", false);

        Manager.slashCommands().forEach(slashCommand -> command.addChoice("/" + ICommandMethods.getSlashCommandName(slashCommand), "/" + ICommandMethods.getSlashCommandName(slashCommand)));
        Manager.userContextMenus().forEach(userContextMenu -> command.addChoice("USER/" + ICommandMethods.getUserContextMenuName(userContextMenu), "USER/" + ICommandMethods.getUserContextMenuName(userContextMenu)));
        Manager.messageContextMenus().forEach(messageContextMenu -> command.addChoice("MESSAGE/" + ICommandMethods.getMessageContextMenuName(messageContextMenu), "MESSAGE/" + ICommandMethods.getMessageContextMenuName(messageContextMenu)));

        options.add(command);
        OptionData category = new OptionData(OptionType.STRING, "category", "Welche Kategorie möchtest du öffnen?", false);
        CommandCategories.categories().forEach(category::addChoice);
        options.add(category);
        return options;
    }

    @Override
    public @NotNull Emoji commandEmoji() {
        return Emoji.fromUnicode("U+2139");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        if (event.getOption("category") == null && event.getOption("command") == null) {

            event.replyEmbeds(HelpEmbeds.mainPage()).addComponents(HelpActionRows.mainPage(event.getUser())).queue();

        } else if (event.getOption("category") != null && event.getOption("command") != null) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du kannst **entweder** einen Command **oder** eine Kategorie angeben.", false)).setEphemeral(true).queue();

        } else if (event.getOption("category") != null && event.getOption("command") == null) {

            String arg2 = event.getOption("category").getAsString();

            event.replyEmbeds(HelpEmbeds.category(arg2)).addComponents(HelpActionRows.category(arg2, event.getMember().getIdLong(), event.getGuild())).queue();

        } else if (event.getOption("category") == null && event.getOption("command") != null) {

            String[] commandArgs = event.getOption("command").getAsString().split("/");

            ISlashCommand slashCommand = CommandManager.slashCommandsMap.get(commandArgs[1]);

            IUserContextMenu userContextMenu = CommandManager.userContextMenuMap.get(commandArgs[1]);

            IMessageContextMenu messageContextMenu = CommandManager.messageContextMenuMap.get(commandArgs[1]);

            if (commandArgs.length == 2 && commandArgs[0].equals("")) {

                event.replyEmbeds(HelpEmbeds.slashCommand(slashCommand, event.getChannel())).addActionRow(HelpActionRows.command(slashCommand.category(), event.getUser().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("USER")) {

                event.replyEmbeds(HelpEmbeds.userCommand(userContextMenu)).addActionRow(HelpActionRows.command(userContextMenu.category(), event.getUser().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("MESSAGE")) {

                event.replyEmbeds(HelpEmbeds.messageCommand(messageContextMenu)).addActionRow(HelpActionRows.command(messageContextMenu.category(), event.getUser().getIdLong())).queue();

            } else {

                event.replyEmbeds(EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.\n\nWenn dies passiert, melde es bitte umgehend dem Entwickler des Bots.", false)).queue();

            }

        } else {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein gültiger Wert.\n\nDu kannst Command-Namen und Kategorie-Namen angeben.", false)).setEphemeral(true).queue();

        }

    }
}
