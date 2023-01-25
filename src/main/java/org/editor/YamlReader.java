package org.editor;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Read in config.yaml and load it into Config.Class
 *
 * @author Haosen
 * @version 1.0
 */

public class YamlReader {
    private static HashMap<String, Map<String, Object>> properties;

    private static final Config config;

    //----init------------------------------
    static {
        properties = new HashMap<>();
        config = new Config();
    }

    YamlReader(String filePath){
        Yaml yaml = new Yaml();
        try{
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
            properties = yaml.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---- Save Window keys --------------------
        {
            config.setWidth((Integer) getValueByKey("Window.width"));
            config.setHeight((Integer) getValueByKey("Window.height"));
            config.setColor((String) getValueByKey("Window.color"));
            config.setWindowTheme((String) getValueByKey("Window.theme"));
        }

        //---- Save Body keys --------------------
        {
            config.setFontSize((Integer) getValueByKey("Body.fontSize"));
            config.setFontStyle((String) getValueByKey("Body.fontStyle"));
            config.setTextAreaTheme((String) getValueByKey("Body.theme"));
        }
    }

    public static Config getConfig() {
        return config;
    }

    /**
     * get yaml property
     * @param key the value's key, for instance: Window.theme, Body.fontsize...
     *            use dots to attach these keys
     * @return the value of the kye
     */
    public Object getValueByKey(@NotNull String key) {
        String separator = ".";
        String[] separatorKeys;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return properties.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) properties.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }
}
