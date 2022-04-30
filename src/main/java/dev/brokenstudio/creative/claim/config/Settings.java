package dev.brokenstudio.creative.claim.config;

import java.util.HashSet;

public class Settings {

    private final HashSet<String> allowedWorlds;
    private final int maxClaims;

    public Settings() {
        allowedWorlds = new HashSet<>();
        maxClaims = 4;
    }

    public HashSet<String> getAllowedWorlds() {
        return allowedWorlds;
    }

    public int maxClaims(){
        return maxClaims;
    }

}
