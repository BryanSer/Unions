/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions.SQL;

import Br.Unions.Tools;
import Br.Unions.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class UserManager {

    private static PreparedStatement getUser_ByUUID;
    private static PreparedStatement getUser_ByName;
    private static PreparedStatement updateUser;
    private static PreparedStatement insertUser;
    private static PreparedStatement deleteUser;

    public static void connect(Connection con) {
        try (Statement s = con.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS Users(UUID VARCHAR(255), User BLOB, Name VARCHAR(255), PRIMARY KEY(`UUID`)) ENGINE = InnoDB DEFAULT CHARSET=utf8");

            getUser_ByUUID = con.prepareStatement("SELECT User FROM Users WHERE UUID = ?");
            getUser_ByName = con.prepareStatement("SELECT User FROM Users WHERE Name = ?");
            updateUser = con.prepareStatement("UPDATE Users SET User = ?, Name = ? WHERE UUID = ?");
            insertUser = con.prepareStatement("INSERT INTO Users VALUES (?, ?, ?)");
            deleteUser = con.prepareStatement("DELETE FROM Users WHERE UUID = ?");
        } catch (SQLException s) {
        }
    }

    public static void deleteUser(UUID uid) {
        try {
            deleteUser.setString(1, uid.toString());
            deleteUser.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param uid
     * @param name
     * @param opt
     * @return true表示创建成功 false表示数据已存在
     */
    public static boolean createUser(UUID uid, String name, Consumer<User> opt) {
        Optional<User> o_u = getUser(uid);
        if (o_u.isPresent()) {
            return false;
        }
        User u = new User(uid, name);
        if (opt != null) {
            try {
                opt.accept(u);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        insertUser(u);
        return true;
    }

    public static <T> T operateUser(UUID uid, Function<User, T> opt, boolean update) {
        Optional<User> o_u = getUser(uid);
        if (o_u.isPresent()) {
            User u = o_u.get();
            try {
                T t = opt.apply(u);
                if (update) {
                    updateUser(u);
                }
                return t;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            return null;
        }
        return null;
    }

    /**
     *
     * @param uid
     * @param opt
     * @return 操作是否成功 若false表示数据不存在
     */
    public static boolean operateUser(UUID uid, Consumer<User> opt, boolean update) {
        Optional<User> o_u = getUser(uid);
        if (o_u.isPresent()) {
            User u = o_u.get();
            try {
                opt.accept(u);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            if (update) {
                updateUser(u);
            }
            return true;
        } else {
            return false;
        }
    }

    public static <T> T operateUser(UUID uid, Function<User, T> opt) {
        Optional<User> o_u = getUser(uid);
        if (o_u.isPresent()) {
            User u = o_u.get();
            try {
                T t = opt.apply(u);
                updateUser(u);
                return t;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            return null;
        }
        return null;
    }

    /**
     *
     * @param uid
     * @param opt
     * @return 操作是否成功 若false表示数据不存在
     */
    public static boolean operateUser(UUID uid, Consumer<User> opt) {
        Optional<User> o_u = getUser(uid);
        if (o_u.isPresent()) {
            User u = o_u.get();
            try {
                opt.accept(u);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            updateUser(u);
            return true;
        } else {
            return false;
        }
    }

    public static void insertUser(User u) {
        try {
            insertUser.setString(1, u.getUUID().toString());
            insertUser.setObject(2, u);
            insertUser.setString(3, u.getUserName());
            insertUser.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateUser(User u) {
        try {
            updateUser.setObject(1, u);
            updateUser.setString(2, u.getUserName());
            updateUser.setString(3, u.getUUID().toString());
            updateUser.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Optional<User> getUser(UUID uid) {
        try {
            getUser_ByUUID.setString(1, uid.toString());
            ResultSet rs = getUser_ByUUID.executeQuery();
            if (rs.next()) {
                User u = Tools.getObject(rs, 1, User.class);
                return Optional.ofNullable(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    private static Optional<User> getUser(String name) {
        try {
            getUser_ByName.setString(1, name);
            ResultSet rs = getUser_ByName.executeQuery();
            if (rs.next()) {
                User u = Tools.getObject(rs, 1, User.class);
                return Optional.ofNullable(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }
}
