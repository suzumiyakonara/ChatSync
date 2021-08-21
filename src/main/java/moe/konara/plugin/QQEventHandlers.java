package moe.konara.plugin;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

public class QQEventHandlers extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    @EventHandler
    public void onGroupMessage(@NotNull GroupMessageEvent event) throws Exception {
        if(event.getGroup().getId() == 849940001) {
            if(event.getMessage().contentToString().charAt(0) == '&') {
                String Command = event.getMessage().contentToString().substring(1).replaceAll(" ", "");
                if(Command.length() >= "list".length()) {
                    if(Command.substring(0, "list".length()).equalsIgnoreCase("list")) {
                        StringBuilder PlayerList = new StringBuilder("当前在线的玩家:\n");
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            PlayerList.append(player.getName()).append("\n");
                        }
                        if(PlayerList.toString().equals("当前在线的玩家:\n")) {
                            PlayerList = new StringBuilder("鬼服欢迎您 ");
                        }
                        event.getGroup().sendMessage(PlayerList.substring(0, PlayerList.length() - 1));
                    }
                }
                if(Command.length() >= "wladd".length()) {
                    if(Command.substring(0, "wladd".length()).equalsIgnoreCase("wladd")) {
                        if(event.getSender().getPermission().getLevel() >= 1) {
                            try {
                                Bukkit.getScheduler().runTaskLater(SpigotQQ.plugin, () -> Bukkit.dispatchCommand(SpigotQQ.console, "wladd " + Command.substring("wladd".length())), 0L);
                                event.getSubject().sendMessage("添加成功");
                            } catch(Exception e) {
                                event.getSubject().sendMessage(e.getMessage());
                            }
                        } else {
                            event.getGroup().sendMessage("权限不足");
                        }
                    }
                    if(Command.substring(0, "wldel".length()).equalsIgnoreCase("wldel")) {
                        if(event.getSender().getPermission().getLevel() >= 1) {
                            try {
                                Bukkit.getScheduler().runTaskLater(SpigotQQ.plugin, () -> Bukkit.dispatchCommand(SpigotQQ.console, "wldel " + Command.substring("wldel".length())), 0L);
                                event.getSubject().sendMessage("删除成功");
                            } catch(Exception e) {
                                event.getSubject().sendMessage(e.getMessage());
                            }
                        } else {
                            event.getGroup().sendMessage("权限不足");
                        }
                    }
                }
                if(Command.length() >= "help".length()) {
                    if(Command.substring(0, "help".length()).equalsIgnoreCase("help")) {
                        event.getGroup().sendMessage(
                                "&list - 获取玩家列表\n" +
                                        "&tps - 获取服务器tps\n" +
                                        "&adminhelp - 管理命令"
                        );
                    }
                }
                if(Command.length() >= "adminhelp".length()) {
                    if(Command.substring(0, "adminhelp".length()).equalsIgnoreCase("adminhelp")) {
                        event.getGroup().sendMessage(
                                "&wladd [游戏名] - 添加到白名单\n" +
                                        "&wldel [游戏名] - 删除白名单\n" +
                                        "&kick [游戏名] - 踢人"
                        );
                    }
                }
                if(Command.length() >= "tps".length()) {
                    if(Command.substring(0, "tps".length()).equalsIgnoreCase("tps")) {
                        NumberFormat temp = NumberFormat.getNumberInstance();
                        temp.setMaximumFractionDigits(2);
                        event.getGroup().sendMessage("tps:" + temp.format(SpigotQQ.tps));
                    }
                }
                if(Command.length() >= "kick".length()) {
                    if(Command.substring(0, "kick".length()).equalsIgnoreCase("kick")) {
                        if(event.getSender().getPermission().getLevel() >= 1) {
                            try {
                                Bukkit.getScheduler().runTaskLater(SpigotQQ.plugin, () -> Bukkit.dispatchCommand(SpigotQQ.console, "kick " + Command.substring("kick".length())), 0L);
                                event.getSubject().sendMessage("完事.");
                            } catch(Exception e) {
                                event.getSubject().sendMessage(e.getMessage());
                            }
                        } else {
                            event.getGroup().sendMessage("权限不足");
                        }
                    }
                }
            } else {
                Utils.update();
                String Prefix = Utils.getPrefix(event.getSender().getId());
                String PlayerName = Utils.getPlayerName(event.getSender().getId());
                if(PlayerName != null) {
                    PlayerName = " <" + PlayerName + "> ";
                } else {
                    PlayerName = "<" + event.getSenderName() + "> ";
                }
                if(Prefix == null) {
                    Prefix = "";
                }
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Prefix + PlayerName + event.getMessage().contentToString());
                }
            }
        }
    }
}
