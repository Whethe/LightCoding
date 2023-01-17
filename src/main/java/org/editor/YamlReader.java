package org.editor;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class YamlReader {
    private static HashMap<String, Map<String, Object>> properties;

    private static Config config;

    //----init------------------------------
    {
        properties = new HashMap<>();
        config = new Config();
    }

    YamlReader(String filePath){
        Yaml yaml = new Yaml();
        try{
            properties = yaml.load(readFile(filePath));
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

    /**
     * get yaml property
     * @param key key&value
     * @return values
     */
    public Object getValueByKey(String key) {
        String separator = ".";
        String[] separatorKeys = null;
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

    /**
     * read in yaml
     *
     * @param filepath filepath
     * @return values
     */
    private static String readFile(String filepath){
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(filepath));
            return new String (encoded, StandardCharsets.UTF_8);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getConfig() {
        return config;
    }
}
