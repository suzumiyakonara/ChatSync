package com.konara.plugin;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class QQEventHandlers extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        System.out.println(exception.getMessage());
    }
    @EventHandler
    public void onGroupMessage(@NotNull GroupMessageEvent event) throws Exception {
        if(event.getGroup().getId() == 849940001)
        {
            String Prefix = "";
            String PlayerName;
            if(Lollipop.getName(event.getSender().getId()) != null) {
                Prefix = "§f[ " + ChatColor.of(new Color(0xFF0000)) + "穿" + ChatColor.of(new Color(0xFF0033)) + "越" + ChatColor.of(new Color(0xFF0066)) + "星" + ChatColor.of(new Color(0xFF0099)) + "空" + ChatColor.of(new Color(0xFF00B4)) + "的" + ChatColor.of(new Color(0xFF00CC)) + "祈" + ChatColor.of(new Color(0xFF00FF)) + "愿 §f] ";
                PlayerName = "<" + Lollipop.getName(event.getSender().getId()) + "> ";
            }else
                if(OtherGroup.getName(event.getSender().getId()) != null)
                {
                    Prefix = "§f[ §7玩家 §f]";
                    PlayerName = "<" + OtherGroup.getName(event.getSender().getId()) + "> ";
                }
                else
                    PlayerName = "<" + event.getSenderName() + "> ";
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Prefix + PlayerName + event.getMessage().contentToString());
            }
        }
    }
}
