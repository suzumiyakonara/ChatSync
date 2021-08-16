package moe.konara.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.bukkit.Bukkit.getPlayer;

public class FunCommandHandler implements TabExecutor{
    private final String[] subCommands = {"lightning", "debug"};
    private final String[] trueorfalse = {"true", "false"};
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("konara")&&args.length>=1) {
            if (args[0].equalsIgnoreCase("lightning") && args.length >= 2) {
                Player player = getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("目标玩家不在线");
                    return true;
                }
                Location loc = player.getLocation();
                Objects.requireNonNull(loc.getWorld()).strikeLightning(loc);
                player.setHealth(player.getHealth()>=6?Math.ceil(player.getHealth()/3):5);
                sender.sendMessage("劈了");
                return true;
            }
            if (args[0].equalsIgnoreCase("debug") && args.length >= 3) {
                String playername=args[1];
                if(args[2].equalsIgnoreCase("true"))
                {
                    if(SpigotQQ.DebugPlayer.contains(playername)) {
                        sender.sendMessage("你已经开启过了");
                    }
                    else{
                        SpigotQQ.DebugPlayer.add(playername);
                        sender.sendMessage("开启成功");
                    }
                }
                else if(args[2].equalsIgnoreCase("false"))
                {
                    if(!SpigotQQ.DebugPlayer.contains(playername)) {
                        sender.sendMessage("(其实你本来就没开)");
                    }
                    else{
                        SpigotQQ.DebugPlayer.remove(playername);
                        sender.sendMessage("关闭成功");
                    }
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) return Arrays.asList(subCommands);

        if(args.length ==2&&args[0].equalsIgnoreCase("lightning")) {
            List<String> players = new ArrayList<>();
            for(Player p:Bukkit.getOnlinePlayers()){
                players.add(p.getName());
            }
            return players;
        }

        if(args[0].equalsIgnoreCase("debug")) {
            if(args.length ==2) {
                List<String> players = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    players.add(p.getName());
                }
                return players;
            }
            if(args.length ==3) return Arrays.asList(trueorfalse);
        }

        return new ArrayList<>();
    }
}
