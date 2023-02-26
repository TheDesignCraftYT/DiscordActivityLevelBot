package de.thedesigncraft.discord.bot.commands.levelsystem;

import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LevelSystemActionRows {

    @NotNull
    static ArrayList<ActionRow> mainPage(@NotNull Member member) {

        ArrayList<SelectOption> options = new ArrayList<>();

        Levels.levelStrings(member.getGuild()).forEach(s -> options.add(SelectOption.of(s + " (" + (Levels.levelStrings(member.getGuild()).indexOf(s) + 1) + ". Level)", String.valueOf(Levels.levelStrings(member.getGuild()).indexOf(s)))));

        SelectMenu selectMenu = StringSelectMenu.create("levelSystem.settings&id=" + member.getIdLong())
                .setPlaceholder("Wähle das Level aus, das du bearbeiten möchtest.")
                .setMaxValues(1)
                .setMinValues(1)
                .addOptions(options).build();

        ArrayList<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(selectMenu));
        returnList.add(ActionRow.of(ActionRows.cancelButton(member.getUser())));

        return returnList;

    }

    @NotNull
    static ArrayList<ActionRow> levelSettings(int index, @NotNull User user, boolean saveAvailable) {

        ArrayList<Button> buttons1 = new ArrayList<>();

        buttons1.add(Button.of(ButtonStyle.PRIMARY, "levelSystem.settings." + index + ".minvalue&id=" + user.getIdLong(), "Nachrichten-Mindestanzahl"));
        buttons1.add(Button.of(ButtonStyle.PRIMARY, "levelSystem.settings." + index + ".name&id=" + user.getIdLong(), "Name"));

        ArrayList<Button> buttons2 = new ArrayList<>();

        buttons2.add(Button.of(ButtonStyle.SECONDARY, "levelSystem.toMainPage&id=" + user.getIdLong(), "Einstellungen abbrechen"));
        buttons2.add(Button.of(ButtonStyle.SUCCESS, "levelSystem.settings.save&id=" + user.getIdLong(), "Einstellungen speichern und schließen").withDisabled(!saveAvailable));

        ArrayList<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(buttons1));
        returnList.add(ActionRow.of(buttons2));

        return returnList;

    }

    @NotNull
    public static ActionRow editValue(@NotNull Member member, @NotNull MessageEmbed levelSettings, @NotNull MessageEmbed editValue) {

        ArrayList<Button> buttons = new ArrayList<>();

        String setting = editValue.getTitle().split("'")[1];

        int index = -1;

        if (setting.equals("minvalue"))
            index = 0;

        if (setting.equals("name"))
            index = 1;

        int finalIndex = index;
        levelSettings.getFields().forEach(field -> {

            if (levelSettings.getFields().indexOf(field) == finalIndex) {

                buttons.add(Button.of(ButtonStyle.SECONDARY, "levelSystem.settings.reset." + levelSettings.getFields().indexOf(field) + "&id=" + member.getId(), "Einstellung zurücksetzen").withDisabled(!field.getValue().contains(" -> ")));

            }

        });

        buttons.add(Button.of(ButtonStyle.SUCCESS, "levelSystem.settings.goToLevelSettings&id=" + member.getId(), "Fertig"));

        return ActionRow.of(buttons);

    }
}
