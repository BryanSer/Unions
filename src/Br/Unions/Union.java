/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import Br.Unions.SQL.UnionManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class Union implements Serializable {

    public static final long serialVersionUID = 0x00BEACDDDFAE130430L;

    private String UnionName;//工会名
    private int UnionLevel;//工会等级
    private UUID OwnerName;//工会所有者
    private byte[] FlagByte;//工会旗帜数据
    private int Prestige;//工会威望
    private int TotalContribution;//工会当前贡献
    private int ContributionWeek;//工会本周获得的贡献
    private int Bank;//工会银行

    public Union(String UnionName, UUID OwnerName) {
        this.UnionName = UnionName;
        this.OwnerName = OwnerName;
        UnionLevel = 0;
        FlagByte = null;
        Prestige = 0;
        TotalContribution = 0;
        ContributionWeek = 0;
        Bank = 0;
    }

    public static class OPT {

        public static int getUnionLevel(String uname) {
            return UnionManager.operateUnion(uname, Union::getUnionLevel, false);
        }

        public static UUID getOwnerName(String uname) {
            return UnionManager.operateUnion(uname, Union::getOwnerName, false);
        }

        public static List<UUID> getMembers(String uname) {
            return Collections.unmodifiableList(UnionManager.operateUnion(uname, Union::getMembers, false));
        }

        public static byte[] getFlagByte(String uname) {
            return UnionManager.operateUnion(uname, Union::getFlagByte, false);
        }

        private static Consumer<Union> CLAMBDA = (u) -> {
        };

        public static boolean hasUnion(String uname) {
            return UnionManager.operateUnion(uname, CLAMBDA, false);
        }

        public static int getPrestige(String uname) {
            return UnionManager.operateUnion(uname, Union::getPrestige, false);
        }

        public static int getTotalContribution(String uname) {
            return UnionManager.operateUnion(uname, Union::getTotalContribution, false);
        }

        public static int getContributionWeek(String uname) {
            return UnionManager.operateUnion(uname, Union::getContributionWeek, false);
        }

        public static int getBank(String uname) {
            return UnionManager.operateUnion(uname, Union::getBank, false);
        }

        public static void setFlagByte(String uname, byte[] data) {
            UnionManager.operateUnion(uname, m -> {
                m.setFlagByte(data);
            });
        }

        public static void setUnionLevel(String uname, int lv) {
            UnionManager.operateUnion(uname, m -> {
                m.setUnionLevel(lv);
            });
        }

        public static void setPrestige(String uname, int p) {
            UnionManager.operateUnion(uname, m -> {
                m.setPrestige(p);
            });
        }

        public static void setTotalContribution(String uname, int tc) {
            UnionManager.operateUnion(uname, m -> {
                m.setTotalContribution(tc);
            });
        }

        public static void setContributionWeek(String uname, int cw) {
            UnionManager.operateUnion(uname, m -> {
                m.setContributionWeek(cw);
            });
        }

        public static void setBank(String uname, int b) {
            UnionManager.operateUnion(uname, m -> {
                m.setBank(b);
            });
        }
    }

    private List<UUID> Members;

    public String getUnionName() {
        return UnionName;
    }

    public int getUnionLevel() {
        return UnionLevel;
    }

    public UUID getOwnerName() {
        return OwnerName;
    }

    public List<UUID> getMembers() {
        return Members;
    }

    public byte[] getFlagByte() {
        return FlagByte;
    }

    public void setUnionLevel(int UnionLevel) {
        this.UnionLevel = UnionLevel;
    }

    public void setFlagByte(byte[] FlagByte) {
        this.FlagByte = FlagByte;
    }

    public int getPrestige() {
        return Prestige;
    }

    public void setPrestige(int Prestige) {
        this.Prestige = Prestige;
    }

    public int getTotalContribution() {
        return TotalContribution;
    }

    public void setTotalContribution(int TotalContribution) {
        this.TotalContribution = TotalContribution;
    }

    public int getContributionWeek() {
        return ContributionWeek;
    }

    public void setContributionWeek(int ContributionWeek) {
        this.ContributionWeek = ContributionWeek;
    }

    public int getBank() {
        return Bank;
    }

    public void setBank(int Bank) {
        this.Bank = Bank;
    }

}
