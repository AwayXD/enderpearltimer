package com.example.skywarscounter.commands;

import com.example.skywarscounter.SkyWarsCounter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandSelectSlot extends CommandBase {
    @Override
    public String getCommandName() {
        return "enderpearl";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/enderpearl slot [#]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("slot")) {
            try {
                int slot = Integer.parseInt(args[1]);
                if (slot >= 1 && slot <= 9) {
                    SkyWarsCounter.instance.setSelectedSlot(slot - 1); // Convert to 0-based index
                    sender.addChatMessage(new ChatComponentText("Ender Pearl detection set to slot " + slot));
                } else {
                    sender.addChatMessage(new ChatComponentText("Slot must be between 1 and 9."));
                }
            } catch (NumberFormatException e) {
                sender.addChatMessage(new ChatComponentText("Invalid slot number."));
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
