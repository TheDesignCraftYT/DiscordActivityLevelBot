package de.thedesigncraft.discord.bot.test;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import de.thedesigncraft.discord.bot.constants.methods.Levels;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import net.dv8tion.jda.api.entities.Member;

public class GetDataConsoleCommand implements ConsoleCommand {
    @Override
    public void code() {

        Member member = Main.jda.getGuilds().get(0).getOwner();

        assert member != null;
        int messages = ActivityMethods.getMessages(member);
        int level = Levels.getLevel(messages, member.getGuild());

        if (level == -1)
            level = 15;

        System.out.println("Messages Count: " + messages);
        System.out.println("Level: " + level);
        try {
            System.out.println(Levels.levelMins(member.getGuild()).get(level));
            System.out.println(Levels.levelStrings(member.getGuild()).get(level));
        } catch (IndexOutOfBoundsException ignored) {
            System.out.println(Levels.levelMins(member.getGuild()).get(Levels.levelMins(member.getGuild()).size() - 1));
            System.out.println(Levels.levelStrings(member.getGuild()).get(Levels.levelStrings(member.getGuild()).size() - 1));
        }

    }
}
