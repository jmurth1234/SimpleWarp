package net.rymate.SimpleWarp.Commands;

import net.rymate.SimpleWarp.SimpleWarpPlugin;
import net.rymate.SimpleWarp.WarpFileHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ryan
 */
public class ListWarpsCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;
    WarpFileHandler warp = new WarpFileHandler();

    public ListWarpsCommand(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("SimpleWarp.listwarps")) {

            String filter = args.length >= 1 ? args[0] : "";
            String[] locs = warp.getList(filter);
            if ((locs != null) && (locs.length >= 1)) {
                for (String i : locs) {
                    player.sendMessage(i);
                }
            } else {
                player.sendMessage("No Warps Found");
            }

            System.out.println(player.getName() + " listed warps ");
        } else {
            player.sendMessage("You do not have the permissions to do this.");
        }
        return true;
    }
}
