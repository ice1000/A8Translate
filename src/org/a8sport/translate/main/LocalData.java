package org.a8sport.translate.main;

import org.apache.commons.lang.StringUtils;
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
@SuppressWarnings("ResultOfMethodCallIgnored")
public class LocalData {
    private static final Properties p;
    private static final File f;
    public static final String PREFIX_NAME = "A8Translate";

    static {
        p = new Properties();
        f = new File(System.getProperty("user.home") + "/a8temp.properties");
        try {
            if (!f.exists()) f.createNewFile();
            p.load(new FileReader(f));
        } catch (IOException ignored) {
        }
    }

    public static void store(@NonNls String key, @NonNls String value) {
        p.put(StringUtils.uncapitalize(key), value);
        save();
    }

    public static void clear() {
        p.clear();
        save();
    }

    private static void save() {
        try {
            p.store(new FileWriter(f), "Created by a8translate");
        } catch (IOException ignored) {
        }
    }

    @Nullable
    public static String read(@NonNls String key) {
        return p.getProperty(StringUtils.uncapitalize(key));
    }
}
