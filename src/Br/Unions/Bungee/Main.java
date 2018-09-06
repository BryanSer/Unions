/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.Bungee;

import Br.Unions.API.MessageManager;
import Br.Unions.PluginProxy;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class Main extends Plugin {

    @Override
    public void onEnable() {
        BungeeCord.getInstance().registerChannel(MessageManager.MESSAGE_CHANNEL);
        PluginProxy.bungee(this);
    }
}
