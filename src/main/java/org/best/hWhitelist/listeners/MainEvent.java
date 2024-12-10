package org.best.hWhitelist.listeners;

import org.best.hWhitelist.HWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class MainEvent implements Listener {

    private final HWhitelist plugin;
    public MainEvent(HWhitelist plugin){
        this.plugin = plugin;
    }

    public String applyplaceholders(String command, Player player, String hostname){

        // my way of adding placeholders.
        // only for this plugin.
        // might add an api for this.
        // fuck

        StringBuilder builder = new StringBuilder();
        builder.append(command); // add the command.
        String return_var = builder.toString();
        if(builder.toString().contains("%player%")){
            return_var = return_var.toString().replace("%player%", player.getName());
        }
        if(builder.toString().contains("%player-ip%")){
            return_var = return_var.toString().replace("%player-ip%", player.getAddress().getAddress().getHostName());
        }
        if(builder.toString().contains("%hostname%")){
            return_var = return_var.toString().replace("%hostname%", hostname);
        }
        return return_var;
    }

    @EventHandler
    public void LoginEvent(PlayerLoginEvent e){
        Boolean anything_but_these_enabled =  plugin.getConfig().getBoolean("anything_but_these.enabled");
        Boolean only_from_this_enabled =  plugin.getConfig().getBoolean("only_from_this.enabled");
        Boolean kick = false;
        String hostname = e.getHostname();
        if(anything_but_these_enabled) {
            List<String> first_hosts = (List<String>) plugin.getConfig().getList("anything_but_these.hosts");
            for (String host : first_hosts) {
                if (hostname.equalsIgnoreCase(host)) {
                    kick = true;
                }
            }
        } else if(only_from_this_enabled){
                List<String> second_hosts = (List<String>) plugin.getConfig().getList("only_from_this.hosts");
                if(anything_but_these_enabled) {
                    plugin.getLogger().warning("[!] You have both options set to 'Enabled'");
                    plugin.getLogger().warning("[!] This might cause issues in the future.");
                }
                for(String host : second_hosts){
                    if(!hostname.equalsIgnoreCase(host)){
                        kick = true;
                    }
                }
        }
        if(kick && anything_but_these_enabled){
            List<String> commands = (List<String>) plugin.getConfig().getList("anything_but_these.commands");
            String kick_message = plugin.getConfig().getString("kick_message").replace("&", "§");
            for(String command : commands){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), applyplaceholders(command, e.getPlayer(), hostname)); //simple yes.
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, kick_message);
        }
        if(kick && only_from_this_enabled){
            List<String> commands = (List<String>) plugin.getConfig().getList("only_from_this_enabled.commands");
            String kick_message = plugin.getConfig().getString("kick_message").replace("&", "§");
            for(String command : commands){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), applyplaceholders(command, e.getPlayer(), hostname));
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, kick_message);
        }
    }
}
