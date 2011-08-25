/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.iConomy.*;
import com.iConomy.system.Account;
import com.iConomy.system.Holdings;
import net.rymate.SimpleWarp.SimpleWarpPlugin;
import org.bukkit.block.Block;

/**
 *
 * @author Ryan
 */
public class DeleteWarpCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;
    private iConomy iConomy;


    public DeleteWarpCommand(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
        plugin.iConomy = iConomy;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
