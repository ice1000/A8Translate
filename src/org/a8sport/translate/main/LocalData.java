package org.a8sport.translate.main;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ice1000 on 2017/4/13.
 *
 * @author ice1000
 */
public class LocalData {
    private static final Properties p;
    private static final File f;

    static {
        p = new Properties();
        f = new File("/a8temp/local.properties");
        try {
            p.load(new FileReader(f));
        } catch (IOException ignored) {
        }
    }

    public static void store(@NonNls String key, @NonNls String value) {
        p.put(key, value);
        try {
            p.store(new FileWriter(f), "Created by a8translate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static String read(@NonNls String key) {
        return p.getProperty(key);
    }
}
