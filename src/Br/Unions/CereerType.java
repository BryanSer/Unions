/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import Br.Unions.Data;
import Br.Unions.Tools;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public enum CereerType {
    Owner,
    Admin,
    Elder,
    Elite,
    Member,
    Newcomer;
    private String Prefix;
    private String Name;

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    
    

    public static void loadConfig() {
        File f = new File(Data.Plugin.getDataFolder(), "cereerr.yml");
        if (!f.exists()) {
            try {
                Tools.OutputFile(Data.Plugin, "cereer.yml", null);
            } catch (IOException ex) {
                Logger.getLogger(CereerType.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        switch (Data.Plugin.getType()) {
            case Bukkit:
                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                for (CereerType ct : CereerType.values()) {
                    ct.Prefix = ChatColor.translateAlternateColorCodes('&', config.getString(ct.name() + ".Prefix"));
                    ct.Name = config.getString(ct.name() + ".Name");
                }
                break;
            case Bungee:
                try {
                    Configuration cfg = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(f);
                    for (CereerType ct : CereerType.values()) {
                        ct.Prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString(ct.name() + ".Prefix"));
                        ct.Name = cfg.getString(ct.name() + ".Name");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(CereerType.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }
}
