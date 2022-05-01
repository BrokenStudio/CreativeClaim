package dev.brokenstudio.creative.claim.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    void run(CommandSender sender, String[] args);

    default String getDesc(){
        return "";
    }

}
