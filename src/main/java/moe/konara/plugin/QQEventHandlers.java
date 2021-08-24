package moe.konara.plugin;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
                if("".equals(event.getMessage().contentToString()) || event.getMessage().contentToString().matches("[ \n]+")) {
                    return;
                }
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
                MessageChain messageChain = event.getMessage();
                StringBuilder message = new StringBuilder();
                int i = 0;
                for(SingleMessage element : messageChain) {
                    i++;
                    //消息元素判断
                    if(element instanceof At) {
                        String target = Utils.getPlayerName(((At) element).getTarget());
                        if(target == null) {
                            message.append(((At) element).getDisplay(event.getGroup()));
                        } else {
                            message.append("@").append(target);
                            Player targetPlayer = SpigotQQ.server.getPlayer(target);
                            if(targetPlayer == null) {
                                return;
                            } else {
                                targetPlayer.sendMessage("§l§3[MC Mail]§r \u0051\u0051\u7fa4\u91cc " + Prefix + PlayerName + " \u7684\u6d88\u606f\u4e2d\u63d0\u53ca\u4e86\u4f60\u3002");
                                targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 5.0F, 1.0F);
                                targetPlayer.sendTitle(null, PlayerName + " \u63d0\u5230\u4e86\u4f60", 10, 40, 20);
                            }
                        }
                        if(messageChain.size() - i > 0) {
                            message.append(" ");
                        }

                    } else if(element instanceof AtAll) {
                        message.append(element.contentToString()); //@全体成员
                        if(messageChain.size() - i > 0) {
                            message.append(" ");
                        }
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage("§l§3[MC Mail]§r \u0051\u0051\u7fa4\u91cc " + Prefix + PlayerName + " \u7684\u6d88\u606f\u4e2d\u63d0\u53ca\u4e86\u6240\u6709\u4eba\u3002");
                            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 5.0F, 1.0F);
                            player.sendTitle(null, PlayerName + " \u63d0\u5230\u4e86\u4f60", 10, 40, 20);
                        }
                    } else if(element instanceof Face) {
                        switch(((Face)element).getId()) {
                            case Face.SHAN_DIAN:
                                message.append("\u26a1");    //⚡
                                break;
                            case Face.JU_HUA:
                                message.append("\u2744");    //❄
                                break;
                            case Face.AI_XIN:
                                message.append("\u2764");    //❤
                                break;
                            case Face.TAI_YANG:
                                message.append("\u2600");    //☀
                                break;
                            case Face.WEI_XIAO:
                                message.append("\u263a");    //☺
                                break;
                            case Face.XIE_YAN_XIAO:
                                message.append("\u263b");    //☻
                                break;
                            case Face.SHOU_QIANG:
                                message.append("\uD83C\uDFF9"); //🏹
                        }
                    } else {
                        message.append(element.contentToString());
                    }
                }
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Prefix + PlayerName + message);
                }
            }
        }
    }
}
