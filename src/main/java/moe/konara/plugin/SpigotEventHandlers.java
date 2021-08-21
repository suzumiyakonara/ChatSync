package moe.konara.plugin;

import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scoreboard.Score;

import java.text.NumberFormat;
import java.util.Objects;

import static moe.konara.plugin.Utils.isShouting;

public class SpigotEventHandlers implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        AtMessage atMessage = Utils.analyzeMessage(e.getPlayer(), message);
        if(atMessage != null) {
            Objects.requireNonNull(SpigotQQ.bot.getGroup(849940001L)).sendMessage(
                    new PlainText("<" + atMessage.getPlayer().getName() + "> ").plus(new At(Utils.getQQNum(atMessage.getTargetPlayer().getName()))).plus(new PlainText(" " + atMessage.getMessage())));
        } else if(isShouting(e.getPlayer().getName())) {
            Objects.requireNonNull(SpigotQQ.bot.getGroup(849940001L)).sendMessage("<" + e.getPlayer().getName() + "> " + message);
        } else if(message.charAt(0) == '!' || message.charAt(0) == '！') {
            Objects.requireNonNull(SpigotQQ.bot.getGroup(849940001L)).sendMessage("<" + e.getPlayer().getName() + "> " + message.substring(1));
        }
    }

    @EventHandler
    public void onDig(BlockBreakEvent event) {
        if(!event.getBlock().isEmpty()) {
            Score realDigScore = SpigotQQ.realDig.getScore(event.getPlayer().getName());
            Score digScore = SpigotQQ.dig.getScore(event.getPlayer().getName());
            realDigScore.setScore(realDigScore.getScore() + 1);
            digScore.setScore((int) Math.floor(realDigScore.getScore() / 1000F));
            if(realDigScore.getScore() == 1) {
                System.out.println(event.getPlayer().getName() + ":" + realDigScore.getScore() + "开启了挖掘计分板");
            }
            if(SpigotQQ.DebugPlayer.contains(event.getPlayer().getName()) && (realDigScore.getScore() % 50) == 0) {
                event.getPlayer().sendMessage("你已经挖掘了" + realDigScore.getScore() + "个方块");
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(SpigotQQ.DebugPlayer.contains(event.getPlayer().getName())) {
            if(event.getRightClicked() instanceof Player) {
                NumberFormat temp = NumberFormat.getNumberInstance();
                temp.setMaximumFractionDigits(2);
                if(event.getPlayer().isSneaking()) {
                    event.getPlayer().openInventory(((Player) event.getRightClicked()).getEnderChest());
                    event.getPlayer().sendMessage("你打开了" + event.getRightClicked().getName() + "的末影箱");
                } else {
                    event.getPlayer().openInventory(((Player) event.getRightClicked()).getInventory());
                    event.getPlayer().sendMessage("你打开了" + event.getRightClicked().getName() + "的背包");
                }
                event.getPlayer().sendMessage("该玩家还剩下" + temp.format(((Player) event.getRightClicked()).getHealth()) + "的血量");
            }
            if(event.getRightClicked() instanceof Creeper) {
                ((Creeper) event.getRightClicked()).setExplosionRadius(0);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(SpigotQQ.DebugPlayer.contains(event.getEntity().getName())) {
                event.setDamage(0.01);
            }
        }
    }
}
