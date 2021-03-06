package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UntrustSubCommand implements SubCommand {

    @Override
    public void run(CommandSender sender, String[] args) {

        if(args.length != 1){
            sender.sendMessage(CreativeClaim.getPrefix() + "/gs untrust <player>");
            return;
        }

        //TODO UUID FETCHER
        CreativeClaim.getInstance().getRegionManager().untrustPlayer((Player) sender, Bukkit.getPlayer(args[0]).getUniqueId());

    }

    @Override
    public String getDesc() {
        return "Nehme einem anderen Spieler Rechte in deinen Chunks.";
    }

}
