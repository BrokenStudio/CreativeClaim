package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeSubCommand implements SubCommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        Location location = CreativeClaim.getInstance().getRegionManager().getHomeLocation(((Player) sender).getUniqueId());
        if(location != null){
            ((Player) sender).teleport(location);
        }else{
            sender.sendMessage(CreativeClaim.getPrefix() + "Du besitzt keinen Chunk.");
        }
    }

    @Override
    public String getDesc() {
        return "Teleportiere dich zu deinem Chunk.";
    }
}
