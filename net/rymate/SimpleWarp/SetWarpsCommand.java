/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Ryan
 */
class SetWarpsCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;

    public SetWarpsCommand(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) {
            return false;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not an in-game player!");
            return true;
        }

        if (((plugin).permissionHandler.has(player, "warp.set")) && ((plugin).permissionsUsage = true)) {
            plugin.m_warps.put(args[0], player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Warp Created!");
            plugin.saveSettings();
            return true;
        } else if ((player.isOp()) && ((plugin).permissionsUsage = false)) {
            plugin.m_warps.put(args[0], player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Warp Created!");
            plugin.saveSettings();
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You do not have the permissions to use this command.");
        }
        return true;
    }
}
