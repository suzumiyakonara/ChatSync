package com.konara.plugin;

import com.konara.plugin.element.Group;
import com.konara.plugin.element.Player;

public class Utils {
    public String getPlayerName(Long QQ){
        for(Group group : SpigotQQ.realName.groups.values()) {
            Player player=group.getPlayers().get(QQ);
            if(player!=null)
            {
                return player.getPlayerName();
            }
        }
        return null;
    }

    public String getPlayerPrefix(Long QQ){
        for(Group group : SpigotQQ.realName.groups.values()) {
            Player player=group.getPlayers().get(QQ);
            if(player!=null)
            {
                String prefix=player.getPrefix();
                if(prefix!=null)
                    return prefix;
                else
                    return group.getDefaultPrefix();
            }
        }
        return "";
    }
}
