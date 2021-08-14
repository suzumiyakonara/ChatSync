package moe.konara.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static moe.konara.plugin.SpigotQQ.bot;
import static moe.konara.plugin.SpigotQQ.statement;
import static moe.konara.plugin.Utils.setShouting;
import static moe.konara.plugin.Utils.translateColor;
import static org.bukkit.Bukkit.getLogger;

public class CommandHandler implements TabExecutor {
    private final String[] subCommands = {"add", "login","update","shout"};
    private final String[] trueorfalse = {"true", "false"};
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("QQbot")&&args.length>=1) {
            if (args[0].equalsIgnoreCase("login")) {
                getLogger().info("logining.........");
                bot.login();
                return true;
            }else if(args[0].equalsIgnoreCase("update")) {
                sender.sendMessage(Utils.update() ? "更新成功" : "更新出错");
                return true;
            }
            else if (args[0].equalsIgnoreCase("add")&&sender instanceof Player) {
                if (args.length >= 2) {
                    String prefix = "§f[ §7玩家 §f]";
                    try {
                        ResultSet rs = statement.executeQuery("SELECT * FROM \"title_player\" where player_name = '" + sender.getName().toLowerCase() + "';");
                        while (rs.next()) {
                            if(rs.getInt("is_use")==1)
                                prefix = rs.getString("title_name");
                        }
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                    prefix=translateColor(prefix);
                    sender.sendMessage(Utils.addCorrespondingData(sender.getName(), Long.parseLong(args[1]), prefix, false) ? "添加成功" : "添加出错");
                    return true;
                }
                else return false;
            }
            else if (args[0].equalsIgnoreCase("shout")&&sender instanceof Player)
                if (args.length >= 2){
                    if(args[1].equalsIgnoreCase("true")) {
                        sender.sendMessage(setShouting(true, sender.getName())?"更改成功":"更改失败");
                        return true;
                    }
                    else if(args[1].equalsIgnoreCase("false")){
                        sender.sendMessage(setShouting(false, sender.getName())?"更改成功":"更改失败");
                        return true;
                    }
                    return false;
                }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) return Arrays.asList(subCommands);

        if(args.length ==2&&args[0].equalsIgnoreCase("add")) return Collections.singletonList("[<QQnum>]");

        if(args.length ==2&&args[0].equalsIgnoreCase("shout")) return Arrays.asList(trueorfalse);

        return new ArrayList<>();
    }
}