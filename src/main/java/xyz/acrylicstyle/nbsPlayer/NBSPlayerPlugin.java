package xyz.acrylicstyle.nbsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NBSPlayerPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("playmusic").setExecutor(new PlayMusicCommand(getConfig().getBoolean("everyoneIsDJ", false)));
    }
}
