/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp.Commands;

import net.rymate.SimpleWarp.SimpleWarpPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Ryan
 */
public class DeleteWarpCommand implements CommandExecutor {

    private final SimpleWarpPlugin plugin;


    public DeleteWarpCommand(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
