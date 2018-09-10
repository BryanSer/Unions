/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */

package Br.Unions;

import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-6
 */
public class UnionSetting {
    private int MaxMembers;
    private int MaxBank;
    private int Level;
    private int UpPrice;
    
    public UnionSetting(Configuration config,int lv){//bungee
        this.Level = lv;
        this.MaxBank = config.getInt("MaxBank");
        this.MaxMembers = config.getInt("MaxMembers");
        this.UpPrice = config.getInt("UpPrice");
    }

    public UnionSetting(ConfigurationSection config){//bukkit
        this.Level = Integer.parseInt(config.getName());
        this.MaxBank = config.getInt("MaxBank");
        this.UpPrice = config.getInt("UpPrice");
        this.MaxMembers = config.getInt("MaxMembers");
    }
    
    public int getMaxMembers() {
        return MaxMembers;
    }

    public int getMaxBank() {
        return MaxBank;
    }

    public int getLevel() {
        return Level;
    }

    public int getUpPrice() {
        return UpPrice;
    }
    
    
}
