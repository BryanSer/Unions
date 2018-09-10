/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import Br.Unions.SQL.UserManager;
import java.io.Serializable;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class User implements Serializable {

    public static final long serialVersionUID = 0x00BEACDCD04A40430L;

    private String UserName;
    private UUID UUID;
    private String BelongsUnion;//玩家所属的工会名
    private CereerType Cereer;
    private int Contribution;
    private double Donate;

    public static class GET {

        public static String getBelongsUnion(UUID uid) {
            return UserManager.operateUser(uid, User::getBelongsUnion, false);
        }

        public static CereerType getCereer(UUID uid) {
            return UserManager.operateUser(uid, User::getCereer, false);
        }

        public static int getContribution(UUID uid) {
            return UserManager.operateUser(uid, User::getContribution, false);
        }

        public static double getDonate(UUID uid) {
            return UserManager.operateUser(uid, User::getDonate, false);
        }

        public static boolean hasUnion(UUID uid) {
            Boolean has = UserManager.operateUser(uid, User::hasUnion, false);
            return has == null ? false : has;
        }
    }

    public User(UUID uid, String name) {
        this.UUID = uid;
        this.UserName = name;
        this.clearData();
    }

    public String getBelongsUnion() {
        return BelongsUnion;
    }

    public void setBelongsUnion(String BelongsUnion) {
        this.BelongsUnion = BelongsUnion;
    }

    public CereerType getCereer() {
        return Cereer;
    }

    public void setCereer(CereerType Cereer) {
        this.Cereer = Cereer;
    }

    public int getContribution() {
        return Contribution;
    }

    public void setContribution(int Contribution) {
        this.Contribution = Contribution;
    }

    public double getDonate() {
        return Donate;
    }

    public void setDonate(double Donate) {
        this.Donate = Donate;
    }

    public void checkUserName() {
        OfflinePlayer p = Bukkit.getOfflinePlayer(this.UUID);
        if (!p.getName().equals(this.UserName)) {
            this.UserName = p.getName();
        }
    }

    public String getUserName() {
        return UserName;
    }

    public UUID getUUID() {
        return UUID;
    }

    public boolean hasUnion() {
        return this.BelongsUnion != null;
    }

    public void clearData() {
        this.BelongsUnion = null;
        this.Cereer = null;
        this.Contribution = 0;
        this.Donate = 0;
    }
}
