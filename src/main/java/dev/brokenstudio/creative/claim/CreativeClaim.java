package dev.brokenstudio.creative.claim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.brokenstudio.creative.claim.commands.CommandHandler;
import dev.brokenstudio.creative.claim.commands.imp.*;
import dev.brokenstudio.creative.claim.config.Settings;
import dev.brokenstudio.creative.claim.manager.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CreativeClaim extends JavaPlugin {

    private final static String PREFIX = "§6Claim §8●• §7";
    private static CreativeClaim instance;
    private Settings settings;
    private RegionContainer regionContainer;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), "settings.json");
        try {
            if(file.createNewFile()){
                settings = new Settings();
                FileWriter writer = new FileWriter(file);
                writer.write(gson.toJson(settings));
                writer.flush();
                writer.close();
            }else{
                FileReader reader = new FileReader(file);
                settings = gson.fromJson(reader, Settings.class);
            }
        } catch (IOException e) {
            System.err.println("CreativeClaim could not be loaded.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        regionManager = new RegionManager();
        CommandHandler claimCommand = new CommandHandler();
        claimCommand.registerSubCommand("claim", new ClaimSubCommand());
        claimCommand.registerSubCommand("delete", new DeleteSubCommand());
        claimCommand.registerSubCommand("trust", new TrustSubCommand());
        claimCommand.registerSubCommand("untrust", new UntrustSubCommand());
        claimCommand.registerSubCommand("my", new HomeSubCommand());
        claimCommand.registerSubCommand("random", new RandomClaimSubCommand());
        getCommand("gs").setExecutor(claimCommand);
    }

    @Override
    public void onDisable() {

    }

    public Settings getSettings() {
        return settings;
    }

    public static CreativeClaim getInstance() {
        return instance;
    }

    public RegionContainer getRegionContainer() {
        return regionContainer;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public static String getPrefix() {
        return PREFIX;
    }
}
