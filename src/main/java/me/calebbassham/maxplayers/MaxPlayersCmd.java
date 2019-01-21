package me.calebbassham.maxplayers;

import me.calebbassham.pluginmessageformat.PluginMessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static me.calebbassham.pluginmessageformat.PluginMessageFormat.*;

public class MaxPlayersCmd implements CommandExecutor, TabCompleter {

    private MaxPlayers plugin;

    private MaxPlayersCmd(MaxPlayers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1 || args.length > 2) {
            return false;
        }

        int players;
        try {
            players = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (players < 0) {
            return false;
        }

        var kick = false;
        if (args.length == 2) {
            try {
                kick = Boolean.parseBoolean(args[1]);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        try {
            plugin.setMaxPlayers(players, kick);
        } catch (IllegalAccessException | InvocationTargetException e) {
            sender.sendMessage(getErrorPrefix() + "Failed to change the player limit.");
            e.printStackTrace();
            return true;
        }

        sender.sendMessage(getPrefix() + "The " + getMainColorPallet().getHighlightTextColor() + "max number of players" +
                getMainColorPallet().getPrimaryTextColor() + " allowed to join is now " + getMainColorPallet().getValueTextColor() +
                players + getMainColorPallet().getPrimaryTextColor() + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }

    static MaxPlayersCmd register(MaxPlayers plugin) {
        var cmd = plugin.getCommand("maxplayers");
        var instance = new MaxPlayersCmd(plugin);
        cmd.setExecutor(instance);
        cmd.setTabCompleter(instance);
        return instance;
    }
}
