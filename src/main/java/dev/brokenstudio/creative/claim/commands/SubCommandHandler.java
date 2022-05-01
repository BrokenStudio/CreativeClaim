package dev.brokenstudio.creative.claim.commands;

import dev.brokenstudio.creative.claim.CreativeClaim;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public abstract class SubCommandHandler implements SubCommand{

    private final HashMap<String, SubCommand> subCommandHashMap;

    {
        subCommandHashMap = new HashMap<>();
    }

    @Override
    public void run(CommandSender sender, String[] args) {
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
            }else sendHelp(sender);
        }else sendHelp(sender);
    }

    public String getName(){
        return "";
    }

    private void sendHelp(CommandSender sender){
        StringBuilder builder = new StringBuilder(CreativeClaim.getPrefix() + "/gs " + getName());
        subCommandHashMap.forEach((key, value) -> {
            builder.append("\n  ").append(key).append( " - ").append(value.getDesc());
        });
        sender.sendMessage(builder.toString());
    }

    public void registerSubCommand(String name, SubCommand command){
        subCommandHashMap.put(name, command);
    }

}
