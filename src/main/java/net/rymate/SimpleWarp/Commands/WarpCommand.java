package net.rymate.SimpleWarp.Commands;

import net.rymate.SimpleWarp.SimpleWarpPlugin;
import net.rymate.SimpleWarp.WarpFileHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ryan
 */
public class WarpCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;


    public WarpCommand(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
    }

    WarpFileHandler warp = new WarpFileHandler(this.plugin);

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission("SimpleWarp.warp")) {
            if (args.length < 1) {
                return false;
            } else if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not an in-game player!");
                return true;
            }

            Location loc = (Location) warp.m_warps.get(args[0]);
            if (loc != null) {
                warpPlayer(player, loc);
                player.sendMessage(ChatColor.GREEN + "You have arrived at your destination!");
            } else {
                player.sendMessage(ChatColor.RED + "There is no warp with that name!");
            }
        } else if (!player.hasPermission("SimpleWarp.warp")) {
            player.sendMessage(ChatColor.RED + "You do not have the permissions to use this command.");
        } else {
            player.sendMessage(ChatColor.RED + "There is no warp with that name!");
        }
        return true;
    }

    private void warpPlayer(Player player, Location loc) {
        Block block = loc.getBlock();
        while (block.getRelative(0, 1, 0).getTypeId() != 0 && block.getY() < 126) {
            if (block.getRelative(0, 2, 0).getTypeId() != 0) {
                block = block.getRelative(0, 3, 0);
            } else {
                block = block.getRelative(0, 2, 0);
            }
        }
        player.teleport(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), loc.getYaw(), loc.getPitch()));
    }
}
