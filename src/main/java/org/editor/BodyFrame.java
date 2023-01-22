package org.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.*;

public class BodyFrame extends JPanel{
    RSyntaxTextArea textArea;
    Map<Integer, Tab> tabs = new HashMap<>();

    JSplitPane TextAreaJSplitPane = new JSplitPane();
    JTabbedPane closableTabsLabel = new JTabbedPane();

    String iconPath = "images/fileicon/";

    //==== controlBar ================
    JToolBar controlBar = new JToolBar();
    JLabel jLabel = new JLabel();
    JPanel controlPanel = new JPanel();
    String[] listData = new String[]{"ActionScript", "Assembler X86", "Assembler 6502", "C", "Clojure","C++", "C#", "CSS", "CSV", "GO", "HTML","Java","Java Script", "JSON", "Lua","Latex",
            "Markdown", "Objective-C", "Perl", "PHP", "Plain Text", "Python", "R", "Ruby", "SQL", "Windows Batch", "XML", "XSL", "YAML"};
    Map<String, String> fileIcons = new HashMap<>();
    JComboBox<String> changeSyntaxComboBox = new JComboBox<>(listData);

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

    public JPanel TextPanel(){
        JPanel p = new JPanel(new BorderLayout());

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
                } );

        changeTabUI();

        p.add(closableTabsLabel, BorderLayout.CENTER);
        return p;
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

    public void addTab(String TabName) {
        closableTabsLabel.addTab(TabName, new FlatSVGIcon( iconPath + "file_txt.svg" ), getTextArea());
        closableTabsLabel.setSelectedIndex(closableTabsLabel.getTabCount()-1);
    }

    public void addTab(String TabName, String filepath, JTextArea ta) {
        String fileType = getFiletype(filepath);
        String icon = "";

        if (Objects.equals(fileType, "txt")){
            icon = iconPath + "file_txt.svg";
        }else{
            icon = fileIcons.getOrDefault(fileType, iconPath + "file_txt.svg");
        }

        closableTabsLabel.addTab(TabName, new FlatSVGIcon( icon ), getTextArea(ta, getFiletype(fileType)));
        closableTabsLabel.setSelectedIndex(closableTabsLabel.getTabCount()-1);
    }


    public JPanel getTextArea(){
        init();

        ControlPanel controlPanel1 = new ControlPanel();

        RSyntaxTextArea rsta = new RSyntaxTextArea(32, 80);

        rsta.addCaretListener(e -> {
            try {
                int pos = rsta.getCaretPosition();
                int lineOfC = rsta.getLineOfOffset(pos) + 1;
                int col = pos - rsta.getLineStartOffset(lineOfC - 1) + 1;

                controlPanel1.setLabelText( lineOfC, col);
                System.out.println("Line: " + lineOfC + ", Column: " + col);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Font font=new Font(fontStyle ,Font.PLAIN, fontSize);
        rsta.setSyntaxEditingStyle(null);
        rsta.setCodeFoldingEnabled(true);
        setThemes(theme, rsta);
        rsta.setFont(font);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new RTextScrollPane(rsta), BorderLayout.CENTER);
        p.add(controlPanel1, BorderLayout.SOUTH);

        return p;
    }

    public JPanel getTextArea(JTextArea ta, String Filetype){
        init();

        ControlPanel controlPanel1 = new ControlPanel();
        RSyntaxTextArea rsta = new RSyntaxTextArea(32, 80);

        rsta.addCaretListener(e -> {
            try {
                int pos = rsta.getCaretPosition();
                int lineOfC = rsta.getLineOfOffset(pos) + 1;
                int col = pos - rsta.getLineStartOffset(lineOfC - 1) + 1;

                controlPanel1.setLabelText( lineOfC, col);
                System.out.println("Line: " + lineOfC + ", Column: " + col);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Font font=new Font(fontStyle ,Font.PLAIN, fontSize);
        rsta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        rsta.setCodeFoldingEnabled(true);
        rsta.setMarkOccurrences(true);
        setThemes(theme, rsta);
        rsta.setFont(font);

        rsta.setText(ta.getText());
        JPanel p = new JPanel(new BorderLayout());
        p.add(new RTextScrollPane(rsta), BorderLayout.CENTER);
        p.add(controlPanel1, BorderLayout.SOUTH);

        return p;
    }

    public void init() {
        textArea = new RSyntaxTextArea(32, 80);
    }

    public String getFiletype(String filepath) {
        String fileName = filepath.substring(filepath.lastIndexOf("\\")+1);

        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    public void setSyntaxEditingStyle(String fileType) {

    }
}
