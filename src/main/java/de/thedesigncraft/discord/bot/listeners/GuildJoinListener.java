package de.thedesigncraft.discord.bot.listeners;

import de.thedesigncraft.discord.bot.constants.methods.ActivityMethods;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        ActivityMethods.registerAllMessages(event.getGuild());

        ActivityMethods.setDefaultLevels(event.getGuild());

    }
}
