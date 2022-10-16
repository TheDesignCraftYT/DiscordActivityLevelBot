package de.thedesigncraft.discord.bot.commands;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.discordcommands.ISlashCommand;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LevelSlashCommand implements ISlashCommand {
    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public @NotNull String description() {
        return "Zeigt dir dein aktuelles Level an.";
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
    public @Nullable List<Guild> guilds() {
        return null;
    }

    @Override
    public boolean guildOnly() {
        return true;
    }

    @Override
    public @Nullable List<OptionData> options() {
        return Collections.singletonList(new OptionData(OptionType.USER, "user", "Der Nutzer, dessen Level du angezeigt bekommen haben möchtest."));
    }

    @Override
    public @NotNull Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F4CA");
    }

    @Override
    public @Nullable List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performSlashCommand(@NotNull SlashCommandInteractionEvent event) {

        Member member = event.getMember();

        if (event.getOption("user") != null)
            member = event.getOption("user").getAsMember();

        assert member != null;
        int messagesCount = ActivityMethods.getMessages(member);
        int level = Levels.getLevel(messagesCount, event.getGuild());

        if (level == -1)
            level = Levels.levelMins(event.getGuild()).size() - 1;

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Dein Level",
                "Du hast in den letzten 7 Tagen **" + messagesCount + "** Nachrichten geschrieben."
        ));

        if (member != event.getMember())
            embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("Du hast", member.getAsMention() + " hat"));

        embedBuilder.setColor(0x00A8F3);

        try {

            embedBuilder.addField("<:arrow:1030434355258081332> | __Vorheriges Level__",
                    "<:line2:1030439744179544084> | Level: " + (level - 1) + "\n" +
                            "<:arrow:1030434355258081332> | Mindestanzahl an Nachrichten: " + Levels.levelMins(event.getGuild()).get(level - 1) + "\n" +
                            "<:line2:1030439744179544084> | Name: " + Levels.levelStrings(event.getGuild()).get(level - 1),
                    false);

        } catch (Exception ignored) {

        }

        try {

            embedBuilder.addField("<:arrow:1030434355258081332> |-| __Aktuelles Level__",
                    "<:line2:1030439744179544084> |-| Level: " + (level) + "\n" +
                            "<:arrow:1030434355258081332> |-| Mindestanzahl an Nachrichten: " + Levels.levelMins(event.getGuild()).get(level) + "\n" +
                            "<:line2:1030439744179544084> |-| Name: " + Levels.levelStrings(event.getGuild()).get(level),
                    false);

        } catch (Exception ignored) {

        }

        try {

            embedBuilder.addField("<:arrow:1030434355258081332> | __Nächstes Level__",
                    "<:line2:1030439744179544084> | Level: " + (level + 1) + "\n" +
                            "<:arrow:1030434355258081332> | Mindestanzahl an Nachrichten: " + Levels.levelMins(event.getGuild()).get(level + 1) + "\n" +
                            "<:line2:1030439744179544084> | Name: " + Levels.levelStrings(event.getGuild()).get(level + 1),
                    false);

        } catch (Exception ignored) {

        }

        event.replyEmbeds(embedBuilder.build()).queue();

    }
}
