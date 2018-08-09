/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.SQL;

import Br.Unions.Tools;
import Br.Unions.Union;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class UnionManager {

    private static PreparedStatement getUnion;
    private static PreparedStatement updateUnion;
    private static PreparedStatement insertUnion;
    private static PreparedStatement deleteUnion;

    public static void connect(Connection con) {
        try (Statement s = con.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS Unions(UnionName VARCHAR(255), Union MEDIUMBLOB, PRIMARY KEY(`UnionName`)) ENGINE = InnoDB DEFAULT CHARSET=utf8");
            getUnion = con.prepareStatement("SELECT Union FROM Unions WHERE UnionName = ?");
            updateUnion = con.prepareStatement("UPDATE Unions SET Union = ? WHERE UnionName = ?");
            insertUnion = con.prepareStatement("INSERT INTO Uions VALUES(?, ?)");
            deleteUnion = con.prepareStatement("DELETE FROM Unions WHERE UnionName = ?");
        } catch (SQLException s) {
        }
    }

    public static <T> T operateUnion(String union, Function<Union, T> opt, boolean update) {
        Optional<Union> ou = getUnion(union);
        if (ou.isPresent()) {
            try {
                Union u = ou.get();
                T t = opt.apply(u);
                if (update) {
                    updateUnion(u);
                }
                return t;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    public static <T> T operateUnion(String union, Function<Union, T> opt) {
        Optional<Union> ou = getUnion(union);
        if (ou.isPresent()) {
            try {
                Union u = ou.get();
                T t = opt.apply(u);
                updateUnion(u);
                return t;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    /**
     *
     * @param union
     * @return 操作是否成功 若false表示数据不存在 或发生异常
     */
    public static boolean operateUnion(String union, Consumer<Union> opt, boolean update) {
        Optional<Union> ou = getUnion(union);
        if (ou.isPresent()) {
            try {
                Union u = ou.get();
                opt.accept(u);
                if (update) {
                    updateUnion(u);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return false;
    }

    /**
     *
     * @param union
     * @return 操作是否成功 若false表示数据不存在 或发生异常
     */
    public static boolean operateUnion(String union, Consumer<Union> opt) {
        Optional<Union> ou = getUnion(union);
        if (ou.isPresent()) {
            try {
                Union u = ou.get();
                opt.accept(u);
                updateUnion(u);
                return true;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return false;
    }

    public static void deleteUnion(Union u) {
        try {
            deleteUnion.setString(1, u.getUnionName());
            deleteUnion.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UnionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertUnion(Union u) {
        try {
            insertUnion.setString(1, u.getUnionName());
            insertUnion.setObject(2, u);
            insertUnion.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UnionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void updateUnion(Union u) {
        try {
            updateUnion.setObject(1, u);
            updateUnion.setString(2, u.getUnionName());
            updateUnion.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UnionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Optional<Union> getUnion(String unionName) {
        try {
            getUnion.setString(1, unionName);
            ResultSet sr = getUnion.executeQuery();
            Union u = Tools.getObject(sr, 1, Union.class);
            return Optional.ofNullable(u);
        } catch (SQLException ex) {
            Logger.getLogger(UnionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }
}
