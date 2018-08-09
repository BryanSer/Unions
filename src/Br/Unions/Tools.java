/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Unions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class Tools {

    public static void OutputFile(PluginProxy p, String res, File fold) throws IOException {
        InputStream is = p.getResource(res);
        if (is == null) {
            return;
        }

        if (fold == null) {
            fold = p.getDataFolder();
            if (!fold.exists()) {
                fold.mkdirs();
            }
        }
        if (!fold.exists()) {
            fold.mkdirs();
        }
        File f = new File(fold, res);

        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(f);
        while (true) {
            int i = is.read();
            if (i == -1) {
                break;
            }
            fos.write(i);
        }
        fos.close();
        is.close();
    }

    public static <T> T Bytes2Object(byte[] b, Class<? extends T> cls) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        return cls.cast(obj);
    }

    public static <T> T getObject(ResultSet sr, String index, Class<? extends T> cls) throws SQLException {
        try {
            return Bytes2Object(sr.getBytes(index), cls);
        } catch (SQLException ex) {
            return sr.getObject(index, cls);
        } catch (IOException ex) {
            return sr.getObject(index, cls);
        } catch (ClassNotFoundException ex) {
            return sr.getObject(index, cls);
        }
    }

    public static <T> T getObject(ResultSet sr, int index, Class<? extends T> cls) throws SQLException {
        try {
            return Bytes2Object(sr.getBytes(index), cls);
        } catch (SQLException ex) {
            return sr.getObject(index, cls);
        } catch (IOException ex) {
            return sr.getObject(index, cls);
        } catch (ClassNotFoundException ex) {
            return sr.getObject(index, cls);
        }
    }
}
