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
import com.nijikokun.register.examples.listeners.server;
import com.nijikokun.register.payment.Method;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    static Properties prop = new Properties(); //creates a new properties file

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
                prop.put("warp-message", "You arrived at your destination!");
                prop.put("warp-fail", "The warp failed to initiate.");
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

            File toPut = new File("lib/Register.jar");
            try {
                download(getServer().getLogger(), new URL("https://github.com/iConomy/Register/blob/master/dist/Register.jar?raw=true"), toPut);
            } catch (IOException ex) {
                System.out.println(ex);
            }
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

    public void download(Logger log, URL url, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (file.exists()) {
            log("Register.jar is already there!");
        } else {
            file.createNewFile();
            final int size = url.openConnection().getContentLength();
            log("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
            final InputStream in = url.openStream();
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            final byte[] buffer = new byte[1024];
            int len, downloaded = 0, msgs = 0;
            final long start = System.currentTimeMillis();
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                downloaded += len;
                if ((int) ((System.currentTimeMillis() - start) / 500) > msgs) {
                    log((int) ((double) downloaded / (double) size * 100d) + "%");
                    msgs++;
                }
            }
            in.close();
            out.close();
            log("Download finished");
        }
    }
}
