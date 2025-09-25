package elevate.unlimitedVaults;

import org.bukkit.plugin.java.JavaPlugin;

public class UnlimitedVaults extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        long delay = getConfig().getLong("reset-delay-ticks", 15L);
        getServer().getPluginManager().registerEvents(new VaultRefresher(this, delay), this);
        getLogger().info("UnlimitedVaults enabled (delay=" + delay + " ticks).");
    }
}
