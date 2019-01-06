package me.calebbassham.maxplayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MaxPlayers extends JavaPlugin {

    private Method CRAFT_SERVER_GET_HANDLE_METHOD;

    private Field PLAYER_LIST_MAX_PLAYERS_FIELD;

    @Override
    public void onEnable() {
        try {
            setupReflection();
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        MaxPlayersCmd.register(this);
    }

    private void setupReflection() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String nmsPackage = "net.minecraft.server." + version + ".";
        String cbPackage = "org.bukkit.craftbukkit." + version + ".";

        Class craftServerClass = Class.forName(cbPackage + "CraftServer");
        CRAFT_SERVER_GET_HANDLE_METHOD = craftServerClass.getDeclaredMethod("getHandle");
        CRAFT_SERVER_GET_HANDLE_METHOD.setAccessible(true);

        Class playerListClass = Class.forName(nmsPackage + "PlayerList");
        PLAYER_LIST_MAX_PLAYERS_FIELD = playerListClass.getDeclaredField("maxPlayers");
        PLAYER_LIST_MAX_PLAYERS_FIELD.setAccessible(true);
    }

    /**
     * Set the number of players allowed to join the server.
     *
     * @param maxPlayers  The number of players to allow to join the server.
     * @param kick        If there are more players online than the new limit allows, should players be kicked to follow the new limit?
     * @param kickMessage The message to kick a player with.
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     * @throws IllegalAccessException   Reflection...
     * @throws InvocationTargetException Reflection...
     */
    public void setMaxPlayers(int maxPlayers, boolean kick, String kickMessage) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (maxPlayers < 0) throw new IllegalArgumentException("maxPlayers cannot be less than 0");
        if (kick) {
            while (Bukkit.getOnlinePlayers().size() > maxPlayers) {
                Bukkit.getOnlinePlayers().toArray(new Player[0])[0].kickPlayer(kickMessage);
            }
        }

        PLAYER_LIST_MAX_PLAYERS_FIELD.set(CRAFT_SERVER_GET_HANDLE_METHOD.invoke(Bukkit.getServer()), maxPlayers);
    }

    /**
     * Set the number of players allowed to join the server.
     *
     * @param maxPlayers The number of players to allow to join the server.
     * @param kick       If there are more players online than the new limit allows, should players be kicked to follow the new limit?
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     * @throws IllegalAccessException   Reflection...
     * @throws InvocationTargetException Reflection...
     */
    public void setMaxPlayers(int maxPlayers, boolean kick) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        setMaxPlayers(maxPlayers, kick, kick ? "The max player limit has been reduced." : null);
    }

    /**
     * Sets the number of players allowed to join the server. Does not kick players if a new limit is set.
     *
     * @param maxPlayers The number of players to allow to join the server.
     * @throws IllegalArgumentException If maxPlayers is greater than Bukkit.getMaxPlayers() or if maxPlayers is less than 0.
     * @throws IllegalAccessException   Reflection...
     * @throws InvocationTargetException Reflection...
     */
    public void setMaxPlayers(int maxPlayers) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        setMaxPlayers(maxPlayers, false);
    }

}
