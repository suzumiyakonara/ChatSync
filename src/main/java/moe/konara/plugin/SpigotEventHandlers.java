package moe.konara.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static moe.konara.plugin.Utils.isShouting;

public class SpigotEventHandlers implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(isShouting(e.getPlayer().getDisplayName()))
            Objects.requireNonNull(SpigotQQ.bot.getGroup(849940001L)).sendMessage("<"+e.getPlayer().getName()+"> "+e.getMessage());
        else if(e.getMessage().charAt(0) == '!'||e.getMessage().charAt(0) == '！')
            Objects.requireNonNull(SpigotQQ.bot.getGroup(849940001L)).sendMessage("<"+e.getPlayer().getDisplayName()+"> "+e.getMessage().substring(1));
    }
}
