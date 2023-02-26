package de.thedesigncraft.discord.bot.commands.levelsystem;

import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.ActionRows;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelSystemListener extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {

        if (!ActionRows.proofSelectMenu(event, "levelSystem.settings", event.getUser()))
            return;

        event
                .editMessageEmbeds(
                        new EmbedBuilder(LevelSystemEmbeds.mainPage(Emoji.fromUnicode("U+2699"))).appendDescription("\n\n`❌ Du kannst aktuell nichts auswählen. Klicke erst unten auf 'Einstellungen abbrechen' um ein weiteres Level zu bearbeiten.`").build(),
                        LevelSystemEmbeds.levelSettings(event))
                .setComponents(
                        LevelSystemActionRows.levelSettings(Integer.parseInt(event.getSelectedOptions().get(0).getValue()), event.getUser(), false)
                ).queue();

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (!event.getButton().getId().startsWith("levelSystem."))
            return;

        if (!event.getButton().getId().split("&id=")[1].equals(event.getUser().getId()))
            return;

        if (event.getButton().getId().split("&id=")[0].replace(".", "III").split("III")[1].equals("toMainPage")) {

            event.editMessageEmbeds(LevelSystemEmbeds.mainPage(Emoji.fromUnicode("U+2699"))).setComponents(LevelSystemActionRows.mainPage(event.getMember())).queue();
            return;

        }

        if (event.getButton().getId().split("&id=")[0].replace(".", "III").split("III")[2].equals("goToLevelSettings")) {

            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(1))
                    .setDescription(
                            event.getMessage().getEmbeds().get(1).getDescription()
                                    .replace(
                                            "\n\n`❌ Du kannst aktuell nichts auswählen. Klicke erst unten auf 'Fertig' um weitere Einstellungen an dem ausgewählten Level vorzunehmen.`",
                                            "")
                    ).build();

            boolean saveAvailable = embed.getFields().get(0).getValue().contains(" -> ") || embed.getFields().get(1).getValue().contains(" -> ");

            event
                    .editMessageEmbeds(
                            event.getMessage().getEmbeds().get(0), embed)
                    .setComponents(
                            LevelSystemActionRows
                                    .levelSettings(
                                            Levels.levelStrings(event.getGuild())
                                                    .indexOf(
                                                            event.getMessage().getEmbeds().get(2).getTitle()
                                                                    .split("'")[1]),
                                            event.getUser(),
                                            saveAvailable)
                    ).queue();

            return;

        }

        if (event.getButton().getId().split("&id=")[0].replace(".", "III").split("III")[2].equals("reset")) {

            int fieldIndex = Integer.parseInt(event.getButton().getId().split("&id=")[0].replace(".", "III").split("III")[3]);

            EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(1))
                    .setDescription(
                            event.getMessage().getEmbeds().get(1).getDescription()
                                    .replace(
                                            "\n\n`❌ Du kannst aktuell nichts auswählen. Klicke erst unten auf 'Fertig' um weitere Einstellungen an dem ausgewählten Level vorzunehmen.`",
                                            ""));

            MessageEmbed.Field minvalue = embedBuilder.getFields().get(0);

            MessageEmbed.Field name = embedBuilder.getFields().get(1);

            embedBuilder.clearFields();

            if (fieldIndex == 0) {

                embedBuilder.addField(minvalue.getName(), "```" + minvalue.getValue().split("` -> `")[fieldIndex].replace("`", "") + "```", true);
                embedBuilder.addField(name);

            } else if (fieldIndex == 1) {

                embedBuilder.addField(minvalue);
                embedBuilder.addField(name.getName(), "```" + name.getValue().split("` -> `")[fieldIndex].replace("`", "") + "```", true);

            }

            MessageEmbed embed = embedBuilder.build();

            boolean saveAvailable = embed.getFields().get(0).getValue().contains(" -> ") || embed.getFields().get(1).getValue().contains(" -> ");

            event
                    .editMessageEmbeds(
                            event.getMessage().getEmbeds().get(0), embed)
                    .setComponents(
                            LevelSystemActionRows
                                    .levelSettings(
                                            Levels.levelStrings(event.getGuild())
                                                    .indexOf(
                                                            event.getMessage().getEmbeds().get(2).getTitle()
                                                                    .split("'")[1]),
                                            event.getUser(),
                                            saveAvailable)
                    ).queue();

            return;

        }

        if (event.getButton().getId().split("&id=")[0].replace(".", "III").split("III")[2].equals("save")) {

            List<MessageEmbed.Field> fields = event.getMessage().getEmbeds().get(1).getFields();

            ArrayList<MessageEmbed.Field> fieldsToSave = new ArrayList<>();

            fields.forEach(field -> {
                if (field.getValue().contains("` -> `"))
                    fieldsToSave.add(field);
            });

            fieldsToSave.forEach(field -> {

                int number = Integer.parseInt(event.getMessage().getEmbeds().get(1).getTitle().replace(" (", "III").split("III")[event.getMessage().getEmbeds().get(1).getTitle().replace(" (", "III").split("III").length - 1].replace(".", "III").split("III")[0]);

                String valueName = null;

                if (fields.indexOf(field) == 0) {

                    valueName = "minvalue";

                    SQLite.updateData("levels", valueName, field.getValue().split("` -> `")[1].replace("`", ""), "guildid", event.getGuild().getId() + " AND number = " + (number - 1));

                }

                if (fields.indexOf(field) == 1) {

                    valueName = "name";

                    SQLite.updateData("levels", valueName, "'" + field.getValue().split("` -> `")[1].replace("`", "") + "'", "guildid", event.getGuild().getId() + " AND number = " + (number - 1));

                }

            });

            event.editMessageEmbeds(new EmbedBuilder(LevelSystemEmbeds.mainPage(Emoji.fromUnicode("U+2699"))).appendDescription("\n\n`✅ Einstellungen erfolgreich abgespeichert.`").build()).setComponents(LevelSystemActionRows.mainPage(event.getMember())).queue();
            return;

        }

        event
                .editMessageEmbeds(
                        event.getMessage().getEmbeds().get(0),
                        new EmbedBuilder(event.getMessage().getEmbeds().get(1)).appendDescription("\n\n`❌ Du kannst aktuell nichts auswählen. Klicke erst unten auf 'Fertig' um weitere Einstellungen an dem ausgewählten Level vorzunehmen.`").build(),
                        LevelSystemEmbeds.editValue(event.getButton().getId()))
                .setComponents(
                        LevelSystemActionRows.editValue(event.getMember(), event.getMessage().getEmbeds().get(1), LevelSystemEmbeds.editValue(event.getButton().getId()))
                ).queue();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getMessage().getReferencedMessage() == null)
            return;

        if (!event.getMessage().getReferencedMessage().getAuthor().equals(Main.jda.getSelfUser()))
            return;

        if (event.getMessage().getReferencedMessage().getEmbeds().get(2) == null)
            return;

        MessageEmbed embed = new EmbedBuilder(event.getMessage().getReferencedMessage().getEmbeds().get(1))
                .setDescription(
                        event.getMessage().getReferencedMessage().getEmbeds().get(1).getDescription()
                                .replace(
                                        "\n\n`❌ Du kannst aktuell nichts auswählen. Klicke erst unten auf 'Fertig' um weitere Einstellungen an dem ausgewählten Level vorzunehmen.`",
                                        "")
                ).build();

        if (event.getMessage().getReferencedMessage().getEmbeds().get(2).getTitle().split("'")[1].equals("minvalue")) {

            int newMinValue = Integer.parseInt(event.getMessage().getContentDisplay());

            try {

                int oldMinValue = SQLite.getData(Collections.singletonList("minvalue"), "levels", "guildid", event.getGuild().getId() + " AND number = " + (Integer.parseInt(event.getMessage().getReferencedMessage().getEmbeds().get(1).getTitle().replace("(", "III").split("III")[1].replace(".", "III").split("III")[0]) - 1)).getInt("minvalue");

                if (newMinValue == oldMinValue) {

                    event.getMessage().getReferencedMessage().editMessageEmbeds(event.getMessage().getReferencedMessage().getEmbeds().get(0), event.getMessage().getReferencedMessage().getEmbeds().get(1),
                            new EmbedBuilder(event.getMessage().getReferencedMessage().getEmbeds().get(2))
                                    .appendDescription("\n\n`❌ Dieser Wert ist aktuell schon dafür festgelegt.`").build()).queue();

                    event.getMessage().delete().queue();

                } else {

                    EmbedBuilder embedBuilder = new EmbedBuilder(embed);

                    List<MessageEmbed.Field> fields = embed.getFields();

                    embedBuilder.clearFields();

                    embedBuilder.addField(fields.get(0).getName(), "`" + oldMinValue + "` -> `" + newMinValue + "`", true);
                    embedBuilder.addField(fields.get(1));

                    embed = embedBuilder.build();

                    event.getMessage().getReferencedMessage()
                            .editMessageEmbeds(
                                    event.getMessage().getReferencedMessage().getEmbeds().get(0), embed)
                            .setComponents(
                                    LevelSystemActionRows
                                            .levelSettings(
                                                    Levels.levelStrings(event.getGuild())
                                                            .indexOf(
                                                                    event.getMessage().getReferencedMessage().getEmbeds().get(2).getTitle()
                                                                            .split("'")[1]),
                                                    event.getAuthor(),
                                                    true)
                            ).queue();

                    event.getMessage().delete().queue();

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (event.getMessage().getReferencedMessage().getEmbeds().get(2).getTitle().split("'")[1].equals("name")) {

            String newName = event.getMessage().getContentDisplay();

            try {

                String oldName = SQLite.getData(Collections.singletonList("name"), "levels", "guildid", event.getGuild().getId() + " AND number = " + (Integer.parseInt(event.getMessage().getReferencedMessage().getEmbeds().get(1).getTitle().replace("(", "III").split("III")[1].replace(".", "III").split("III")[0]) - 1)).getString("name");

                if (newName.equals(oldName)) {

                    event.getMessage().getReferencedMessage().editMessageEmbeds(event.getMessage().getReferencedMessage().getEmbeds().get(0), event.getMessage().getReferencedMessage().getEmbeds().get(1),
                            new EmbedBuilder(event.getMessage().getReferencedMessage().getEmbeds().get(2))
                                    .appendDescription("\n\n`❌ Dieser Wert ist aktuell schon dafür festgelegt.`").build()).queue();

                    event.getMessage().delete().queue();

                } else {

                    EmbedBuilder embedBuilder = new EmbedBuilder(embed);

                    List<MessageEmbed.Field> fields = embed.getFields();

                    embedBuilder.clearFields();

                    embedBuilder.addField(fields.get(0));
                    embedBuilder.addField(fields.get(1).getName(), "`" + oldName + "` -> `" + newName + "`", true);

                    embed = embedBuilder.build();

                    event.getMessage().getReferencedMessage()
                            .editMessageEmbeds(
                                    event.getMessage().getReferencedMessage().getEmbeds().get(0), embed)
                            .setComponents(
                                    LevelSystemActionRows
                                            .levelSettings(
                                                    Levels.levelStrings(event.getGuild())
                                                            .indexOf(
                                                                    event.getMessage().getReferencedMessage().getEmbeds().get(2).getTitle()
                                                                            .split("'")[1]),
                                                    event.getAuthor(),
                                                    true)
                            ).queue();

                    event.getMessage().delete().queue();

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
