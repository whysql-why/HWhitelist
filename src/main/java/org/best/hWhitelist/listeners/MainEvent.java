package org.best.hWhitelist.listeners;

import org.best.hWhitelist.HWhitelist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class MainEvent implements Listener {

    private final HWhitelist plugin;
    public MainEvent(HWhitelist plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void LoginEvent(PlayerLoginEvent e){
        Boolean anything_but_these_enabled =  plugin.getConfig().getBoolean("anything_but_these.enabled");
        Boolean only_from_this_enabled =  plugin.getConfig().getBoolean("only_from_this.enabled");
        Boolean kick = false;
        Boolean stay = false;
        String hostname = e.getHostname();
        if(anything_but_these_enabled){
            List<String> first_hosts = (List<String>) plugin.getConfig().getList("anything_but_these.hosts");
            for(String host : first_hosts){
                if(hostname.equalsIgnoreCase(host)){
                    kick = true;
                }
            }
            if(only_from_this_enabled){
                List<String> second_hosts = (List<String>) plugin.getConfig().getList("only_from_this.hosts");
                plugin.getLogger().warning("[!] You have both options set to 'Enabled'");
                plugin.getLogger().warning("[!] This might cause issues in the future.");
                for(String host : second_hosts){
                    if(!hostname.equalsIgnoreCase(host)){
                        kick = true;
                    }
                }
            }
        }
        if(only_from_this_enabled){
            List<String> hosts = (List<String>) plugin.getConfig().getList("only_from_this.hosts");
            for (String host : hosts){
                if(!hostname.equalsIgnoreCase(host)){
                    kick = true;
                }
            }
        }
        if(kick && !stay){
            String kick_message = plugin.getConfig().getString("kick_message").replace("&", "§");
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, kick_message);
        }
    }
}
