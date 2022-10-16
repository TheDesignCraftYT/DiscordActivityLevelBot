package de.thedesigncraft.discord.bot.template.methods.manage.discordcommands;

import de.thedesigncraft.discord.bot.template.methods.EmbedTemplates;
import de.thedesigncraft.discord.bot.template.methods.listeners.StandardActionRowListener;
import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import de.thedesigncraft.discord.bot.template.methods.manage.Manager;
import de.thedesigncraft.discord.bot.template.values.MainValues;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {

    public static ConcurrentHashMap<String, ISlashCommand> slashCommandsMap;
    public static ConcurrentHashMap<String, IUserContextMenu> userContextMenuMap;

    public static ConcurrentHashMap<String, IMessageContextMenu> messageContextMenuMap;

    public CommandManager() {

        slashCommandsMap = new ConcurrentHashMap<>();
        userContextMenuMap = new ConcurrentHashMap<>();
        messageContextMenuMap = new ConcurrentHashMap<>();

        Manager.slashCommands().forEach(serverCommand -> slashCommandsMap.put(ICommandMethods.getSlashCommandName(serverCommand), serverCommand));
        Manager.userContextMenus().forEach(userContextMenu -> userContextMenuMap.put(ICommandMethods.getUserContextMenuName(userContextMenu), userContextMenu));
        Manager.messageContextMenus().forEach(messageContextMenu -> messageContextMenuMap.put(ICommandMethods.getMessageContextMenuName(messageContextMenu), messageContextMenu));

        MainTemplate.jda.retrieveCommands().queue(commands -> {

            List<String> oldCommandNames = new ArrayList<>();

            commands.forEach(command -> oldCommandNames.add(command.getName()));

            List<String> newCommandNames = new ArrayList<>();

            slashCommandsMap.forEach((s, serverCommand) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            userContextMenuMap.forEach((s, userContextMenu) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            messageContextMenuMap.forEach((s, messageContextMenu) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            Logger logger = LoggerFactory.getLogger(CommandManager.class);

            newCommandNames.forEach(s -> logger.info("Neuer Befehl registriert: '" + s + "'"));

        });

        List<String> oldCommandNames = new ArrayList<>();
        MainTemplate.jda.retrieveCommands().queue(commands -> commands.forEach(command -> oldCommandNames.add(command.getName())));

        CommandListUpdateAction updateAction = MainTemplate.jda.updateCommands();
        ConcurrentHashMap<Long, CommandListUpdateAction> guildUpdateActions = new ConcurrentHashMap<>();

        Manager.slashCommands().forEach(slashCommand -> {

            if (slashCommand.globalCommand()) {

                registerSlashCommand(slashCommand, updateAction);

            } else {

                MainTemplate.jda.getGuilds().forEach(guild -> {

                    if (!guildUpdateActions.containsKey(guild.getIdLong())) {

                        CommandListUpdateAction guildUpdateAction = guild.updateCommands();
                        guildUpdateActions.put(guild.getIdLong(), guildUpdateAction);
                        registerSlashCommand(slashCommand, guildUpdateAction);

                    } else {

                        registerSlashCommand(slashCommand, guildUpdateActions.get(guild.getIdLong()));

                    }

                });

            }

        });

        Manager.userContextMenus().forEach(userContextMenu -> {

            if (userContextMenu.globalCommand()) {

                registerUserCommand(userContextMenu, updateAction);

            } else {

                MainTemplate.jda.getGuilds().forEach(guild -> {

                    if (!guildUpdateActions.containsKey(guild.getIdLong())) {

                        CommandListUpdateAction guildUpdateAction = guild.updateCommands();
                        guildUpdateActions.put(guild.getIdLong(), guildUpdateAction);
                        registerUserCommand(userContextMenu, guildUpdateAction);

                    } else {

                        registerUserCommand(userContextMenu, guildUpdateActions.get(guild.getIdLong()));

                    }

                });

            }

        });

        Manager.messageContextMenus().forEach(messageContextMenu -> {

            if (messageContextMenu.globalCommand()) {

                registerMessageCommand(messageContextMenu, updateAction);

            } else {

                MainTemplate.jda.getGuilds().forEach(guild -> {

                    if (!guildUpdateActions.containsKey(guild.getIdLong())) {

                        CommandListUpdateAction guildUpdateAction = guild.updateCommands();
                        guildUpdateActions.put(guild.getIdLong(), guildUpdateAction);
                        registerMessageCommand(messageContextMenu, guildUpdateAction);

                    } else {

                        registerMessageCommand(messageContextMenu, guildUpdateActions.get(guild.getIdLong()));

                    }

                });

            }

        });

        guildUpdateActions.forEach((aLong, updateAction1) -> updateAction1.queue());
        updateAction.queue();

        List<String> newCommandNames = new ArrayList<>();
        MainTemplate.jda.retrieveCommands().queue(commands -> commands.forEach(command -> newCommandNames.add(command.getName())));

        Logger logger = LoggerFactory.getLogger(CommandManager.class);
        oldCommandNames.forEach(s -> {

            if (!newCommandNames.contains(s))
                logger.info("Alter Befehl entfernt: '" + s + "'");

        });

        logger.info("Befehle registriert.");

    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        String commandName = event.getName();

        ISlashCommand serverCommand;

        if ((serverCommand = slashCommandsMap.get(commandName)) != null) {

            if (serverCommand.requiredPermissions() != null) {

                if (event.getMember().hasPermission(serverCommand.requiredPermissions())) {

                    serverCommand.performSlashCommand(event);

                } else {

                    List<Permission> missingPermissions = new ArrayList<>();

                    serverCommand.requiredPermissions().forEach(permission -> {

                        if (!event.getMember().hasPermission(permission))
                            missingPermissions.add(permission);

                    });

                    StringBuilder stringBuilder = new StringBuilder();

                    missingPermissions.forEach(permission -> stringBuilder
                            .append("> • ")
                            .append(permission.getName())
                            .append("\n"));

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                }

            } else {

                serverCommand.performSlashCommand(event);

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf einen ISlashCommand wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf ISlashCommand");

            MainValues.owners().forEach(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue()));
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an die Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {

        String commandName = event.getName();

        IUserContextMenu IUserContextMenu;

        if ((IUserContextMenu = userContextMenuMap.get(commandName)) != null) {

            if (IUserContextMenu.requiredPermissions() != null) {

                if (event.getMember().hasPermission(IUserContextMenu.requiredPermissions())) {

                    IUserContextMenu.performUserContextMenu(event);

                } else {

                    List<Permission> missingPermissions = new ArrayList<>();

                    IUserContextMenu.requiredPermissions().forEach(permission -> {

                        if (!event.getMember().hasPermission(permission))
                            missingPermissions.add(permission);

                    });

                    StringBuilder stringBuilder = new StringBuilder();

                    missingPermissions.forEach(permission -> stringBuilder
                            .append("> • ")
                            .append(permission.getName())
                            .append("\n"));

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                }

            } else {

                IUserContextMenu.performUserContextMenu(event);

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf ein IUserContextMenu wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf IUserContextMenu");

            MainValues.owners().forEach(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue()));
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        String commandName = event.getName();

        IMessageContextMenu IMessageContextMenu;

        if ((IMessageContextMenu = messageContextMenuMap.get(commandName)) != null) {

            if (IMessageContextMenu.requiredPermissions() != null) {

                if (event.getMember().hasPermission(IMessageContextMenu.requiredPermissions())) {

                    IMessageContextMenu.performMessageContextMenu(event);

                } else {

                    List<Permission> missingPermissions = new ArrayList<>();

                    IMessageContextMenu.requiredPermissions().forEach(permission -> {

                        if (!event.getMember().hasPermission(permission))
                            missingPermissions.add(permission);

                    });

                    StringBuilder stringBuilder = new StringBuilder();

                    missingPermissions.forEach(permission -> stringBuilder
                            .append("> • ")
                            .append(permission.getName())
                            .append("\n"));

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                }

            } else {

                IMessageContextMenu.performMessageContextMenu(event);

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf ein IMessageContextMenu wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf IMessageContextMenu");

            MainValues.owners().forEach(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue()));
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an die Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @CheckReturnValue
    private static void registerSlashCommand(@NotNull ISlashCommand ISlashCommand, @NotNull CommandListUpdateAction updateAction) {

        if (ISlashCommand.options() != null) {

            if (ISlashCommand.requiredPermissions() != null) {

                updateAction.addCommands(Commands.slash(
                                ICommandMethods.getSlashCommandName(ISlashCommand),
                                ISlashCommand.description())
                        .setGuildOnly(ISlashCommand.guildOnly())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(ISlashCommand.requiredPermissions()))
                        .addOptions(ISlashCommand.options())
                );

            } else {

                updateAction.addCommands(Commands.slash(
                                ICommandMethods.getSlashCommandName(ISlashCommand),
                                ISlashCommand.description())
                        .setGuildOnly(ISlashCommand.guildOnly())
                        .addOptions(ISlashCommand.options())
                );

            }

        } else {

            if (ISlashCommand.requiredPermissions() != null) {

                updateAction.addCommands(Commands.slash(
                                ICommandMethods.getSlashCommandName(ISlashCommand),
                                ISlashCommand.description())
                        .setGuildOnly(ISlashCommand.guildOnly())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(ISlashCommand.requiredPermissions()))
                );

            } else {

                updateAction.addCommands(Commands.slash(
                                ICommandMethods.getSlashCommandName(ISlashCommand),
                                ISlashCommand.description())
                        .setGuildOnly(ISlashCommand.guildOnly())
                );

            }

        }

    }

    @CheckReturnValue
    private static void registerUserCommand(@NotNull IUserContextMenu IUserContextMenu, @NotNull CommandListUpdateAction updateAction) {

        if (IUserContextMenu.requiredPermissions() != null) {

            updateAction.addCommands(Commands.user(
                            ICommandMethods.getUserContextMenuName(IUserContextMenu))
                    .setGuildOnly(IUserContextMenu.guildOnly())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(IUserContextMenu.requiredPermissions()))
            );

        } else {

            updateAction.addCommands(Commands.user(
                            ICommandMethods.getUserContextMenuName(IUserContextMenu))
                    .setGuildOnly(IUserContextMenu.guildOnly())
            );

        }

    }

    @CheckReturnValue
    private static void registerMessageCommand(@NotNull IMessageContextMenu IMessageContextMenu, @NotNull CommandListUpdateAction updateAction) {

        if (IMessageContextMenu.requiredPermissions() != null) {

            updateAction.addCommands(Commands.user(
                            ICommandMethods.getMessageContextMenuName(IMessageContextMenu))
                    .setGuildOnly(IMessageContextMenu.guildOnly())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(IMessageContextMenu.requiredPermissions()))
            );

        } else {

            updateAction.addCommands(Commands.user(
                            ICommandMethods.getMessageContextMenuName(IMessageContextMenu))
                    .setGuildOnly(IMessageContextMenu.guildOnly())
            );

        }

    }

}
