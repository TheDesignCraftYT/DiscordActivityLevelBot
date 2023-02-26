package de.thedesigncraft.discord.bot.template.methods.commands.help.methods;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import de.thedesigncraft.discord.bot.template.methods.manage.Manager;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.CommandManager;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.ICommandMethods;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IMessageContextMenu;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IUserContextMenu;
import de.thedesigncraft.discord.bot.template.values.EmbedValues;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public interface HelpEmbeds {

    @NotNull
    static MessageEmbed category(String category) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        List<ISlashCommand> categoryISlashCommands = new ArrayList<>();
        List<IUserContextMenu> categoryUserCommands = new ArrayList<>();
        List<IMessageContextMenu> categoryMessageCommands = new ArrayList<>();

        Manager.slashCommands().forEach(serverCommand -> {

            if (serverCommand.category().equals(category))
                categoryISlashCommands.add(serverCommand);

        });

        Manager.userContextMenus().forEach(userContextMenu -> {

            if (userContextMenu.category().equals(category))
                categoryUserCommands.add(userContextMenu);

        });

        Manager.messageContextMenus().forEach(messageContextMenu -> {

            if (messageContextMenu.category().equals(category))
                categoryMessageCommands.add(messageContextMenu);

        });

        categoryISlashCommands.forEach(serverCommand -> embedBuilder.addField(serverCommand.commandEmoji().getName() + " /" + ICommandMethods.getSlashCommandName(serverCommand), "```" + serverCommand.description() + "```", true));

        categoryUserCommands.forEach(userContextMenu -> embedBuilder.addField(userContextMenu.commandEmoji().getName() + " USER/" + ICommandMethods.getUserContextMenuName(userContextMenu), "```" + userContextMenu.description() + "```", true));

        categoryMessageCommands.forEach(messageContextMenu -> embedBuilder.addField(messageContextMenu.commandEmoji().getName() + " MESSAGE/" + ICommandMethods.getMessageContextMenuName(messageContextMenu), "```" + messageContextMenu.description() + "```", true));

        embedBuilder.setTitle(category);
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed slashCommand(ISlashCommand command, Channel channel) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        StringBuilder usage = new StringBuilder();

        embedBuilder.setTitle(command.category() + " » " + command.commandEmoji().getName() + " /" + ICommandMethods.getSlashCommandName(command));

        if (command.options() != null) {

            usage
                    .append("```➤ /")
                    .append(ICommandMethods.getSlashCommandName(command))
                    .append(requiredOptions(1, command.options(), channel))
                    .append("\n");

            command.options().forEach(optionData -> {

                if (!optionData.isRequired()) {

                    usage
                            .append("➤ /")
                            .append(ICommandMethods.getSlashCommandName(command))
                            .append(" ")
                            .append(requiredOptions(1, command.options(), channel))
                            .append(" [(")
                            .append(optionData.getType().name())
                            .append(") ")
                            .append(optionData.getName())
                            .append("]\n");

                }

            });

        } else {

            usage
                    .append("```➤ /")
                    .append(ICommandMethods.getSlashCommandName(command))
                    .append("\n");

        }


        usage.append("```");

        StringBuilder examples = new StringBuilder();

        if (command.options() != null) {

            examples
                    .append("```➤ /")
                    .append(ICommandMethods.getSlashCommandName(command))
                    .append(requiredOptions(2, command.options(), channel))
                    .append("\n");

            command.options().forEach(optionData -> {

                if (!optionData.isRequired()) {

                    examples
                            .append("➤ /")
                            .append(ICommandMethods.getSlashCommandName(command))
                            .append(" ");

                    if (optionData.getChoices().isEmpty()) {

                        if (optionData.getType().equals(OptionType.CHANNEL)) {

                            if (channel.getType().isGuild()) {

                                examples
                                        .append(requiredOptions(2, command.options(), channel))
                                        .append(" ")
                                        .append(channel.getAsMention());

                            } else {

                                examples.append("<#ID>");

                            }

                        } else if (optionData.getType().equals(OptionType.BOOLEAN)) {

                            examples
                                    .append(requiredOptions(2, command.options(), channel))
                                    .append(" true");

                        } else if (optionData.getType().equals(OptionType.INTEGER)) {

                            examples
                                    .append(requiredOptions(2, command.options(), channel))
                                    .append(" 20");

                        } else if (optionData.getType().equals(OptionType.STRING)) {

                            examples
                                    .append(requiredOptions(2, command.options(), channel))
                                    .append(" hello!");

                        } else if (optionData.getType().equals(OptionType.ROLE)) {

                            if (channel.getType().isGuild()) {

                                GuildChannel guildChannel = (GuildChannel) channel;

                                examples
                                        .append(requiredOptions(2, command.options(), channel))
                                        .append(" ")
                                        .append(guildChannel.getGuild().getRoles().get(0).getAsMention());

                            } else {

                                examples.append("<@&ID>");

                            }

                        } else if (optionData.getType().equals(OptionType.USER)) {

                            if (channel.getType().isGuild()) {

                                GuildChannel guildChannel = (GuildChannel) channel;

                                examples
                                        .append(requiredOptions(2, command.options(), channel))
                                        .append(" ")
                                        .append(guildChannel.getGuild().getMembers().get(0).getAsMention());

                            } else {

                                examples.append("<@ID>");

                            }

                        }

                        examples.append("\n");

                    } else {

                        examples
                                .append(optionData.getChoices().get(0).getName())
                                .append("\n");

                    }

                }

            });

        } else {

            examples
                    .append("```➤ /")
                    .append(ICommandMethods.getSlashCommandName(command))
                    .append("\n");

        }

        examples.append("```");

        embedBuilder.addField("Anwendung", usage.toString(), true);
        embedBuilder.addField("Beispiele", examples.toString(), true);

        if (command.requiredPermissions() != null) {

            StringBuilder stringBuilder2 = new StringBuilder();

            stringBuilder2.append("```");

            command.requiredPermissions().forEach(permission -> stringBuilder2
                    .append("• ")
                    .append(permission.getName())
                    .append("\n"));

            stringBuilder2.append("```");

            embedBuilder.addField("Erforderliche Berechtigungen", stringBuilder2.toString(), false);

        }

        embedBuilder.setDescription(command.description());
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed userCommand(IUserContextMenu command) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(command.category() + " » " + command.commandEmoji().getName() + " USER/" + ICommandMethods.getUserContextMenuName(command));

        String fieldValue = "```➤ USER/" + ICommandMethods.getUserContextMenuName(command) + "```";

        embedBuilder.addField("Anwendung", fieldValue, true);
        embedBuilder.addField("Beispiele", fieldValue, true);

        if (command.requiredPermissions() != null) {

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("```");

            command.requiredPermissions().forEach(permission -> stringBuilder
                    .append("• ")
                    .append(permission.getName())
                    .append("\n"));

            stringBuilder.append("```");

            embedBuilder.addField("Erforderliche Berechtigungen", stringBuilder.toString(), false);

        }

        embedBuilder.setDescription(command.description());
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed messageCommand(IMessageContextMenu command) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(command.category() + " » " + command.commandEmoji().getName() + " MESSAGE/" + ICommandMethods.getMessageContextMenuName(command));

        String fieldValue = "```➤ MESSAGE/" + ICommandMethods.getMessageContextMenuName(command) + "```";

        embedBuilder.addField("Anwendung", fieldValue, true);
        embedBuilder.addField("Beispiele", fieldValue, true);

        if (command.requiredPermissions() != null) {

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("```");

            command.requiredPermissions().forEach(permission -> stringBuilder
                    .append("• ")
                    .append(permission.getName())
                    .append("\n"));

            stringBuilder.append("```");

            embedBuilder.addField("Erforderliche Berechtigungen", stringBuilder.toString(), false);

        }

        embedBuilder.setDescription(command.description());
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed mainPage() {

        ISlashCommand helpServerCommand = CommandManager.slashCommandsMap.get("help");

        return EmbedTemplates.standardEmbed(helpServerCommand.commandEmoji().getName() + " Hilfe", "Willkommen in der Hilfe-Station des " + Main.jda.getSelfUser().getAsMention() + "-Bots.\n\nDu kannst entweder unten eine Kategorie auswählen, oder diesen Befehl nochmal ausführen und dabei hinter den Befehlsnamen den Namen einer Kategorie oder eines Befehls schreiben.");

    }

    private static String requiredOptions(int type, List<OptionData> options, Channel channel) {

        List<OptionData> requiredOptions = new ArrayList<>();

        options.forEach(optionData -> {

            if (optionData.isRequired()) {

                requiredOptions.add(optionData);

            }

        });

        StringBuilder stringBuilder = new StringBuilder();

        if (type == 1) {

            requiredOptions.forEach(optionData -> stringBuilder
                    .append("[(")
                    .append(optionData.getType().name())
                    .append(") ")
                    .append(optionData.getName())
                    .append("] "));

            if (!stringBuilder.toString().equals("")) {

                return " " + stringBuilder;

            } else {

                return "";

            }

        } else if (type == 2) {

            requiredOptions.forEach(optionData -> {

                if (optionData.getChoices().isEmpty()) {

                    if (optionData.getType().equals(OptionType.CHANNEL)) {

                        if (channel.getType().isGuild()) {

                            stringBuilder.append(channel.getAsMention());

                        } else {

                            stringBuilder.append("<#ID>");

                        }

                    } else if (optionData.getType().equals(OptionType.BOOLEAN)) {

                        stringBuilder.append("true");

                    } else if (optionData.getType().equals(OptionType.INTEGER)) {

                        stringBuilder.append("20");

                    } else if (optionData.getType().equals(OptionType.STRING)) {

                        stringBuilder.append("hello!");

                    } else if (optionData.getType().equals(OptionType.ROLE)) {

                        if (channel.getType().isGuild()) {

                            GuildChannel guildChannel = (GuildChannel) channel;

                            stringBuilder.append(guildChannel.getGuild().getRoles().get(0).getAsMention());

                        } else {

                            stringBuilder.append("<@&ID>");

                        }

                    } else if (optionData.getType().equals(OptionType.USER)) {

                        if (channel.getType().isGuild()) {

                            GuildChannel guildChannel = (GuildChannel) channel;

                            stringBuilder.append(guildChannel.getGuild().getMembers().get(0).getAsMention());

                        } else {

                            stringBuilder.append("<@ID>");

                        }

                    }

                    stringBuilder.append(" ");

                } else {

                    stringBuilder
                            .append(optionData.getChoices().get(0).getName())
                            .append(" ");

                }

            });

            if (!stringBuilder.toString().equals("")) {

                return " " + stringBuilder;

            } else {

                return "";

            }

        } else {

            return null;

        }

    }

}
