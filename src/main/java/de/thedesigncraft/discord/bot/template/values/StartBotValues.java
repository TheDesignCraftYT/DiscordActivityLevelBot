package de.thedesigncraft.discord.bot.template.values;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface StartBotValues {

    @NotNull
    static List<GatewayIntent> intents() {

        List<GatewayIntent> returnList = new ArrayList<>();

        // Intents zur returnList hinzufügen
        returnList.add(GatewayIntent.GUILD_MEMBERS);
        returnList.add(GatewayIntent.GUILD_PRESENCES);
        returnList.add(GatewayIntent.MESSAGE_CONTENT);

        return returnList;

    }

    static List<CacheFlag> cachFlags() {

        List<CacheFlag> returnList = new ArrayList<>();

        returnList.add(CacheFlag.ONLINE_STATUS);

        return returnList;

    }
}
