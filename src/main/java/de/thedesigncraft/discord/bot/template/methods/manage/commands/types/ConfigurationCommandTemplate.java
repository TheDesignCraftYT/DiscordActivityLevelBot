package de.thedesigncraft.discord.bot.template.methods.manage.commands.types;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// TODO

public interface ConfigurationCommandTemplate extends ISlashCommand {

    @Override
    @NotNull
    default String description() {

        return "Der " + this.getClass().getSimpleName().replace("SlashCommand", "") + "-Konfigurations Befehl.";

    }

    @Override
    @NotNull
    default String category() {

        return CommandCategories.categories().get("configuration");

    }

    @Override
    default boolean globalCommand() {

        return true;

    }

    @Override
    @NotNull
    default List<OptionData> options() {

        ArrayList<OptionData> options = new ArrayList<>();

        try {

            ResultSetMetaData rsmd = SQLite.getData(Collections.singletonList("*"), connectedTableName(), null, null).getMetaData();

            for (int i = 1; i != rsmd.getColumnCount(); i++) {

                System.out.println(i);

                System.out.println(rsmd.getColumnName(i));

                if (!rsmd.getColumnName(i).equals("guildid")) {

                    if (getOptionTypeFromSQLType(rsmd.getColumnTypeName(i)).equals(OptionType.STRING) && commandChoices(getOptionTypeFromSQLType(rsmd.getColumnTypeName(i))).get(i-1) != null) {

                        System.out.println(commandChoices(getOptionTypeFromSQLType(rsmd.getColumnTypeName(i))).get(i-1));

                        options.add(new OptionData(OptionType.STRING, rsmd.getColumnName(i), "Der Wert für die Einstellung '" + rsmd.getColumnName(i) + "'.").addChoices(commandChoices(getOptionTypeFromSQLType(rsmd.getColumnTypeName(i))).get(i-1)));

                    } else {

                        options.add(new OptionData(getOptionTypeFromSQLType(rsmd.getColumnTypeName(i)), rsmd.getColumnName(i), "Der Wert für die Einstellung '" + rsmd.getColumnName(i) + "'."));

                    }


                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return options;

    }

    @Override
    @NotNull
    default Emoji commandEmoji() {

        return Emoji.fromUnicode("U+2699");

    }

    @Override
    @NotNull
    default List<Permission> requiredPermissions() {

        return Collections.singletonList(Permission.ADMINISTRATOR);

    }

    @NotNull
    String connectedTableName();

    @NotNull
    ArrayList<ArrayList<Command.Choice>> commandChoices(@NotNull OptionType optionType);

    @NotNull
    default String detailedDescription() {

        return description();

    }

    @Override
    default void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " " + this.getClass().getSimpleName().replace("SlashCommand", ""), detailedDescription()));

        options().forEach(optionData -> {

            try {
                embedBuilder.addField(optionData.getName(), SQLite.getData(Collections.singletonList(optionData.getName()), connectedTableName(), "guildid", event.getGuild().getId()).getString(optionData.getName()), false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });

        event.replyEmbeds(embedBuilder.build()).queue();

    }

    @Nullable
    static OptionType getOptionTypeFromSQLType(@NotNull String typeName) {

        try {

            return OptionType.valueOf(typeName);

        } catch (IllegalArgumentException e) {

            LoggerFactory.getLogger(ConfigurationCommandTemplate.class).error(e.getMessage());
            return null;

        }

    }

}
