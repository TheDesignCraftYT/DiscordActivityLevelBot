package de.thedesigncraft.discord.bot.template.values;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Loop {

    // Interval in seconds
    int interval = 310;

    static void onLoop() {

        ActivityMethods.cleanUpTable();

        Main.jda.getPresence().setStatus(OnlineStatus.ONLINE);
        Main.jda.getPresence().setActivity(activity("/help | %reachableUsers erreichbare Nutzer"));

    }

    @NotNull
    static Activity activity(@NotNull String activityText) {

        List<Long> userIds = new ArrayList<>();

        List<User> users = new ArrayList<>();

        Main.jda.getGuilds().forEach(guild -> guild.getMembers().forEach(member -> users.add(member.getUser())));

        users.forEach(user -> {

            if (!userIds.contains(user.getIdLong()) && !user.isBot()) {

                userIds.add(user.getIdLong());

            }

        });

        int sizeOfReachableUsers = userIds.size();

        // Main.jda.getGuilds().get(0).getMembers().forEach(System.out::println);

        return Activity.listening(activityText.replace("%reachableUsers", String.valueOf(sizeOfReachableUsers)));

    }

}
