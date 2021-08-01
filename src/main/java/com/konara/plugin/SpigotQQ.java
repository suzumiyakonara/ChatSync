package com.konara.plugin;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.Map;
import java.util.Objects;

import static com.konara.plugin.Utils.readColorConfig;

public final class SpigotQQ extends JavaPlugin {
    private static final long QQNUM = 000000L;
    private static final String QQPWD = "";
    public static String ConfigPath="./plugins/Spigot-QQ/";
    static Statement statement=null;
    static Connection connection = null;
    static String sqlTab ;
    static ResultSet being ;
    static Bot bot;
    static ConsoleCommandSender console;
    Thread thread=new Thread(() -> {
        EventChannel<BotEvent> channel = bot.getEventChannel();
        bot.login();
        channel.registerListenerHost(new QQEventHandlers());
    });
    static Map<String, Integer> CorrespondingColor;
    @Override
    public void onEnable() {
        // Plugin startup logic
        console = Bukkit.getServer().getConsoleSender();
        if(!new File(ConfigPath).exists())
            System.out.println(new File(ConfigPath).mkdirs()?"创建成功":"创建失败");
        bot = BotFactory.INSTANCE.newBot(QQNUM, QQPWD, new BotConfiguration() {{
            fileBasedDeviceInfo(ConfigPath+"/device.json");
        }});
        CorrespondingColor = readColorConfig();
        thread.start();
        Objects.requireNonNull(Bukkit.getPluginCommand("QQbot")).setExecutor(new CommandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("QQbot")).setTabCompleter(new CommandHandler());
        if(new File(ConfigPath+"/PlayerTitle.db").exists())
            System.out.println(new File(ConfigPath+"/PlayerTitle.db").delete()?"清除缓存成功":"清除缓存失败");
        try {
            Files.copy(new File("./plugins/PlayerTitle" + "/PlayerTitle.db").toPath(),new File(ConfigPath+"/PlayerTitle.db").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + ConfigPath+"/PlayerTitle.db");
            statement = connection.createStatement();
            sqlTab ="PRAGMA table_info(title_player)";
            being = statement.executeQuery(sqlTab);
            System.out.println("Opened database successfully");
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new SpigotEventHandlers(),this);
    }

    @Override
    public void onDisable() {
        try {
            statement.close();
            connection.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(new File(ConfigPath+"/PlayerTitle.db").exists())
            System.out.println(new File(ConfigPath+"/PlayerTitle.db").delete()?"清除缓存成功":"清除缓存失败");
    }
}
