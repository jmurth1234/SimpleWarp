package net.rymate.SimpleWarp;

// Example plugin
import com.iConomy.*;
import net.rymate.SimpleWarp.SimpleWarpPlugin;


// Bukkit Imports
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class WarpServerListener extends ServerListener {
    // Change "MyPlugin" to the name of your MAIN class file.
    // Let's say my plugins MAIN class is: Register.java
    // I would change "MyPlugin" to "Register"
    private SimpleWarpPlugin plugin;

    public WarpServerListener(SimpleWarpPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        if (plugin.iConomy != null) {
            if (event.getPlugin().getDescription().getName().equals("iConomy")) {
                plugin.iConomy = null;
                System.out.println("[MyPlugin] un-hooked from iConomy.");
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (plugin.iConomy == null) {
            Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
                    plugin.iConomy = (iConomy)iConomy;
                    System.out.println("[MyPlugin] hooked into iConomy.");
                }
            }
        }
    }

}

