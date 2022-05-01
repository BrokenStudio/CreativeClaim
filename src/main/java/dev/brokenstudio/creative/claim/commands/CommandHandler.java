package dev.brokenstudio.creative.claim.commands;

import dev.brokenstudio.creative.claim.CreativeClaim;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    private final HashMap<String, SubCommand> subCommandHashMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender == Bukkit.getConsoleSender()){
            sender.sendMessage(CreativeClaim.getPrefix() + "Dieser Command ist nur für Spieler.");
            return true;
        }

        if(args.length > 0){
            String sub = args[0];
            if(subCommandHashMap.containsKey(sub)){
                String[] newArgs;
                if(args.length == 1){
                    newArgs = new String[0];
                }else{
                    newArgs = new String[args.length-1];
                    for(int i = 0; i < newArgs.length; i++){
                        newArgs[i] = args[i+1];
                    }
                    subCommandHashMap.get(sub).run(sender, newArgs);
                }
            }else sendHelp(sender, label);
        }else sendHelp(sender, label);


        return true;
    }

    private void sendHelp(CommandSender sender, String usedCommand){
        StringBuilder builder = new StringBuilder(CreativeClaim.getPrefix() + "/" + usedCommand);
        subCommandHashMap.forEach((key, value) -> {
            builder.append("\n  ").append(key).append( " - ").append(value.getDesc());
        });
        sender.sendMessage(builder.toString());
    }

    public void registerSubCommand(String name, SubCommand command){
        subCommandHashMap.put(name, command);
    }

}
