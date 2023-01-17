package org.editor;

import org.junit.jupiter.api.Test;

import java.awt.*;

public class YamlReaderTest {
    private final static String CONFIG_PATH = "src/main/resources/config/defaultConfig.yaml";
    private final static String USERsCONFIG_PATH = "src/main/resources/userConfig.yaml";

    @Test
    void test1() {
        YamlReader yamlReader = new YamlReader(CONFIG_PATH);
        System.out.println(yamlReader.getValueByKey("Window.height"));
    }
}
