package com.songoda.epicbuckets.command;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {

    public enum ReturnType { SUCCESS, FAILURE, SYNTAX_ERROR }

    private final AbstractCommand parent;

    private List<String> command;

    private List<String> subCommand = new ArrayList<>();

    private final boolean noConsole;

    protected AbstractCommand(AbstractCommand parent, boolean noConsole, String... command) {
        if (parent != null) {
            this.subCommand = Arrays.asList(command);
        } else {
            this.command = Arrays.asList(command);
        }
        this.parent = parent;
        this.noConsole = noConsole;
    }

    public AbstractCommand getParent() {
        return parent;
    }

    public List<String> getCommand() {
        System.out.println(command + " " + (command != null && command.contains("epicbuckets")));
        return command;
    }

    public List<String> getSubCommand() {
        return subCommand;
    }

    public void addSubCommand(String command) {
        subCommand.add(command);
    }

    protected abstract ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    public boolean isNoConsole() {
        return noConsole;
    }
}
