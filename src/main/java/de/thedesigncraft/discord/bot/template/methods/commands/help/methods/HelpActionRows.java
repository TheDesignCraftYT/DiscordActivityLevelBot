package de.thedesigncraft.discord.bot.template.methods.commands.help.methods;

import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import de.thedesigncraft.discord.bot.template.methods.manage.Manager;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.ICommandMethods;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IMessageContextMenu;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IUserContextMenu;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public interface HelpActionRows {

    static List<ActionRow> category(String category, long userId, Guild guild) {
        List<Button> buttons = new ArrayList<>();

        List<ISlashCommand> categoryISlashCommands = new ArrayList<>();
        List<IUserContextMenu> categoryUserCommands = new ArrayList<>();
        List<IMessageContextMenu> categoryMessageCommands = new ArrayList<>();

        Manager.slashCommands().forEach(serverCommand -> {

            if (serverCommand.category().equals(category))
                categoryISlashCommands.add(serverCommand);

        });

        Manager.userContextMenus().forEach(userContextMenu -> {

            if (userContextMenu.category().equals(category))
                categoryUserCommands.add(userContextMenu);

        });

        Manager.messageContextMenus().forEach(messageContextMenu -> {

            if (messageContextMenu.category().equals(category))
                categoryMessageCommands.add(messageContextMenu);

        });

        categoryISlashCommands.forEach(serverCommand -> buttons.add(Button.of(ButtonStyle.SUCCESS, "help.goToCommand&command=/" + ICommandMethods.getSlashCommandName(serverCommand) + "&id=" + userId, "/" + ICommandMethods.getSlashCommandName(serverCommand), serverCommand.commandEmoji())));

        categoryUserCommands.forEach(userContextMenu -> buttons.add(Button.of(ButtonStyle.SUCCESS, "help.goToCommand&command=USER/" + ICommandMethods.getUserContextMenuName(userContextMenu) + "&id=" + userId, "USER/" + ICommandMethods.getUserContextMenuName(userContextMenu), userContextMenu.commandEmoji())));

        categoryMessageCommands.forEach(messageContextMenu -> buttons.add(Button.of(ButtonStyle.SUCCESS, "help.goToCommand&command=MESSAGE/" + ICommandMethods.getMessageContextMenuName(messageContextMenu) + "&id=" + userId, "MESSAGE/" + ICommandMethods.getMessageContextMenuName(messageContextMenu), messageContextMenu.commandEmoji())));

        List<ActionRow> returnList = new ArrayList<>();

        if (buttons.size() > 5) {

            returnList.add(ActionRow.of(buttons.subList(0, 5)));

            if (buttons.size() == 6) {

                returnList.add(ActionRow.of(buttons.get(5)));

            } else if (buttons.size() > 6 && buttons.size() <= 10) {

                returnList.add(ActionRow.of(buttons.subList(6, buttons.size())));

            } else if (buttons.size() == 11) {

                returnList.add(ActionRow.of(buttons.get(10)));

            } else if (buttons.size() > 11 && buttons.size() <= 15) {

                returnList.add(ActionRow.of(buttons.subList(6, 10)));
                returnList.add(ActionRow.of(buttons.subList(11, buttons.size())));

            }

        } else {

            returnList.add(ActionRow.of(buttons));

        }

        returnList.add(ActionRow.of(Button.of(ButtonStyle.SECONDARY, "help.goToMainPage&id=" + userId, "Zurück", Emoji.fromUnicode("U+25c0"))));

        return returnList;

    }

    static Button command(String category, long userId) {

        return Button.of(ButtonStyle.SECONDARY, "help.goToCategory&category=" + category + "&id=" + userId, "Zurück", Emoji.fromUnicode("U+25c0"));

    }

    static SelectMenu allCategories(User user) {

        List<SelectOption> selectOptions = new ArrayList<>();

        CommandCategories.categories().forEach((s, s2) -> selectOptions.add(SelectOption.of(s2, s)));

        return StringSelectMenu.create("help.goToCategory&id=" + user.getId()).addOptions(selectOptions).setMaxValues(1).setPlaceholder("Welche Kategorie möchtest du sehen?").build();

    }

    static List<ActionRow> mainPage(User user) {

        List<ActionRow> returnList = new ArrayList<>();
        returnList.add(ActionRow.of(allCategories(user)));
        returnList.add(ActionRow.of(ActionRows.cancelButton(user)));

        return returnList;

    }

}
