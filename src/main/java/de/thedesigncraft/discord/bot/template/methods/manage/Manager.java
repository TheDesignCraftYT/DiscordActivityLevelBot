package de.thedesigncraft.discord.bot.template.methods.manage;

import de.thedesigncraft.discord.bot.template.methods.listeners.StandardActionRowListener;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ConsoleCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.discord.CommandManager;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IMessageContextMenu;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.ISlashCommand;
import de.thedesigncraft.discord.bot.template.methods.manage.commands.types.IUserContextMenu;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Manager {

    @NotNull
    public static Object[] eventListeners() {

        Reflections reflections = new Reflections(
                "de.thedesigncraft.discord.bot", new SubTypesScanner());

        List<Object> returnList = new ArrayList<>();

        reflections.getSubTypesOf(ListenerAdapter.class).forEach(aClass -> {

            if (!aClass.equals(CommandManager.class) && !aClass.equals(StandardActionRowListener.class)) {

                try {

                    returnList.add(aClass.newInstance());

                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        returnList.add(new StandardActionRowListener());

        return returnList.toArray();

    }

    @NotNull
    public static List<ISlashCommand> slashCommands() {

        List<ISlashCommand> returnList = new ArrayList<>();

        getCommands(ISlashCommand.class).forEach(aClass -> {
            try {
                returnList.add((ISlashCommand) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return returnList;

    }

    @NotNull
    public static List<IUserContextMenu> userContextMenus() {

        List<IUserContextMenu> returnList = new ArrayList<>();

        getCommands(IUserContextMenu.class).forEach(aClass -> {
            try {
                returnList.add((IUserContextMenu) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return returnList;

    }

    @NotNull
    public static List<IMessageContextMenu> messageContextMenus() {

        List<IMessageContextMenu> returnList = new ArrayList<>();

        getCommands(IMessageContextMenu.class).forEach(aClass -> {
            try {
                returnList.add((IMessageContextMenu) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return returnList;

    }

    @NotNull
    public static List<ConsoleCommand> consoleCommands() {

        List<ConsoleCommand> returnList = new ArrayList<>();

        getCommands(ConsoleCommand.class).forEach(aClass -> {
            try {
                returnList.add((ConsoleCommand) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return returnList;

    }

    @NotNull
    private static List<Class<?>> getCommands(Class<?> clazz) {

        Reflections reflections1 = new Reflections("de.thedesigncraft.discord.bot");

        HashSet<Class<?>> classes = new HashSet<>(reflections1.getSubTypesOf(clazz));

        List<Class<?>> returnList = new ArrayList<>();

        classes.forEach(aClass -> {

            if (!aClass.getSimpleName().endsWith("CommandTemplate")) {

                returnList.add(aClass);

            }

        });

        return returnList;

    }

}
