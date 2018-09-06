/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */

package Br.Unions.API;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class BungeeUnionCreateEvent extends Event implements Cancellable{
    
    private boolean cancel = false;
    private ProxiedPlayer player;

    public BungeeUnionCreateEvent(ProxiedPlayer player) {
        this.player = player;
    }
    
    

    public ProxiedPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean bln) {
        cancel = bln;
    }
    
}
