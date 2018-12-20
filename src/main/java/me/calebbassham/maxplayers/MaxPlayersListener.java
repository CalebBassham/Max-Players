package me.calebbassham.maxplayers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class MaxPlayersListener implements Listener {

    private MaxPlayers plugin;

    private MaxPlayersListener(MaxPlayers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() >= plugin.getMaxPlayers()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Server is full.");
        }
    }

    @EventHandler
    public void onServerListInfo(ServerListPingEvent e) {
        e.setMaxPlayers(plugin.getMaxPlayers());
    }

    static MaxPlayersListener register(MaxPlayers plugin) {
        var listener = new MaxPlayersListener(plugin);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

}
