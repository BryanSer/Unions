/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.Bungee;

import Br.Unions.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class Log {

    private String UnionName;
    private List<String> Logs = new ArrayList<>();

    public static Map<String, Log> UnionLogs = new HashMap<>();

    public Log(String uni) {
        this.UnionName = uni;
        this.load();
    }

    private File getFile() {
        return new File(Data.Plugin.getDataFolder(), this.UnionName + ".log");
    }

    public void log(String s) {
        Logs.add(String.format("[%s %s] %s", getDate(), getTime(), s));
    }

    public List<String> getLog(int page) {
        List<String> log = new ArrayList<>();
        int start = page * 10;
        int end = (page + 1) * 10;
        for (int i = start; i < Logs.size() && i < end; i++) {
            log.add(Logs.get(i));
        }
        return log;
    }

    public void save() {
        try {
            File f = this.getFile();
            f.delete();
            f.createNewFile();
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
            List<String> save = new ArrayList<>();
            for (int i = (Logs.size() - 100) < 0 ? 0 : (Logs.size() - 100); i < Logs.size(); i++) {
                String get = Logs.get(i);
                save.add(get);
            }
            config.set("Logs", save);;
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, f);
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        try {
            File f = this.getFile();
            if (!f.exists()) {
                return;
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
            for (String s : config.getStringList("Logs")) {
                Logs.add(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return df.format(date);
    }

    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }

};
