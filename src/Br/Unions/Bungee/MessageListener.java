/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.Bungee;

import Br.Unions.APIs.MessageManager;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class MessageListener implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent evt) {
        if (evt.isCancelled()) {
            return;
        }
        if (!evt.getTag().equals(MessageManager.MESSAGE_CHANNEL)) {
            return;
        }
        evt.setCancelled(true);
        String msg = new String(evt.getData());
        MessageManager.Message m = MessageManager.Message.decodeMessage(msg);
        m.getType().getFunction().accept(m);
    }
}
