package com.konara.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotEventHandlers implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getMessage().charAt(0) == '!')
            SpigotQQ.bot.getGroup(849940001L).sendMessage("<"+e.getPlayer().getDisplayName()+"> "+e.getMessage().substring(1));
    }
}
