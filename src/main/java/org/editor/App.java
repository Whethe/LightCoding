package org.editor;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import java.io.File;

/**
 * Main Class
 * 2023/1/16
 */
public class App 
{
    private final static String CONFIG_PATH = "config/defaultConfig.yaml";
    private final static String USERsCONFIG_PATH = "src/main/resources/config/userConfig.yaml";
    private static Config config = new Config();
//    static Logger logger  =  Logger.getLogger(App.class);

    public static void main( String[] args )
    {

        YamlReader yamlReader;

        if (fileChecker(USERsCONFIG_PATH)) {
            yamlReader = new YamlReader(USERsCONFIG_PATH);
        }else{
            yamlReader = new YamlReader(CONFIG_PATH);
        }

        config = YamlReader.getConfig();

        if( SystemInfo.isMacOS ) {
            // enable screen menu bar
            // (moves menu bar from JFrame window to top of screen)
            System.setProperty( "apple.laf.useScreenMenuBar", "true" );

            // application name used in screen menu bar
            // (in first menu after the "apple" menu)
            System.setProperty( "apple.awt.application.name", "FlatLaf Demo" );

            // appearance of window title bars
            // possible values:
            //   - "system": use current macOS appearance (light or dark)
            //   - "NSAppearanceNameAqua": use light appearance
            //   - "NSAppearanceNameDarkAqua": use dark appearance
            // (needs to be set on main thread; setting it on AWT thread does not work)
            System.setProperty( "apple.awt.application.appearance", "system" );
        }

        // Linux
        if( SystemInfo.isLinux ) {
            // enable custom window decorations
            JFrame.setDefaultLookAndFeelDecorated( true );
            JDialog.setDefaultLookAndFeelDecorated( true );
        }

        try {
            UIManager.setLookAndFeel( getFlatTheme());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        JFrame.setDefaultLookAndFeelDecorated( true );

        MainFrame mainFrame = MainFrame.getInstance(config);
    }

    /**File Checker: to check if the cfg file is exists.
     * @param filepath cfg path
     * @return boolean
     */
    public static boolean fileChecker( String filepath ) {
        File file=new File(filepath);
        if (file.exists()) {
            if(file.isDirectory()){
                //logger.info("File exists!");
                return true;
            }else{
                //logger.info("File does not exists!");
                return false;
            }
        }else{
            //logger.info("Directory: "+file.getParent()+" does not exist!");
            return false;
        }
    }

    public static FlatLaf getFlatTheme() {
        switch (config.getWindowTheme()){
            case "Dark":
                return new FlatDarkLaf();
            case "IntelliJ":
                return new FlatIntelliJLaf();
            case "Light":
                return new FlatLightLaf();
            case "Darcula":
                return new FlatDarculaLaf();
        }
        return null;
    }
}
