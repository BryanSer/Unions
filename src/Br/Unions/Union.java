/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class Union implements Serializable {

    public static final long serialVersionUID = 0x00BEACDDDFAE130430L;
    
    private String UnionName;
    private int UnionLevel;
    private UUID OwnerName;
    private byte[] FlagByte;

    public Union(String UnionName, UUID OwnerName) {
        this.UnionName = UnionName;
        this.OwnerName = OwnerName;
        UnionLevel = 0;
        FlagByte = null;
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
    
    
}
