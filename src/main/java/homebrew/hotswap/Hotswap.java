package homebrew.hotswap;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class Hotswap extends JavaPlugin {

    //Instance
    private static Hotswap instance;

    //config
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // set instance
        instance = this;

        // Variables
        List<String> worlds;

        //load config
        config.options().copyDefaults(true);
        saveDefaultConfig();

        // Code to run on initial installation
        if (config.getBoolean("first")) {
            // Get the server's worlds
            worlds = new LinkedList<String>();
            for (World world : getServer().getWorlds()) {
                // Add each world name
                worlds.add(world.getName());
                getLogger().info("Adding: " + world.getName());
            }
            config.createSection("worlds.default");
            config.set("worlds.default", worlds);
            config.set("first", false);
            saveConfig();

        } else {
            worlds = (List<String>) config.getList("worlds.default");
        }
        if (!config.getBoolean("worlds.all")) {
            for (String world : worlds) {
                World temp = getServer().getWorld(world);
                if (temp != null) {
                    getServer().getPluginManager().registerEvents(new PlayerInteract(temp), this);
                    getLogger().info("Adding: " + world);
                }
            }
        } else {
            getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        }

        getLogger().info("Hotswap: Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Hotswap getInstance() {
        return instance;
    }
}
