package me.calebbassham.maxplayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MaxPlayers extends JavaPlugin {

    private int maxPlayers;

    @Override
    public void onEnable() {
        this.maxPlayers = Bukkit.getMaxPlayers();
        MaxPlayersListener.register(this);
        MaxPlayersCmd.register(this);
    }

    /**
     * Returns the number of players that can join the server.
     * @return The number of players that can join the server.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set the number of players allowed to join the server.
     * @param maxPlayers The number of players to allow to join the server.
     * @param kick If there are more players online than the new limit allows, should players be kicked to follow the new limit?
     * @param kickMessage The message to kick a player with.
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     */
    public void setMaxPlayers(int maxPlayers, boolean kick, String kickMessage) throws IllegalArgumentException {
        if (maxPlayers < 0) throw new IllegalArgumentException("maxPlayers cannot be less than 0");
        if (maxPlayers > Bukkit.getMaxPlayers()) throw new IllegalArgumentException("maxPlayers cannot be greater than Bukkit.getMaxPlayers()");
        if (kick) {
            while (Bukkit.getOnlinePlayers().size() > maxPlayers) {
                Bukkit.getOnlinePlayers().toArray(new Player[0])[0].kickPlayer(kickMessage);
            }
        }
        this.maxPlayers = maxPlayers;
    }

    /**
     * Set the number of players allowed to join the server.
     * @param maxPlayers The number of players to allow to join the server.
     * @param kick If there are more players online than the new limit allows, should players be kicked to follow the new limit?
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     */
    public void setMaxPlayers(int maxPlayers, boolean kick) throws IllegalArgumentException {
        setMaxPlayers(maxPlayers, kick, kick ? "The max player limit has been reduced." : null);
    }

    /**
     * Sets the number of players allowed to join the server. Does not kick players if a new limit is set.
     * @param maxPlayers The number of players to allow to join the server.
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     */
    public void setMaxPlayers(int maxPlayers) throws IllegalArgumentException {
        setMaxPlayers(maxPlayers, false);
    }

}
