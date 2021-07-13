package com.konara.plugin.element;

public class Player {
    String PlayerName;
    String Prefix;
    Player(String PlayerName,String Prefix){
        this.PlayerName=PlayerName;
        this.Prefix=Prefix;
    }
    Player(String PlayerName){
        this.PlayerName=PlayerName;
        this.Prefix=null;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getPrefix() {
        return Prefix;
    }
}
