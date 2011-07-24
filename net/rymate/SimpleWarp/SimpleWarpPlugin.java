/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.SimpleWarp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.register.payment.Method;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Ryan
 */
public class SimpleWarpPlugin extends JavaPlugin {

    public final String FILE_WARPS = "warps.txt";
    public File m_Folder;
    public File configFile; // = new File(m_Folder + File.separator + "config.properties");
    public HashMap<String, Location> m_warps = new HashMap();
    public int warpPrice;
    public boolean useEconomy;
    public boolean economyFound;
    public static PermissionHandler permissionHandler;
    static Properties prop = new Properties(); //creates a new properties file
    // This is public so we can
    public Method Method = null;
    public Method.MethodAccount MethodAccount;

    public void onEnable() {
        m_Folder = this.getDataFolder();
        configFile = new File(m_Folder + File.separator + "config.properties");
        File homelist = new File(this.m_Folder.getAbsolutePath() + File.separator + "warps.txt");
        if (!configFile.exists()) { //Checks to see if the config file exists, defined above, if it doesn't exist then it will do the following. The ! turns the whole statement around, checking that the file doesn't exist instead of if it exists.
            try { //try catch clause explained below in tutorial
                if (!this.m_Folder.exists()) {
                    System.out.print("[SimpleWarp] Creating Config");
                    this.m_Folder.mkdir();
                }
                configFile.createNewFile(); //creates the file zones.dat
                FileOutputStream out = new FileOutputStream(configFile); //creates a new output steam needed to write to the file
                prop.put("use-economy", "true"); //puts someting in the properties file
                prop.put("price-to-warp", "20"); //puts someting in the properties file
                prop.store(out, "Please edit to your desires."); //You need this line! It stores what you just put into the file and adds a comment.
                out.flush();  //flushes any unoutputed bytes to the file
                out.close(); //Closes the output stream as it is not needed anymore.
                log("Done!");
                if (!homelist.exists()) {
                    log("Missing Warplist, creating the file....");
                    homelist.createNewFile();
                    log("Done!");
                }

            } catch (IOException ex) {
                ex.printStackTrace(); //explained below.
            }
        } else if (!homelist.exists()) {
            log("Missing Warplist, creating the file....");
            try {
                homelist.createNewFile();
                log("Done!");
            } catch (IOException ex) {
                log("FAILED");
                System.out.println(ex);
            }
        } else {
            PluginManager pm = getServer().getPluginManager();
            pm.registerEvent(Event.Type.PLUGIN_ENABLE, new WarpServerListener(this), Priority.Monitor, this);
            pm.registerEvent(Event.Type.PLUGIN_DISABLE, new WarpServerListener(this), Priority.Monitor, this);

            log("Loading warps...");
            if (loadSettings()) {
                log("Done!");
            } else {
                log("FAILED");
            }
            getCommand("listwarps").setExecutor(new ListWarpsCommand(this));
            getCommand("setwarp").setExecutor(new SetWarpsCommand(this));
            getCommand("warp").setExecutor(new WarpCommand(this));
//            getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));
            setupPermissions();

            PluginDescriptionFile pdfFile = getDescription();
            System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        }
    }

    public void onDisable() {
        System.out.println("Meh. SimpleWarp disabled.");
    }

    public void log(String string) {
        System.out.println("[SimpleWarp] " + string);
    }

    public void loadProcedure() throws IOException {
        FileInputStream in = new FileInputStream(configFile);
        prop.load(in);
        useEconomy = Boolean.getBoolean(prop.getProperty("use-economy"));
        warpPrice = Integer.getInteger(prop.getProperty("price-to-warp"));
        in.close();
    }

    //Start configuration stuff
    public boolean saveSettings() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.m_Folder.getAbsolutePath() + File.separator + "warps.txt"));
            for (Map.Entry entry : this.m_warps.entrySet()) {
                Location loc = (Location) entry.getValue();
                if (loc != null) {
                    writer.write((String) entry.getKey() + "," + loc.getX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getPitch() + "," + loc.getYaw() + "," + loc.getWorld().getName());
                    writer.newLine();
                }
            }
            writer.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean loadSettings() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.m_Folder.getAbsolutePath() + File.separator + "warps.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
                    double X = Double.parseDouble(values[1]);
                    double Y = Double.parseDouble(values[2]);
                    double Z = Double.parseDouble(values[3]);
                    float pitch = Float.parseFloat(values[4]);
                    float yaw = Float.parseFloat(values[5]);

                    World world = getServer().getWorld(values[6]);
                    if (world != null) {
                        this.m_warps.put(values[0], new Location(world, X, Y, Z, yaw, pitch));
                    }
                }
                line = reader.readLine();
            }
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public String[] getList(String name) {
        Scanner scanner;
        List<String> list = new ArrayList<String>();
        try {
            scanner = new Scanner(new FileReader(this.m_Folder.getAbsolutePath() + File.separator + "warps.txt"));

            try {

                // first use a Scanner to get each line
                int i = 0;
                while (scanner.hasNextLine()) {

                    String[] elements = scanner.nextLine().split(",");

                    if (elements[0].matches(".*" + name + ".*")) {
                        list.add(elements[0]);
                        i++;
                    }

                    if (i >= 8) {
                        break;
                    }

                }

            } catch (Exception e) {
                System.out.println("Warp:" + e.getMessage());
                e.printStackTrace();
            } finally {

                scanner.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Warp:" + e.getMessage());
        }

        String[] locations = new String[list.size()];
        list.toArray(locations);

        return locations;
    }
    //end Configuration stuff

    private void setupPermissions() {
        if (permissionHandler != null) {
            return;
        }

        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

        if (permissionsPlugin == null) {
            log("Permission system not detected, defaulting to OP");
            return;
        }

        permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        log("Found and will use plugin " + ((Permissions) permissionsPlugin).getDescription().getFullName());
    }
}
