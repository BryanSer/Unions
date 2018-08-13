/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.APIs;

import Br.Unions.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class BungeeButtonMessager implements Listener {

    private static boolean Register = false;
    public static Map<String, BungeeButtonMessager.ButtonInfo> ButtonInfos = new HashMap<>();
    private static final int CHARAMOUNT = 26 + 26 + 10;
    private static final int LENGTH = 6;
    private static final char[] CHAR = new char[CHARAMOUNT];
    private static final Random Random = new Random();
    private static final String REGex = "\\/?Unions Button .*";

    static {
        int index = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            CHAR[index++] = c;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            CHAR[index++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            CHAR[index++] = c;
        }
    }

    private static String getRandomString() {
        char[] c = new char[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            c[i] = CHAR[Random.nextInt(CHARAMOUNT)];
        }
        return new String(c);
    }

    /**
     * 向玩家发送一堆按钮 按钮的的内容将由msg决定,最后通过BiConsumer来返回执行玩家按下的按钮<p>
     * 按钮计数从0开始 超时overtime秒则传入null
     *
     * @param p
     * @param msg
     * @param callback 注意 Integer参数为null时表示超时 玩家如果中途退出游戏也会触发null
     * @param overtime 超时时间 单位秒
     * @return true时表示 请求成功 false时表示 上个处理还未完成
     */
    public static boolean SendButtonRequest(ProxiedPlayer p, String[] msg, BiConsumer<ProxiedPlayer, Integer> callback, int overtime) {
        RegisterListener();
        if (ButtonInfos.containsKey(p.getName())) {
            return false;
        }
        BungeeButtonMessager.ButtonInfo bi = new BungeeButtonMessager.ButtonInfo(p, msg, callback, overtime);
        ButtonInfos.put(p.getName(), bi);
        List<BaseComponent> bs = new ArrayList<>();
        for (int i = 0; i < msg.length; i++) {
            String key = msg[i];
            bs.addAll(Arrays.asList(getButton(String.format("§r[%s§r]", key), bi.getKeys().get(i))));
            bs.add(new TextComponent("    "));
        }
        BaseComponent[] comps = new BaseComponent[bs.size()];
        for (int i = 0; i < comps.length; i++) {
            comps[i] = bs.get(i);
        }
        p.sendMessage(comps);
        return true;
    }

    public static BaseComponent[] getButton(String text, String cmd) {
        return new ComponentBuilder(text).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Unions Button " + cmd)).create();
    }

    private static void RegisterListener() {
        if (!Register) {
            //Bukkit.getPluginManager().registerEvents(new CallBack(), Data.Plugin.);
            BungeeCord.getInstance().getPluginManager().registerListener(Data.Plugin.getPlugin(), new BungeeButtonMessager());
        }
        Register = true;
    }

    public static class ButtonInfo {

        private String Name;
        private List<String> Keys = new ArrayList<>();
        private BiConsumer<ProxiedPlayer, Integer> Callback;
        private boolean canceled = false;
        private ProxiedPlayer Player;
        private boolean overtime = true;
        private ScheduledTask Task;

        public ButtonInfo(ProxiedPlayer p, String[] display, BiConsumer<ProxiedPlayer, Integer> callback, int overtime) {
            this.Name = p.getName();
            Player = p;
            for (String s : display) {
                Keys.add(BungeeButtonMessager.getRandomString());
            }
            this.Callback = callback;
            Task = BungeeCord.getInstance().getScheduler().schedule(Data.Plugin.getPlugin(), this::run, overtime, TimeUnit.SECONDS);
            //super.runTaskLater(PluginData.plugin, overtime * 20L);
        }

        public synchronized void cancel() throws IllegalStateException {
            overtime = false;
            if (!canceled) {
                this.run();
                this.canceled = true;
            }
            Task.cancel();
        }

        public void run() {
            if (canceled) {
                return;
            }
            if (overtime) {
                Callback.accept(Player, null);
            }
            ButtonInfos.remove(this.Name);
        }

        public String getName() {
            return Name;
        }

        public List<String> getKeys() {
            return Keys;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public BiConsumer<ProxiedPlayer, Integer> getCallback() {
            return Callback;
        }

    }

    @EventHandler
    public void onChat(ChatEvent evt) {
        if (!evt.isCommand()) {
            return;
        }
        if (!(evt.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        String msg = evt.getMessage();
        ProxiedPlayer p = (ProxiedPlayer) evt.getSender();
        if (msg.matches(REGex)) {
            evt.setCancelled(true);
            String key = evt.getMessage().split(" ")[2];
            ButtonInfo bi = ButtonInfos.get(p.getName());
            if (bi == null) {
                return;
            }
            int index = bi.getKeys().indexOf(key);
            if (index == -1) {
                bi.getCallback().accept(p, null);
            }
            bi.getCallback().accept(p, index);
            bi.cancel();
        }
    }
}
