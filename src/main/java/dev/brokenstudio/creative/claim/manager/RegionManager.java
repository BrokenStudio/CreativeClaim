package dev.brokenstudio.creative.claim.manager;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.brokenstudio.creative.claim.CreativeClaim;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class RegionManager implements DualManager<ProtectedRegion, World> {

    private final HashMap<String, UUID> claimedChunks;
    private final HashMap<UUID, HashSet<String>> playerChunks;
    private final HashMap<UUID, Location> homeLocation;

    {
        claimedChunks = new HashMap<>();
        playerChunks = new HashMap<>();
        homeLocation = new HashMap<>();
    }

    @Override
    public ProtectedRegion get(String key, World world) {
        com.sk89q.worldguard.protection.managers.RegionManager regions = CreativeClaim.getInstance().getRegionContainer().get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(world.getName()));
        assert regions != null;
        return regions.getRegion(key);
    }

    @Override
    public void set(String key, World world, ProtectedRegion protectedRegion) {
        com.sk89q.worldguard.protection.managers.RegionManager regions = CreativeClaim.getInstance().getRegionContainer().get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(world.getName()));
        assert regions != null;
        regions.addRegion(protectedRegion);
    }

    public void claimChunk(Player player){
        if(!CreativeClaim.getInstance().getSettings().getAllowedWorlds().contains(player.getWorld().getName())){
            player.sendMessage(CreativeClaim.getPrefix() + "In dieser Welt darfst du keine Chunks in besitz nehmen.");
            return;
        }
        HashSet<String> claimedChunksSet = playerChunks.getOrDefault(player.getUniqueId(), new HashSet<>());
        if(claimedChunksSet.size() >= CreativeClaim.getInstance().getSettings().maxClaims()){
            player.sendMessage(CreativeClaim.getPrefix() + "Du besitzt schon die maximale anzahl an Chunks.");
            return;
        }
        //TODO check for Money
        Chunk chunk = player.getLocation().getChunk();
        String id = getChunkId(chunk);
        if(claimedChunks.containsKey(id)){
            if(claimedChunks.get(id) != player.getUniqueId()){
                player.sendMessage(CreativeClaim.getPrefix() + "Dieser Chunk ist schon im besitz eines Spielers.");
                return;
            }
        }

        Location loc1, loc2;
        loc1 = chunk.getBlock(0, -64, 0).getLocation();
        loc2 = chunk.getBlock(15, -319, 15).getLocation();
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(id, BlockVector3.at(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ()), BlockVector3.at(loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ()));
        region.getOwners().addPlayer(player.getUniqueId());
        set(player.getUniqueId() + ":" + id, chunk.getWorld(), region);
        homeLocation.putIfAbsent(player.getUniqueId(), Objects.requireNonNull(loc1.getWorld()).getHighestBlockAt(loc1).getLocation().add(0,1,0));
        claimedChunksSet.add(id);
        playerChunks.put(player.getUniqueId(), claimedChunksSet);
        claimedChunks.put(id, player.getUniqueId());
        //TODO remove money
        player.sendMessage(CreativeClaim.getPrefix() + "Du bist nun Besitz des Chunks mit der ID " + id + ".");
    }

    //TODO add claimRandomRegion Method

    public void trustPlayer(Player owner, Player to){
        if(!playerChunks.containsKey(owner.getUniqueId())){
            owner.sendMessage(CreativeClaim.getPrefix() + "Du besitzt keine Chunks.");
            return;
        }
        HashSet<String> chunks = playerChunks.get(owner.getUniqueId());
        chunks.forEach(cr -> {
            com.sk89q.worldguard.protection.managers.RegionManager regions = CreativeClaim.getInstance().getRegionContainer().get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(cr.split(":")[0]));
            assert regions != null;
            Objects.requireNonNull(regions.getRegion(cr)).getOwners().addPlayer(to.getUniqueId());
        });
        owner.sendMessage(CreativeClaim.getPrefix() + "Der Spieler " + to.getName() + " hat nun Zugriff auf deine Chunks.");
        to.sendMessage(CreativeClaim.getPrefix() + "Du hast nun Zugriff auf die Chunks von " + owner.getName() + ".");
    }

    public void untrustPlayer(Player owner, UUID to){
        if(!playerChunks.containsKey(owner.getUniqueId())){
            owner.sendMessage(CreativeClaim.getPrefix() + "Du besitzt keine Chunks.");
            return;
        }
        HashSet<String> chunks = playerChunks.get(owner.getUniqueId());
        chunks.forEach(cr -> {
            com.sk89q.worldguard.protection.managers.RegionManager regions = CreativeClaim.getInstance().getRegionContainer().get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(cr.split(":")[0]));
            assert regions != null;
            Objects.requireNonNull(regions.getRegion(cr)).getOwners().addPlayer(to);
        });
        owner.sendMessage(CreativeClaim.getPrefix() + "Dieser Spieler  hat nun keinen Zugriff mehr auf deine Chunks.");
    }

    public void deleteRegion(Player player){
        if(!playerChunks.containsKey(player.getUniqueId())){
            player.sendMessage(CreativeClaim.getPrefix() + "Du besitzt keine Chunks.");
            return;
        }
        HashSet<String> chunks = playerChunks.get(player.getUniqueId());
        chunks.forEach(this::deleteChunk);
        playerChunks.remove(player.getUniqueId());
        homeLocation.remove(player.getUniqueId());
        player.sendMessage(CreativeClaim.getPrefix() + "Alle Chunks von dir wurden wieder freigegeben.");
    }

    private void deleteChunk(String id){
        Objects.requireNonNull(CreativeClaim.getInstance().getRegionContainer().get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(id.split(":")[0]))).removeRegion(id);
        claimedChunks.remove(id);
    }

    private String getChunkId(Chunk chunk) {
        return chunk.getWorld() + ":" + chunk.getX() + ":" + chunk.getZ();
    }

    public Location getHomeLocation(UUID uuid) {
        return homeLocation.get(uuid);
    }

    public boolean isClaimed(Chunk chunk){
        return claimedChunks.containsKey(getChunkId(chunk));
    }

}
