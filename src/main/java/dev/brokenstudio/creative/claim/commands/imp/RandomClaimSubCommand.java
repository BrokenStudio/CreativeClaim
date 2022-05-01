package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class RandomClaimSubCommand implements SubCommand {

    private final Random random = new Random();


    @Override
    public void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Chunk chunk = getFreeChunk(player);
        Location l1 = chunk.getBlock(7,0,7).getLocation();
        Location l2 = chunk.getWorld().getHighestBlockAt(l1).getLocation();
        player.teleport(l2.add(0,1,0));
    }

    private Chunk getFreeChunk(Player player){
        int x,z;
        x = random.nextInt();
        z = random.nextInt();
        World w = player.getWorld();
        Chunk chunk = w.getChunkAt(x,z);
        if(CreativeClaim.getInstance().getRegionManager().isClaimed(chunk)){
            return getFreeChunk(player);
        }
        return chunk;
    }

    @Override
    public String getDesc() {
        return "Teleportiert ich zu einem freien Chunk";
    }
}
