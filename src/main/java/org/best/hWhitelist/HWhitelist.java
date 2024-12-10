package org.best.hWhitelist;

import org.best.hWhitelist.listeners.MainEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class HWhitelist extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MainEvent(this), this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
