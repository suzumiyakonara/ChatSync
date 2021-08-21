package moe.konara.plugin;

import org.bukkit.entity.Player;

public class AtMessage {
    private Player player;
    private Player targetPlayer;
    private String message;

    public AtMessage(Player player, Player targetPlayer, String message) {
        this.player = player;
        this.targetPlayer = targetPlayer;
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public String getMessage() {
        return message;
    }
}
