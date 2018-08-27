/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.APIs;

import Br.Unions.Bungee.Log;
import Br.Unions.CereerType;
import Br.Unions.Data;
import Br.Unions.SQL.UnionManager;
import Br.Unions.SQL.UserManager;
import Br.Unions.Union;
import Br.Unions.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class MessageManager {

    public static final String MESSAGE_CHANNEL = "UNIONMESSAGER";

    public enum MessageType {
        CreateUnion("CU", (m) -> {//创建或删除公会 >> 公会名 操作者 (+/-)
            ProxiedPlayer p = m.getSenderProxied();
            Boolean has = UserManager.operateUser(p.getUniqueId(), User::hasUnion);
            if (has == null || !has) {
                boolean hasuni = UnionManager.operateUnion(m.getTargetUnion(), (t) -> {
                });
                if (hasuni) {
                    p.sendMessage(new TextComponent("§c已存在同名的公会了"));
                    return;
                }
                BungeeUnionCreateEvent buce = new BungeeUnionCreateEvent(p);
                BungeeCord.getInstance().getPluginManager().callEvent(buce);
                if (!buce.isCancelled()) {
                    Union u = new Union(m.getTargetUnion(), p.getUniqueId());
                    UnionManager.insertUnion(u);
                    p.sendMessage(new TextComponent("§6公会创建成功"));
                }
            } else {
                p.sendMessage(new TextComponent("§c你已经在一个公会中 无法创建新的公会"));
            }
        }),
        SetCereer("SC", (m) -> {//设置玩家的职位 >> 工会名 操作者 目标玩家(名) 职位
            ProxiedPlayer p = m.getSenderProxied();
            ProxiedPlayer tp = BungeeCord.getInstance().getPlayer(m.getData(0));
            if (tp == null || !tp.isConnected()) {
                p.sendMessage(new TextComponent("§c找不到玩家"));
                return;
            }
            CereerType ct = CereerType.valueOf(m.getData(1));
            UserManager.operateUser(tp.getUniqueId(), (u) -> {
                u.setCereer(ct);
            });
            String u = UserManager.operateUser(p.getUniqueId(), User::getBelongsUnion);
            p.sendMessage(new TextComponent("§6操作完成"));
            tp.sendMessage(new TextComponent("§6你在工会的职位已经被设置为: " + ct.getName()));
//            Boolean has = UserManager.operateUser(p.getUniqueId(), User::hasUnion); //以下代码希望能在bukkit完成判断
//            has = has == null ? false : has;
//            if (!has) {
//                p.sendMessage(new TextComponent("§c你不在任何工会中"));
//                return;
//            }
//            CereerType c = UserManager.operateUser(p.getUniqueId(), User::getCereer);
//            if (c != CereerType.Admin && c != CereerType.Owner) {
//                p.sendMessage(new TextComponent("§c你没有权限设置其他人的职位"));
//                return;
//            }
//            CereerType ct = CereerType.valueOf(m.getData(1));
//            if ((ct == CereerType.Admin || ct == CereerType.Owner) && c != CereerType.Owner) {
//                p.sendMessage(new TextComponent("§c你没有权限设置该职位"));
//                return;
//            }
//            ProxiedPlayer tp = BungeeCord.getInstance().getPlayer(UUID.fromString(m.getData(0)));
//            CereerType tc = UserManager.operateUser(tp.getUniqueId(), User::getCereer);
//            if(tc == CereerType.Admin && c != CereerType.Owner){
//                p.sendMessage(new TextComponent("§c你没有权限设置他的职位"));
//                return;
//            }
//            
        }),
        ModifyMember("MM", (m) -> {//加入或提出指定玩家 >> 工会名 操作者 目标玩家(名) (+/-)
            ProxiedPlayer tp = BungeeCord.getInstance().getPlayer(m.getData(0));
            if (tp == null || !tp.isConnected()) {
                m.getSenderProxied().sendMessage(new TextComponent("§c找不到玩家"));
                return;
            }
            Log log = Log.UnionLogs.get(m.getTargetUnion());
            switch (m.getData(1)) {
                case "+":
                    UserManager.operateUser(tp.getUniqueId(), (u) -> {
                        u.setBelongsUnion(m.getTargetUnion());
                    });
                    UnionManager.operateUnion(m.getTargetUnion(), (u) -> {
                        u.getMembers().add(tp.getUniqueId());
                    });
                    Log.UnionLogs.get(m.getTargetUnion()).log("§6" + m.getSenderProxied().getName() + " 同意了 " + tp.getName() + " 的加入请求");
                    tp.sendMessage(new TextComponent("§6你成功加入了" + m.getTargetUnion() + " 工会"));
                    break;
                case "-":
                    UserManager.operateUser(tp.getUniqueId(), (u) -> {
                        u.setBelongsUnion(null);
                    });
                    UnionManager.operateUnion(m.getTargetUnion(), (u) -> {
                        u.getMembers().remove(tp.getUniqueId());
                    });
                    Log.UnionLogs.get(m.getTargetUnion()).log("§6" + tp.getName() + " 退出了工会");
                    tp.sendMessage(new TextComponent("§6你成功离开了" + m.getTargetUnion() + " 工会"));
                    break;
            }
        }),
        InvitePlayer("IP", (m) -> {//邀请玩家 >> 工会名 操作者 目标玩家(名)
            if(m.getSender().equalsIgnoreCase(m.getData(0))){
                m.getSenderProxied().sendMessage(new TextComponent("§c你不能邀请你自己"));
                return;
            }
            ProxiedPlayer tp = BungeeCord.getInstance().getPlayer(m.getData(0));
            if (tp == null || !tp.isConnected()) {
                m.getSenderProxied().sendMessage(new TextComponent("§c找不到玩家"));
                return;
            }
            //TODO
        }),
        UnionLevelUp("UL"),//升级工会 >> 工会名 操作者
        DonateMoney("DM"),//捐献资金 >> 工会名 操作者 金钱数
        ReadLog("RL", (m) -> {//查看LOG >> 工会名 请求玩家 页数(从0开始 最大9)
            ProxiedPlayer p = BungeeCord.getInstance().getPlayer(m.getSender());
            Log log = Log.UnionLogs.get(m.getTargetUnion());
            int page = Integer.parseInt(m.getData(0));
            List<String> eLog = log.getLog(page);
            if (eLog.isEmpty()) {
                p.sendMessage(new TextComponent("§c本页没有数据"));
            } else {
                int index = 1;
                for (String s : eLog) {
                    p.sendMessage(new TextComponent(String.format("§6%d. %s", index++, s)));
                }
            }
            ComponentBuilder cb = new ComponentBuilder("");

            ComponentBuilder prev = new ComponentBuilder("---<< Prev");
            if (page != 0) {
                prev.color(ChatColor.GREEN);
                prev.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Unions:Unions log " + (page - 1)));
            } else {
                prev.color(ChatColor.GRAY);
            }
            cb.append(prev.create());

            cb.append("§b" + (page + 1) + "/10");

            ComponentBuilder next = new ComponentBuilder("Next >>---");
            if (page != 9) {
                next.color(ChatColor.GREEN);
                next.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Unions:Unions log " + (page + 1)));
            } else {
                next.color(ChatColor.GRAY);
            }
            cb.append(next.create());
            p.sendMessage(cb.create());
        }),
        Broadcost("BC", (m) -> {//发送公告 >> 工会名 操作者 信息
            String msg = m.getData(0);
            msg = String.format("§b§l[§e%s§b§l] §e§l[%s] §a >> %s", m.getTargetUnion(), m.getSender(), msg);
            TextComponent text = new TextComponent(msg);
            UnionManager.operateUnion(m.getTargetUnion(), (u) -> {
                u.getMembers()
                        .stream()
                        .map(BungeeCord.getInstance()::getPlayer)
                        .filter(p -> p != null && p.isConnected())
                        .forEach(p -> p.sendMessage(text));
            });
        });
        private String Key;
        private static Map<String, MessageType> Index = new HashMap<>();
        private Consumer<Message> Function;

        private MessageType(String key) {
            this.Key = key;
        }

        private MessageType(String key, Consumer<Message> func) {
            this.Key = key;
            this.Function = func;
        }

        public Consumer<Message> getFunction() {
            return Function;
        }

        public static MessageType getMessageType(String c) {
            return Index.get(c);
        }

        public String getKey() {
            return Key;
        }
    }

    public static class Message {

        private MessageType Type;
        private String TargetUnion;
        private String Sender;
        private List<String> ExtraData = new ArrayList<>();

        public ProxiedPlayer getSenderProxied() {
            return BungeeCord.getInstance().getPlayer(this.getSender());
        }

        public String getData(int index) {
            return ExtraData.get(index);
        }

        public static Message decodeMessage(String msg) {
            String s[] = msg.split("\\|");
            Message m = new Message();
            m.Type = MessageType.getMessageType(s[0]);
            m.TargetUnion = s[1];
            m.Sender = s[2];
            for (int i = 3; i < s.length; i++) {
                m.ExtraData.add(s[i]);
            }
            return m;
        }

        private Message() {
        }

        public static Message newMessage() {
            return new Message();
        }

        public Message type(MessageType t) {
            this.Type = t;
            return this;
        }

        public Message union(String u) {
            this.TargetUnion = u;
            return this;
        }

        public Message sender(String s) {
            this.Sender = s;
            return this;
        }

        public Message data(String... s) {
            for (String ss : s) {
                ExtraData.add(ss);
            }
            return this;
        }

        public void sendMessage() {
            String msg = this.Type.getKey() + "|" + this.TargetUnion + "|" + this.Sender;
            for (String s : ExtraData) {
                msg += "|" + s;
            }
            switch (Data.Plugin.getType()) {
                case Bukkit:
                    Bukkit.getServer().sendPluginMessage(Data.Plugin.getPlugin(), MessageManager.MESSAGE_CHANNEL, msg.getBytes());
                    break;
            }
        }

        public MessageType getType() {
            return Type;
        }

        public String getTargetUnion() {
            return TargetUnion;
        }

        public String getSender() {
            return Sender;
        }

        public List<String> getExtraData() {
            return ExtraData;
        }

    }

}
