package com.konara.plugin;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotQQ extends JavaPlugin {
    private static final long QQNUM = 1295874819L;
    private static final String QQPWD = "CaiYueXin456";
    public static RealName realName = new RealName();
    static Bot bot = BotFactory.INSTANCE.newBot(QQNUM, QQPWD, new BotConfiguration() {{
        fileBasedDeviceInfo();
    }});
    Thread thread=new Thread(new Runnable(){
        public void run(){
            EventChannel<BotEvent> channel = bot.getEventChannel();
            bot.login();
            channel.registerListenerHost(new QQEventHandlers());
        }});
    @Override
    public void onEnable() {
        // Plugin startup logic
        thread.start();
        getServer().getPluginManager().registerEvents(new SpigotEventHandlers(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("restartbot")) {
            getLogger().info("Restarting.........");
            thread.stop();
            bot.login();
            return true;
        } //如果以上内容成功执行，则返回true
        // 如果执行失败，则返回false.
        return false;
    }
}
