package com.konara.plugin;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QQEventHandlers extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        System.out.println(exception.getMessage());
    }
    @EventHandler
    public void onGroupMessage(@NotNull GroupMessageEvent event) throws Exception {
        if(event.getGroup().getId() == 849940001)
        {
            String Prefix = Utils.getPrefix(event.getSender().getId());
            String PlayerName=Utils.getPlayerName(event.getSender().getId());
            if(PlayerName!=null)
                PlayerName="<"+PlayerName+"> ";
            else
                PlayerName="<"+event.getSenderName()+"> ";
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Prefix + PlayerName + event.getMessage().contentToString());
            }
        }
    }
}
