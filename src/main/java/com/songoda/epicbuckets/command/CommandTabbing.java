package com.songoda.epicbuckets.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import com.songoda.epicbuckets.EpicBuckets;

public class CommandTabbing {

    public static void registerCommandCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = EpicBuckets.getInstance().getCommandManager().getCommandCompletions();

        commandCompletions.registerCompletion("")
    }

}
