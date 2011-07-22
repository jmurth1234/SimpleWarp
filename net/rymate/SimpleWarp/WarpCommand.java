/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Ryan
 */
class WarpCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;

    public WarpCommand(SimpleWarpPlugin plugin) {
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

        Location loc = (Location) plugin.m_warps.get(args[0]);
        if (loc != null) {
            player.teleportTo(loc);
            player.sendMessage(ChatColor.GREEN + "You have arrived at your destination!");
        } else {
            player.sendMessage(ChatColor.RED + "There is no warp with that name!");
        }
        return true;
    }
}
