/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import Br.Unions.APIs.MessageManager;
import java.io.File;
import java.io.InputStream;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
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

    default PluginProxy init() {
        Data.Plugin = this;
        CereerType.loadConfig();
        return this;
    }

    public static PluginProxy bukkit(JavaPlugin p) {
        return new PluginProxy() {
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
        }.init();
    }

    public static PluginProxy bungee(Plugin p) {
        return new PluginProxy() {
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
