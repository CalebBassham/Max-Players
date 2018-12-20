package me.calebbassham.maxplayers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class JoinListener implements Listener {

    private MaxPlayers plugin;

    private JoinListener(MaxPlayers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() >= plugin.getMaxPlayers()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Server is full.");
        }
    }

    static JoinListener register(MaxPlayers plugin) {
        var listener = new JoinListener(plugin);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

}
