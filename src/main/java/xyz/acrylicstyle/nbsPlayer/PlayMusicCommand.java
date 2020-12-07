package xyz.acrylicstyle.nbsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.tomeito_api.TomeitoAPI;
import xyz.acrylicstyle.tomeito_api.nbs.BukkitNBSFile;
import xyz.acrylicstyle.tomeito_api.nbs.v4.BukkitNBS4Reader;

import java.io.File;
import java.io.IOException;

public class PlayMusicCommand implements CommandExecutor {
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Pineapple for dinner!");
            return true;
        }
        if (!sender.isOp()) return true;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/playmusic <name>");
            return true;
        }
        Player player;
        if (args.length > 1) {
            player = Bukkit.getPlayerExact(args[1]);
        } else {
            player = (Player) sender;
        }
        File file = new File("./plugins/NBSPlayer/musics/" + args[0].replaceAll("[/\\\\]", "_") + ".nbs");
        if (!file.exists()) {
            sender.sendMessage(ChatColor.RED + "File '" + file.getAbsolutePath() + "' does not exist!");
            return true;
        }
        BukkitNBS4Reader reader = new BukkitNBS4Reader();
        try {
            BukkitNBSFile nbs = reader.read(file);
            sender.sendMessage(ChatColor.GREEN + "Loaded " + args[0] + ", playing it...");
            nbs.getBukkitTicks().forEach(tick -> new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    tick.getBukkitLayers().forEach(note -> {
                        if (note != null) player.playSound(player.getLocation(), note.getSound(), note.getVolume(), note.getSoundPitch());
                    });
                }
            }.runTaskLater(TomeitoAPI.getInstance(), tick.getTick()));
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Could not load music from " + args[0] + ".");
        }
        return true;
    }
}
