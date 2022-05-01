package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimSubCommand implements SubCommand {

    @Override
    public void run(CommandSender sender, String[] args) {

        CreativeClaim.getInstance().getRegionManager().claimChunk((Player) sender);

    }

    @Override
    public String getDesc() {
        return "Nehme einen Chunk in besitz";
    }
}
