/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp;

import net.rymate.SimpleWarp.Commands.DeleteWarpCommand;
import net.rymate.SimpleWarp.Commands.ListWarpsCommand;
import net.rymate.SimpleWarp.Commands.SetWarpsCommand;
import net.rymate.SimpleWarp.Commands.WarpCommand;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


/**
 * @author Ryan
 */
public class SimpleWarpPlugin extends JavaPlugin {

    public final String FILE_WARPS = "warps.txt";
    public File m_Folder;
    public HashMap<String, Location> m_warps = new HashMap();
    static Properties prop = new Properties(); //creates a new properties file
    WarpFileHandler warp = new WarpFileHandler();


    public void onEnable() {
        m_Folder = this.getDataFolder();
        File homelist = new File(this.m_Folder.getAbsolutePath() + File.separator + "warps.txt");
        if (!homelist.exists()) {
            log("Missing Warplist, creating the file....");
            try {
                homelist.createNewFile();
                log("Done!");
            } catch (IOException ex) {
                log("FAILED");
                System.out.println(ex);
            }
        }
            log("Loading warps...");
            if (warp.loadSettings()) {
                log("Done!");
            } else {
                log("FAILED");
            }
            getCommand("listwarps").setExecutor(new ListWarpsCommand(this));
            getCommand("setwarp").setExecutor(new SetWarpsCommand(this));
            getCommand("warp").setExecutor(new WarpCommand(this));
            getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));

            PluginDescriptionFile pdfFile = getDescription();
            System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        }


    public void onDisable() {
        System.out.println("Meh. SimpleWarp disabled.");
    }

    public void log(String string) {
        System.out.println("[SimpleWarp] " + string);
    }

}
