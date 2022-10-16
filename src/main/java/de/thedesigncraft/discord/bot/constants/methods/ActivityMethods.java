package de.thedesigncraft.discord.bot.constants.methods;

import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.SQLite;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public interface ActivityMethods {

    static int getMessages(@NotNull Member member) {

        ResultSet resultSet = SQLite.getData(Collections.singletonList("timestamp"), "messages", "guildid", member.getGuild().getIdLong() + " AND userid = " + member.getUser().getIdLong());

        ArrayList<Long> timestamps = new ArrayList<>();

        while (true) {
            try {
                if (!resultSet.next()) break;

                timestamps.add(resultSet.getLong("timestamp"));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return timestamps.size();

    }

    static void cleanUpTable() {

        SQLite.onUpdate("DELETE FROM messages WHERE timestamp < " + Date.from(Instant.now().minus(7, ChronoUnit.DAYS)).getTime() / 1000L);

    }

    static void addNewMessage(@NotNull Message message) {

            System.out.println("Neue Nachricht");

            ArrayList<String> valueNames = new ArrayList<>();
            ArrayList<String> valueValues = new ArrayList<>();

            valueNames.add("guildid");
            valueValues.add(message.getGuild().getId());

            valueNames.add("userid");
            valueValues.add(message.getAuthor().getId());

            valueNames.add("timestamp");
            valueValues.add(String.valueOf(Date.from(message.getTimeCreated().toInstant()).getTime() / 1000L));

            SQLite.setData("messages", valueNames, valueValues);

    }

    static void registerAllMessages(@NotNull Guild guild) {

        System.out.println("Neuer Server");

        HashMap<Long, ArrayList<Long>> data = new HashMap<>();

        guild.getChannels().forEach(guildChannel -> {

            if (guildChannel.getType().equals(ChannelType.GUILD_NEWS_THREAD) || guildChannel.getType().equals(ChannelType.GUILD_PRIVATE_THREAD) || guildChannel.getType().equals(ChannelType.GUILD_PUBLIC_THREAD)) {

                ThreadChannel threadChannel = (ThreadChannel) guildChannel;

                threadChannel.getIterableHistory().cache(false).stream().filter(
                        message -> message.getTimeCreated().isAfter(OffsetDateTime.ofInstant(Instant.now().minus(7, ChronoUnit.DAYS), ZoneId.systemDefault())) &&
                                !message.getAuthor().equals(MainTemplate.jda.getSelfUser())
                ).forEach(message -> {

                    ArrayList<Long> timestamps = data.get(message.getAuthor().getIdLong());

                    if (timestamps == null)
                        timestamps = new ArrayList<>();

                    timestamps.add(Date.from(message.getTimeCreated().toInstant()).getTime() / 1000L);

                    data.put(message.getAuthor().getIdLong(), timestamps);

                });

            } else if (guildChannel.getType().equals(ChannelType.NEWS)) {

                NewsChannel newsChannel = (NewsChannel) guildChannel;

                newsChannel.getIterableHistory().cache(false).stream().filter(
                        message -> message.getTimeCreated().isAfter(OffsetDateTime.ofInstant(Instant.now().minus(7, ChronoUnit.DAYS), ZoneId.systemDefault())) &&
                                !message.getAuthor().equals(MainTemplate.jda.getSelfUser())
                ).forEach(message -> {

                    ArrayList<Long> timestamps = data.get(message.getAuthor().getIdLong());

                    if (timestamps == null)
                        timestamps = new ArrayList<>();

                    timestamps.add(Date.from(message.getTimeCreated().toInstant()).getTime() / 1000L);

                    data.put(message.getAuthor().getIdLong(), timestamps);

                });

            } else if (guildChannel.getType().equals(ChannelType.TEXT)) {

                TextChannel textChannel = (TextChannel) guildChannel;

                textChannel.getIterableHistory().cache(false).stream().filter(
                        message -> message.getTimeCreated().isAfter(OffsetDateTime.ofInstant(Instant.now().minus(7, ChronoUnit.DAYS), ZoneId.systemDefault())) &&
                                !message.getAuthor().equals(MainTemplate.jda.getSelfUser())
                ).forEach(message -> {

                    ArrayList<Long> timestamps = data.get(message.getAuthor().getIdLong());

                    if (timestamps == null)
                        timestamps = new ArrayList<>();

                    timestamps.add(Date.from(message.getTimeCreated().toInstant()).getTime() / 1000L);

                    data.put(message.getAuthor().getIdLong(), timestamps);

                });

            } else if (guildChannel.getType().equals(ChannelType.VOICE)) {

                VoiceChannel voiceChannel = (VoiceChannel) guildChannel;

                voiceChannel.getIterableHistory().cache(false).stream().filter(
                        message -> message.getTimeCreated().isAfter(OffsetDateTime.ofInstant(Instant.now().minus(7, ChronoUnit.DAYS), ZoneId.systemDefault())) &&
                                !message.getAuthor().equals(MainTemplate.jda.getSelfUser())
                ).forEach(message -> {

                    ArrayList<Long> timestamps = data.get(message.getAuthor().getIdLong());

                    if (timestamps == null)
                        timestamps = new ArrayList<>();

                    timestamps.add(Date.from(message.getTimeCreated().toInstant()).getTime() / 1000L);

                    data.put(message.getAuthor().getIdLong(), timestamps);

                });

            }

        });

        System.out.println("Abspeichern wird gestartet");

        data.forEach((userid, unixTimestamps) -> {

            System.out.println("Nachricht zum abspeichern.");

            ArrayList<String> valueNames = new ArrayList<>();
            ArrayList<String> valueValues = new ArrayList<>();

            valueNames.add("guildid");
            valueValues.add(guild.getId());

            valueNames.add("userid");
            valueValues.add(String.valueOf(userid));

            valueNames.add("timestamp");
            unixTimestamps.forEach(unixTimestamp -> {

                valueValues.add(String.valueOf(unixTimestamp));

                SQLite.setData("messages", valueNames, valueValues);

                valueValues.remove(String.valueOf(unixTimestamp));

            });

            System.out.println("Nachricht abgespeichert");

        });

        System.out.println("Abspeichern beendet");

    }

    static void setDefaultLevels(@NotNull Guild guild) {

        ArrayList<String> valueNames = new ArrayList<>();
        ArrayList<String> valueValues = new ArrayList<>();

        valueNames.add("guildid");
        valueValues.add(guild.getId());

        valueNames.add("number");
        valueNames.add("minvalue");
        valueNames.add("name");

        for (int i = 0;i != Levels.standardLevelMins().size();i++) {

            valueValues.add(String.valueOf(i));

            valueValues.add(String.valueOf(Levels.standardLevelMins().get(i)));

            valueValues.add("'" + Levels.standardLevelStrings().get(i) + "'");

            SQLite.setData("levels", valueNames, valueValues);

            valueValues.remove(String.valueOf(i));

            valueValues.remove(String.valueOf(Levels.standardLevelMins().get(i)));

            valueValues.remove("'" + Levels.standardLevelStrings().get(i) + "'");

        }

    }

}
