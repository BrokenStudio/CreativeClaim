package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrustSubCommand implements SubCommand {

    @Override
    public void run(CommandSender sender, String[] args) {

        if(args.length != 1){
            sender.sendMessage(CreativeClaim.getPrefix() + "/gs trust <player>");
            return;
        }

        //TODO CHECK IF PLAYER IS ONLINE
        CreativeClaim.getInstance().getRegionManager().trustPlayer((Player) sender, Bukkit.getPlayer(args[0]));

    }

    @Override
    public String getDesc() {
        return "Gebe einem anderen Spieler Rechte in deinen Chunks.";
    }
}
