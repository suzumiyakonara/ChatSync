package com.konara.plugin.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
public class Group {
    String DefaultPrefix;
    Map<Long,Player> players= new HashMap<>();
    public Group(String DefaultPrefix){
        this.DefaultPrefix=DefaultPrefix;
    }
    public void addPlayer(Long QQ,String PlayerName,String Prefix){
        Player player=new Player(PlayerName,Prefix);
        players.put(QQ,player);
    }

    public void addPlayer(Long QQ,String PlayerName){
        Player player=new Player(PlayerName);
        players.put(QQ,player);
    }

    public Map<Long,Player> getPlayers(){
        return players;
    }

    public String getDefaultPrefix() {
        return DefaultPrefix;
    }
}
