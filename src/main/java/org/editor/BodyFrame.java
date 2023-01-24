package org.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.*;

public class BodyFrame extends JPanel{
    ArrayList<Tab> tabs = new ArrayList<>();

    JSplitPane TextAreaJSplitPane = new JSplitPane();
    JTabbedPane closableTabsLabel = new JTabbedPane();

    String iconPath = "images/fileicon/";
    Map<String, String> fileIcons = new HashMap<>();
    //==== controlBar ================
//    JToolBar controlBar = new JToolBar();
//    JLabel jLabel = new JLabel();
//    JPanel controlPanel = new JPanel();
//    String[] listData = new String[]{"ActionScript", "Assembler X86", "Assembler 6502", "C", "Clojure","C++", "C#", "CSS", "CSV", "GO", "HTML","Java","Java Script", "JSON", "Lua","Latex",
//            "Markdown", "Objective-C", "Perl", "PHP", "Plain Text", "Python", "R", "Ruby", "SQL", "Windows Batch", "XML", "XSL", "YAML"};

//    JComboBox<String> changeSyntaxComboBox = new JComboBox<>(listData);

    String fileType;

    //==== Yaml data ================
    private final String theme;
    private final String fontStyle;
    private final int fontSize;

    BodyFrame(String theme, String fontStyle, int fontSize) {
        this.theme = theme;
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;

        setLayout(new BorderLayout());
    }

    {
        //==== file icons =======================
        {
            fileIcons.put("css", iconPath+"css.svg");
            fileIcons.put("java", iconPath+"java2.svg");
            fileIcons.put("svg", iconPath+"svg.svg");
            fileIcons.put("yml", iconPath+"yml.svg");
            fileIcons.put("yaml", iconPath+"yml.svg");
            fileIcons.put("py", iconPath+"python.svg");
            fileIcons.put("js", iconPath+"js.svg");
            fileIcons.put("jpg", iconPath+"pic.svg");
            fileIcons.put("png", iconPath+"pic.svg");
            fileIcons.put("c", iconPath+"c.svg");
            fileIcons.put("cpp", iconPath+"cpp.svg");
            fileIcons.put("cs", iconPath+"cs.svg");
            fileIcons.put("go", iconPath+"go.svg");
            fileIcons.put("json", iconPath+"json.svg");
        }
    }

    public BodyFrame TextPanel(){
        closableTabsLabel.putClientProperty( TABBED_PANE_TAB_CLOSABLE, true );
        closableTabsLabel.putClientProperty( TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close" );
        closableTabsLabel.putClientProperty( TABBED_PANE_SHOW_TAB_SEPARATORS, true );
        closableTabsLabel.putClientProperty( TABBED_PANE_TAB_CLOSE_CALLBACK,
                (BiConsumer<JTabbedPane, Integer>) (tabPane, tabIndex) -> {
                    AWTEvent e = EventQueue.getCurrentEvent();
                    int modifiers = (e instanceof MouseEvent) ? ((MouseEvent)e).getModifiers() : 0;
//                    JOptionPane.showMessageDialog( this, "Closed tab '" + tabPane.getTitleAt( tabIndex ) + "'."
//                                    + "\n\n(modifiers: " + MouseEvent.getMouseModifiersText( modifiers ) + ")",
//                            "Tab Closed", JOptionPane.PLAIN_MESSAGE );
                    closableTabsLabel.remove(tabIndex);
                    tabs.remove((int)tabIndex);
                } );

        changeTabUI();

        add(closableTabsLabel, BorderLayout.CENTER);
        return this;
    }

    public void setThemes(String theme, RSyntaxTextArea text) {
        Theme themes;
        try {
            themes = Theme.load(getClass().getResourceAsStream("/themes/"+theme+".xml"));
            themes.apply(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set closableTabsLabel Style
     * */
    private void changeTabUI() {
        closableTabsLabel.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT);

        UIManager.put( "TabbedPane.closeArc", 999 );
        UIManager.put( "TabbedPane.closeCrossFilledSize", 5.5f );
        UIManager.put( "TabbedPane.closeIcon", new FlatTabbedPaneCloseIcon() );
        closableTabsLabel.updateUI();
        UIManager.put( "TabbedPane.closeArc", null );
        UIManager.put( "TabbedPane.closeCrossFilledSize", null );
        UIManager.put( "TabbedPane.closeIcon", null );
    }

    public void addTab(String TabName){
        Tab tab = new Tab(TabName);
        Tab p = tab.initRSyntaxTextArea(OutLook(), null);
        closableTabsLabel.addTab(TabName, new FlatSVGIcon( iconPath + "file_txt.svg" ), p);
        closableTabsLabel.setSelectedIndex(closableTabsLabel.getTabCount()-1);
        tabs.add(p);
    }

    public void addTab(String TabName, String filepath, JTextArea ta) {
        String fileType = getFiletype(filepath);
        Tab tab = new Tab(TabName, filepath, fileType);
        String icon = "";

        if (Objects.equals(fileType, "txt")){
            icon = iconPath + "file_txt.svg";
        }else{
            icon = fileIcons.getOrDefault(fileType, iconPath + "file_txt.svg");
        }

        Tab p = tab.initRSyntaxTextArea(OutLook(), ta);
        closableTabsLabel.addTab(TabName, new FlatSVGIcon( icon ), p);
        closableTabsLabel.setSelectedIndex(closableTabsLabel.getTabCount()-1);

        tabs.add(p);
    }

    public TextEditorPane OutLook() {
        TextEditorPane tep = new TextEditorPane();

        Font font=new Font(fontStyle ,Font.PLAIN, fontSize);
        setThemes(theme, tep);
        tep.setFont(font);

        return tep;
    }

    public String getFiletype(String filepath) {
        String fileName = filepath.substring(filepath.lastIndexOf("\\")+1);
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    public void setSyntaxEditingStyle(String fileType) {

    }

    public void closeTabs() {

    }
    public Tab getTabsTextArea(int tabIndex) {
        if (tabs.isEmpty()) return null;
        return tabs.get(tabIndex);
    }

}
