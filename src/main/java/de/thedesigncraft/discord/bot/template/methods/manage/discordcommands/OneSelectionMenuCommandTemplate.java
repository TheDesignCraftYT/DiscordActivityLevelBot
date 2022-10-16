package de.thedesigncraft.discord.bot.template.methods.manage.discordcommands;

import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface OneSelectionMenuCommandTemplate extends ISlashCommand {

    @NotNull
    MessageEmbed mainPage();

    // 0: CommandName | 1: OptionName | 2: OptionDescription
    @NotNull
    String[] settings();

    @NotNull
    ConcurrentHashMap<String, String[]> choices();

    @NotNull
    MessageEmbed choiceEmbed(@NotNull String name, @NotNull String[] args);

    @NotNull
    default List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        List<Command.Choice> choices = new ArrayList<>();

        choices().forEach((s, strings) -> choices.add(new Command.Choice(s, s)));

        returnList.add(new OptionData(OptionType.STRING, settings()[1], settings()[2]).addChoices(choices));

        return returnList;

    }

    default void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        if (event.getOption(settings()[1]) != null) {

            String arg = event.getOption(settings()[1]).getAsString();

            ConcurrentHashMap<String, String[]> targets = targets(choices(), arg);

            String key = targets.keySet().stream().findFirst().get();

            event.replyEmbeds(replyEmbed(arg, targets, settings(), mainPage(), choiceEmbed(key, targets.get(key)))).addComponents(actionRow(event.getUser(), arg, choices(), settings())).queue();

        } else {

            event.replyEmbeds(replyEmbed(null, targets(choices(), null), settings(), mainPage(), null)).addComponents(actionRow(event.getUser(), null, choices(), settings())).queue();

        }

    }

    @NotNull
    static MessageEmbed replyEmbed(@Nullable String arg, @NotNull ConcurrentHashMap<String, String[]> targets, @NotNull String[] settings, @NotNull MessageEmbed mainPage, @Nullable MessageEmbed choiceEmbed) {

        if (targets.size() > 1)
            return EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.", false);

        if (targets.size() == 0) {

            if (arg != null) {

                return EmbedTemplates.issueEmbed("Das ist kein registrierter Wert für die Option '" + settings[1] + "'.", false);

            } else {

                return mainPage;

            }

        }

        assert choiceEmbed != null;
        return choiceEmbed;

    }

    static List<ActionRow> actionRow(@NotNull User user, @Nullable String arg, @NotNull ConcurrentHashMap<String, String[]> choices, @NotNull String[] settings) {

        ConcurrentHashMap<String, String[]> targets = targets(choices, arg);

        if (targets.size() > 1)
            return Collections.singletonList(ActionRow.of(ActionRows.cancelButton(user)));

        if (targets.size() == 0) {

            if (arg != null) {

                return Collections.singletonList(ActionRow.of(ActionRows.cancelButton(user)));

            } else {

                List<ActionRow> returnList = new ArrayList<>();

                List<SelectOption> options = new ArrayList<>();

                choices.forEach((s, strings) -> options.add(SelectOption.of(s, s)));

                returnList.add(ActionRow.of(SelectMenu.create(settings[0] + "Menu&id=" + user.getIdLong()).addOptions(options).build()));

                returnList.add(ActionRow.of(ActionRows.cancelButton(user)));

                return returnList;

            }

        }

        return Collections.singletonList(ActionRow.of(Button.of(ButtonStyle.SECONDARY, settings[0] + ".goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+25c0"))));

    }

    static ConcurrentHashMap<String, String[]> targets(@NotNull ConcurrentHashMap<String, String[]> choices, @Nullable String arg) {

        ConcurrentHashMap<String, String[]> targets = new ConcurrentHashMap<>();

        choices.forEach((s, strings) -> {

            if (s.equalsIgnoreCase(arg)) {

                targets.put(s, strings);

            }

        });

        return targets;

    }

    static void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        ISlashCommand serverCommand = CommandManager.slashCommandsMap.get(event.getButton().getId().replace(".goToMainPage", "").toLowerCase().split("&id=")[0]);

        try {

            OneSelectionMenuCommandTemplate oneSelectionMenu = (OneSelectionMenuCommandTemplate) serverCommand;

            if (oneSelectionMenu != null && ActionRows.proofButton(event, event.getButton().getId().split("&id=")[0], event.getUser())) {

                event.editMessageEmbeds(oneSelectionMenu.mainPage()).setComponents(actionRow(event.getUser(), null, oneSelectionMenu.choices(), oneSelectionMenu.settings())).queue();

            }

        } catch (ClassCastException ignored) {

        }

    }

    static void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {

        ISlashCommand serverCommand = CommandManager.slashCommandsMap.get(event.getSelectMenu().getId().replace("Menu", "").toLowerCase().split("&id=")[0]);

        OneSelectionMenuCommandTemplate oneSelectionMenu = (OneSelectionMenuCommandTemplate) serverCommand;

        if (oneSelectionMenu != null && ActionRows.proofSelectMenu(event, event.getSelectMenu().getId().split("&id=")[0], event.getUser())) {

            String arg = event.getSelectedOptions().get(0).getValue();

            ConcurrentHashMap<String, String[]> targets = targets(oneSelectionMenu.choices(), arg);

            event.editMessageEmbeds(replyEmbed(arg, targets, oneSelectionMenu.settings(), oneSelectionMenu.mainPage(), oneSelectionMenu.choiceEmbed(targets.keySet().stream().findFirst().get(), targets.get(targets.keySet().stream().findFirst().get())))).setComponents(actionRow(event.getUser(), arg, oneSelectionMenu.choices(), oneSelectionMenu.settings())).queue();

        }

    }

}
