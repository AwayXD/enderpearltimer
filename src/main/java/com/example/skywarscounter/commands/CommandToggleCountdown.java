package com.example.skywarscounter.commands;

import com.example.skywarscounter.SkyWarsCounter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandToggleCountdown extends CommandBase {
    @Override
    public String getCommandName() {
        return "enderpearltimer";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/enderpearltimer toggle";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            SkyWarsCounter.instance.toggleCountdown();
            if (SkyWarsCounter.instance.isCountdownEnabled()) {
                sender.addChatMessage(new ChatComponentText("Ender Pearl countdown enabled."));
            } else {
                sender.addChatMessage(new ChatComponentText("Ender Pearl countdown disabled."));
            }
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
