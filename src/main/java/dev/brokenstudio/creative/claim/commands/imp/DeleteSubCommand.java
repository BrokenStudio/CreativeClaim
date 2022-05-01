package dev.brokenstudio.creative.claim.commands.imp;

import dev.brokenstudio.creative.claim.CreativeClaim;
import dev.brokenstudio.creative.claim.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class DeleteSubCommand implements SubCommand {

    private final static HashSet<UUID> toDelete = new HashSet<>();

    @Override
    public void run(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if(args.length == 0){
            if(toDelete.contains(player.getUniqueId())){
                player.sendMessage(CreativeClaim.getPrefix() + "Du befindest dich bereits in einem aufgabe Vorgang.");
                return;
            }
            toDelete.add(player.getUniqueId());
            player.sendMessage(CreativeClaim.getPrefix() + "Biite nutze \"/gs delete confirm\" um die Aufgabe abzuschliessen.");
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("confirm")){
                if(!toDelete.contains(player.getUniqueId())){
                    player.sendMessage(CreativeClaim.getPrefix() + "Du musst zuerst eine Gebietsaufgabe starten.");
                    return;
                }
                CreativeClaim.getInstance().getRegionManager().deleteRegion(player);
                toDelete.remove(player);
                player.sendMessage(CreativeClaim.getPrefix() + "Du hast das Gebiet aufgegeben.");
            }else{
                sender.sendMessage(CreativeClaim.getPrefix() + "/gs <delete> (confirm)");
            }
        }else {
            sender.sendMessage(CreativeClaim.getPrefix() + "/gs <delete> (confirm)");
        }

    }

    @Override
    public String getDesc() {
        return "Gebe den Besitz auf.";
    }
}
