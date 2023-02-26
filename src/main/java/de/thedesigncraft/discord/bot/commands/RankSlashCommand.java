package de.thedesigncraft.discord.bot.commands;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.Versions;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.values.CommandCategories;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RankSlashCommand implements ISlashCommand {
    @Override
    public @NotNull String version() {
        return Versions.versions().get("v100a2");
    }

    @Override
    public @NotNull String description() {
        return "Zeigt dir die aktuelle Rangliste an.";
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
        return null;
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

        InteractionHook interactionHook = event.replyEmbeds(new EmbedBuilder().setDescription("Loading...").build()).complete();

        HashMap<Member, Integer> messageCount = new HashMap<>();
        LinkedHashMap<Member, Integer> sortedMessageCount = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        event.getGuild().getMembers().forEach(member -> {
            if (member.getUser().isBot() || member.getUser().equals(Main.jda.getSelfUser())) return;
            messageCount.put(member, ActivityMethods.getMessages(member));
        });

        for (Map.Entry<Member, Integer> entry : messageCount.entrySet()) {
            list.add(entry.getValue());
        }
        list.sort(Comparator.naturalOrder());
        Collections.reverse(list);
        for (int i : list) {
            for (Map.Entry<Member, Integer> entry : messageCount.entrySet()) {
                if (entry.getValue() == i) {
                    sortedMessageCount.put(entry.getKey(), i);
                }
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Rangliste", "Das ist die Rangliste dieses Servers:"));
        embedBuilder.setColor(0x00A8F3);

        StringBuilder stringBuilder = new StringBuilder();
        sortedMessageCount.forEach((member, integer) -> stringBuilder.append(list.indexOf(integer) + 1).append(". ").append(member.getAsMention()).append(" - ").append(integer).append(" Nachrichten - Level ").append(Levels.getLevel(integer, event.getGuild()) + 1).append("\n"));

        embedBuilder.addField("Rangliste", stringBuilder.toString(), false);

        interactionHook.editOriginalEmbeds(embedBuilder.build()).queue();

    }
}
