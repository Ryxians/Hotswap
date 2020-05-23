package homebrew.hotswap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hotswap extends JavaPlugin {

    //Instance
    private static Hotswap instance;

    //config
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        //set instance
        instance = this;

        //load config
        config.options().copyDefaults(true);
        saveDefaultConfig();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Hotswap getInstance() {
        return instance;
    }
}
