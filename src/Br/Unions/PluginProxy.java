/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import Br.API.Utils;
import Br.Unions.API.MessageManager;
import Br.Unions.SQL.UnionManager;
import Br.Unions.SQL.UserManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public interface PluginProxy {

    public enum Type {
        Bukkit, Bungee
    }

    public File getDataFolder();

    public InputStream getResource(String filename);

    public Type getType();

    public <T> T getPlugin();

    public UnionSetting getSetting(int level);

    public String getSQLUrl();

    public int getMaxUnionLevel();

    default PluginProxy init() {
        Data.Plugin = this;
        CereerType.loadConfig();
        
        try {
            Connection conn = DriverManager.getConnection(this.getSQLUrl());
            UnionManager.connect(conn);
            UserManager.connect(conn);
        } catch (SQLException ex) {
            Logger.getLogger(PluginProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public static PluginProxy bukkit(JavaPlugin p) {

        return new PluginProxy() {
            private Map<Integer, UnionSetting> Settings = new HashMap<>();
            private int MaxUnionLevel = 0;
            private String SQLUrl;

            {
                File configfile = new File(p.getDataFolder(), "config.yml");
                if (!configfile.exists()) {
                    try {
                        Utils.OutputFile(p, "config.yml", null);
                    } catch (IOException ex) {
                        Logger.getLogger(PluginProxy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configfile);
                ConfigurationSection us = config.getConfigurationSection("Setting.Union");
                for (String key : us.getKeys(false)) {
                    UnionSetting s = new UnionSetting(us.getConfigurationSection(key));
                    if (s.getLevel() > MaxUnionLevel) {
                        MaxUnionLevel = s.getLevel();
                    }
                    Settings.put(s.getLevel(), s);
                }
                SQLUrl = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true",
                        config.getString("SQL.IP"), config.getInt("SQL.Port"), config.getString("SQL.Database"), config.getString("SQL.User"), config.getString("SQL.Password"));
            }

            @Override
            public File getDataFolder() {
                return p.getDataFolder();
            }

            @Override
            public InputStream getResource(String s) {
                return p.getResource(s);
            }

            @Override
            public Type getType() {
                return Type.Bukkit;
            }

            @Override
            public <T> T getPlugin() {
                return (T) p;
            }

            @Override
            public UnionSetting getSetting(int level) {
                return Settings.get(level);
            }

            @Override
            public String getSQLUrl() {
                return this.SQLUrl;
            }

            @Override
            public int getMaxUnionLevel() {
                return this.MaxUnionLevel;
            }
        }.init();
    }

    public static PluginProxy bungee(Plugin p) {
        return new PluginProxy() {
            private Map<Integer, UnionSetting> Settings = new HashMap<>();
            private int MaxUnionLevel = 0;
            private String SQLUrl;

            {
                File configfile = new File(p.getDataFolder(), "config.yml");
                if (!configfile.exists()) {
                    try {
                        Tools.OutputFile(this, "config.yml", null);
                    } catch (IOException ex) {
                        Logger.getLogger(PluginProxy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    Configuration config = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(configfile);
                    Configuration su = config.getSection("Setting.Union");
                    for (String key : su.getKeys()) {
                        UnionSetting s = new UnionSetting(su.getSection(key), Integer.parseInt(key));
                        if (s.getLevel() > MaxUnionLevel) {
                            MaxUnionLevel = s.getLevel();
                        }
                        Settings.put(s.getLevel(), s);
                    }
                    SQLUrl = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true",
                            config.getString("SQL.IP"), config.getInt("SQL.Port"), config.getString("SQL.Database"), config.getString("SQL.User"), config.getString("SQL.Password"));
                } catch (IOException ex) {
                    Logger.getLogger(PluginProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public File getDataFolder() {
                return p.getDataFolder();
            }

            @Override
            public InputStream getResource(String filename) {
                return p.getResourceAsStream(filename);
            }

            @Override
            public Type getType() {
                return Type.Bungee;
            }

            @Override
            public <T> T getPlugin() {
                return (T) p;
            }

            @Override
            public UnionSetting getSetting(int level) {
                return this.Settings.get(level);
            }

            @Override
            public String getSQLUrl() {
                return this.SQLUrl;
            }

            @Override
            public int getMaxUnionLevel() {
                return this.MaxUnionLevel;
            }
        }.init();
    }

    public static boolean isFacing(Player p, LivingEntity e) {
        Vector v = e.getLocation().toVector().add(p.getLocation().toVector().multiply(-1));
        Vector f = p.getLocation().getDirection();
        v.setY(0);
        f.setY(0);
        return Math.abs(v.angle(f)) < Math.PI / 6f;
    }
}
