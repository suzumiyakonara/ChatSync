package moe.konara.plugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static moe.konara.plugin.SpigotQQ.*;

public class Utils {
    public static boolean update() {
        YamlConfiguration yc = getYamlConfiguration();
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("CorrespondingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        for(String path : Players) {
            long QQ = Objects.requireNonNull(section.getConfigurationSection(path)).getLong("QQ");
            String Prefix = "§f[ §7玩家 §f]";
            try {
                ResultSet rs = statement.executeQuery("SELECT * FROM \"title_player\" where player_name = '" + path.toLowerCase() + "';");
                while(rs.next()) {
                    if(rs.getInt("is_use") == 1) {
                        Prefix = rs.getString("title_name");
                    }
                }
            } catch(SQLException e) {
                e.printStackTrace();
                return false;
            }
            Prefix = translateColor(Prefix);
            if(!addCorrespondingData(path, QQ, Prefix, true)) {
                System.out.println(path + "更新失败");
            }
        }
        return true;
    }

    public static boolean addCorrespondingData(String sender, long QQ, String prefix, boolean globalupdate) {
        YamlConfiguration yc = getYamlConfiguration();
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("CorrespondingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        if(!globalupdate) {
            for(String path : Players) {
                if(Objects.requireNonNull(section.getConfigurationSection(path)).getLong("QQ") == QQ) {
                    return false;
                }
            }
        }
        Map<String, Object> Player = new HashMap<>();
        Player.put("QQ", QQ);
        Player.put("PlayerName", sender);
        Player.put("Prefix", prefix);
        Objects.requireNonNull(section).createSection(sender, Player);
        try {
            yc.save(ConfigPath + "CorrespondingData.yml");
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static YamlConfiguration getYamlConfiguration() {
        try {
            being.close();
            statement.close();
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        if(new File(ConfigPath + "/PlayerTitle.db").exists()) {
            if(!new File(ConfigPath + "/PlayerTitle.db").delete()) {
                System.out.println("清除缓存失败");
            }
        }
        try {
            Files.copy(new File("./plugins/PlayerTitle" + "/PlayerTitle.db").toPath(), new File(ConfigPath + "/PlayerTitle.db").toPath());
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + ConfigPath + "/PlayerTitle.db");
            statement = connection.createStatement();
            sqlTab = "PRAGMA table_info(title_player)";
            being = statement.executeQuery(sqlTab);
        } catch(Exception throwables) {
            throwables.printStackTrace();
        }
        File CorrespondingFile = new File(ConfigPath + "CorrespondingData.yml");
        if(!CorrespondingFile.exists()) {
            try {
                System.out.println(CorrespondingFile.createNewFile() ? "配置文件创建成功" : "配置文件创建失败");
            } catch(IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        YamlConfiguration yc = new YamlConfiguration();
        try {
            yc.load(CorrespondingFile);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        if(yc.getConfigurationSection("CorrespondingData") == null) {
            yc.createSection("CorrespondingData");
        }
        return yc;
    }

    public static String getPlayerName(long QQ) {
        YamlConfiguration yc = getYamlConfiguration();
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("CorrespondingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        for(String path : Players) {
            if(Objects.requireNonNull(section.getConfigurationSection(path)).getLong("QQ") == QQ) {
                return path;
            }
        }
        return null;
    }

    public static long getQQNum(String playerName) {
        YamlConfiguration yc = getYamlConfiguration();
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("CorrespondingData");
        long qqNum = Objects.requireNonNull(section).getLong(playerName + ".QQ");
        if(Long.toString(qqNum).matches("[1-9][0-9]{4,}")) {
            return qqNum;
        }
        return -1L;
    }

    public static String getPrefix(long QQ) {
        YamlConfiguration yc = getYamlConfiguration();
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("CorrespondingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        for(String path : Players) {
            if(Objects.requireNonNull(section.getConfigurationSection(path)).getLong("QQ") == QQ) {
                return Objects.requireNonNull(section.getConfigurationSection(path)).getString("Prefix");
            }
        }
        return null;
    }

    public static boolean isShouting(String target) {
        File CorrespondingFile = new File(ConfigPath + "ShoutingData.yml");
        if(!CorrespondingFile.exists()) {
            try {
                System.out.println(CorrespondingFile.createNewFile() ? "配置文件创建成功" : "配置文件创建失败");
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        YamlConfiguration yc = new YamlConfiguration();
        try {
            yc.load(CorrespondingFile);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        if(yc.getConfigurationSection("ShoutingData") == null) {
            yc.createSection("ShoutingData");
        }
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("ShoutingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        for(String name : Players) {
            if(name.equalsIgnoreCase(target)) {
                if(Objects.equals(Objects.requireNonNull(section.getConfigurationSection(name)).getString("Shout"), "true")) {
                    return true;
                } else if(Objects.equals(Objects.requireNonNull(section.getConfigurationSection(name)).getString("Shout"), "false")) {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean setShouting(boolean shouting, String sender) {
        File CorrespondingFile = new File(ConfigPath + "ShoutingData.yml");
        if(!CorrespondingFile.exists()) {
            try {
                System.out.println(CorrespondingFile.createNewFile() ? "配置文件创建成功" : "配置文件创建失败");
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        YamlConfiguration yc = new YamlConfiguration();
        try {
            yc.load(CorrespondingFile);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        if(yc.getConfigurationSection("ShoutingData") == null) {
            yc.createSection("ShoutingData");
        }
        ConfigurationSection section = Objects.requireNonNull(yc).getConfigurationSection("ShoutingData");
        Set<String> Players = Objects.requireNonNull(section).getKeys(false);
        Map<String, Object> Player = new HashMap<>();
        Player.put("Shout", shouting ? "true" : "false");
        Objects.requireNonNull(section).createSection(sender, Player);
        try {
            yc.save(ConfigPath + "ShoutingData.yml");
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, Integer> readColorConfig() {
        YamlConfiguration yc = new YamlConfiguration();
        Map<String, Integer> tmp = new HashMap<>();
        try {
            yc.load(new File("./plugins/PlayerTitle/color.yml"));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        Set<String> ColorNickName = yc.getKeys(false);
        for(String Name : ColorNickName) {
            tmp.put(Name, HexStringtoInt(yc.getString(Name)));
        }
        return tmp;
    }

    public static String translateColor(String prefix) {
        Pattern r = Pattern.compile("%[a-zA-Z0-9]+%");
        Matcher m = r.matcher(prefix);
        while(m.find()) {
            prefix = prefix.replaceAll(m.group(), ChatColor.of(new Color(SpigotQQ.CorrespondingColor.get(m.group().substring(1, m.group().length() - 1)))).toString());
        }
        return prefix;
    }

    public static int HexStringtoInt(String HexString) {
        int num = 0;
        HexString = HexString.toUpperCase();
        for(int i = HexString.length() - 1; i >= 0; i--) {
            double pow = Math.pow(16, HexString.length() - 1 - i);
            switch(HexString.charAt(i)) {
                case '1':
                    num = (int) (num + (1 * pow));
                    break;
                case '2':
                    num = (int) (num + (2 * pow));
                    break;
                case '3':
                    num = (int) (num + (3 * pow));
                    break;
                case '4':
                    num = (int) (num + (4 * pow));
                    break;
                case '5':
                    num = (int) (num + (5 * pow));
                    break;
                case '6':
                    num = (int) (num + (6 * pow));
                    break;
                case '7':
                    num = (int) (num + (7 * pow));
                    break;
                case '8':
                    num = (int) (num + (8 * pow));
                    break;
                case '9':
                    num = (int) (num + (9 * pow));
                    break;
                case 'A':
                    num = (int) (num + (10 * pow));
                    break;
                case 'B':
                    num = (int) (num + (11 * pow));
                    break;
                case 'C':
                    num = (int) (num + (12 * pow));
                    break;
                case 'D':
                    num = (int) (num + (13 * pow));
                    break;
                case 'E':
                    num = (int) (num + (14 * pow));
                    break;
                case 'F':
                    num = (int) (num + (15 * pow));
                    break;
                default:
                    break;
            }
        }
        return num;
    }

    /**
     * Analyze whether the message is a @ message and return a AtMessage.
     *
     * @param message The player chat message
     * @return If it is not a @ message, it returns a null value, otherwise it returns a AtMessage.
     */
    @Nullable
    public static AtMessage analyzeMessage(@NotNull Player player, @NotNull String message) {
        if(!message.matches("[\\w ]+@.*")) {
            return null;
        }
        String[] args = message.split("@", 1);
        return new AtMessage(player, SpigotQQ.server.getPlayer(args[0]), args[1]);
    }
}
