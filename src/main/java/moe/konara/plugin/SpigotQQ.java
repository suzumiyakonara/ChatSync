package moe.konara.plugin;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SpigotQQ extends JavaPlugin {
    private static final long QQNUM = 0000000000L;
    private static final String QQPWD = "password";
    public static String ConfigPath="./plugins/Spigot-QQ/";
    static Statement statement=null;
    static Connection connection = null;
    static String sqlTab ;
    static ResultSet being ;
    static Bot bot;
    static ConsoleCommandSender console;
    static Plugin plugin;
    static double tps;
    static ScoreboardManager scoreboardManager;
    static Scoreboard board;
    static Objective dig;
    static Objective realDig;
    static List<String> DebugPlayer= new ArrayList<>();
    Thread thread=new Thread(() -> {
        EventChannel<BotEvent> channel = bot.getEventChannel();
        bot.login();
        channel.registerListenerHost(new QQEventHandlers());
    });
    static Map<String, Integer> CorrespondingColor;
    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin=this;
        console = getServer().getConsoleSender();

        //获得积分板
        scoreboardManager = Bukkit.getServer().getScoreboardManager();
        board = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        dig = board.getObjective("dig");
        if(dig==null)
            dig = board.registerNewObjective("dig", "dummy", "挖掘榜");
        realDig = board.getObjective("realDig");
        if(realDig==null)
            realDig = board.registerNewObjective("realDig", "dummy", "挖掘量");
        dig.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        realDig.setDisplaySlot(DisplaySlot.BELOW_NAME);

        //创建插件目录
        if(!new File(ConfigPath).exists())
            System.out.println(new File(ConfigPath).mkdirs()?"创建成功":"创建失败");
        bot = BotFactory.INSTANCE.newBot(QQNUM, QQPWD, new BotConfiguration() {{
            fileBasedDeviceInfo(ConfigPath+"/device.json");
        }});

        //读取色彩占位符
        CorrespondingColor = Utils.readColorConfig();

        //开启Bot
        thread.start();

        //注册指令执行器及补充器
        Objects.requireNonNull(Bukkit.getPluginCommand("QQbot")).setExecutor(new CommandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("QQbot")).setTabCompleter(new CommandHandler());

        Objects.requireNonNull(Bukkit.getPluginCommand("konara")).setExecutor(new FunCommandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("konara")).setTabCompleter(new FunCommandHandler());

        //更新plt数据库
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
        Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable(){

            long secstart;
            long secend;

            int ticks;

            @Override
            public void run(){
                secstart = (System.currentTimeMillis() / 1000);

                if(secstart == secend){
                    ticks++;
                }else{
                    secend = secstart;
                    tps = (tps == 0) ? ticks : ((tps + ticks) / 2);
                    ticks = 1;
                }
            }

        }, 0, 1);
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
