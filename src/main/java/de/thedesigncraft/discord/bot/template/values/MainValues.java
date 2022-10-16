package de.thedesigncraft.discord.bot.template.values;

import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface MainValues {

    @NotNull
    String name = MainTemplate.jda.getSelfUser().getName();

    @NotNull
    static List<User> owners() {

        List<User> returnList = new ArrayList<>();

        // returnList.add(MainTemplate.jda.retrieveUserById(ID).complete());
        returnList.add(MainTemplate.jda.retrieveUserById(810769870521106464L).complete());

        return returnList;

    }

    @NotNull
    TextChannel githubLog = MainTemplate.jda.getGuildById(1007268691689341030L).getTextChannelById(1031219783154270248L);

    @NotNull
    String projectName = "ActivityLevelBot";

    @NotNull
    static List<TextChannel> updateChannels() {

        List<TextChannel> returnList = new ArrayList<>();

        //returnList.add(MainTemplate.jda.getGuildById(GUILID).getTextChannelById(CHANNELID));

        return returnList;

    }

}
