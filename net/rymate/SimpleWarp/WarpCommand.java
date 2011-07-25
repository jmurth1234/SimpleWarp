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
import com.nijikokun.register.payment.Method;

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

        if ((plugin).permissionHandler.has(player, "warp.go." + args[0])) {
            if (args.length < 1) {
                return false;
            } else if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not an in-game player!");
                return true;
            }

            Location loc = (Location) plugin.m_warps.get(args[0]);
            if ((loc != null) && (plugin.Method != null) && (plugin.useEconomy = true)) {
                if ((plugin.Method.hasAccount(player.getName())) && plugin.MethodAccount.hasOver(plugin.warpPrice)) {
                    plugin.MethodAccount.subtract(plugin.warpPrice);
                    player.teleportTo(loc);
                    player.sendMessage(ChatColor.GREEN + "You have arrived at your destination! " + plugin.warpPrice + "was deducted from your money.");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough money :(");
                }
            } else if (loc != null) {
                player.teleportTo(loc);
                player.sendMessage(ChatColor.GREEN + "You have arrived at your destination!");
            } else {
                player.sendMessage(ChatColor.RED + "There is no warp with that name!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have the permissions to use this command.");
        }
        return true;
    }
}
