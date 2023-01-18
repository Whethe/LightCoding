package org.editor;

import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.*;

public class BodyFrame extends JPanel{
    RSyntaxTextArea textArea = new RSyntaxTextArea(32, 80);
    JSplitPane TextAreaJSplitPane = new JSplitPane();
    JTabbedPane closableTabsLabel = new JTabbedPane();

    //==== controlBar ================
    JToolBar controlBar = new JToolBar();
    JLabel jLabel = new JLabel();
    JPanel controlPanel = new JPanel();
    String[] listData = new String[]{"C", "C#", "C++", "CSS", "GO", "HTML","Java","Java Script", "JSON", "Lua",
            "Markdown", "Objective-C", "Perl", "PHP", "Plain Text", "Python", "R", "Ruby", "Rust", "SQL", "XML", "XSL", "YAML"};
    JComboBox<String> changeSyntaxComboBox = new JComboBox<>(listData);

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

    //==== controlBar ================
    {
        controlBar.setMargin(new Insets(0, 0, 0, 0));
        jLabel.setText("Line 1, Column 1");
        controlBar.add(jLabel);

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel1.add(controlBar);

        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(jPanel1, BorderLayout.WEST);

        jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanel1.add(changeSyntaxComboBox);

        controlPanel.add(jPanel1, BorderLayout.EAST);
    }

    public JPanel TextPanel(){
        JPanel p = new JPanel(new BorderLayout());

        RTextScrollPane sp = new RTextScrollPane(getTextArea());

        closableTabsLabel = new JTabbedPane();
        closableTabsLabel.addTab("Untitled", sp);
        closableTabsLabel.setSelectedComponent(sp);

        closableTabsLabel.putClientProperty( TABBED_PANE_TAB_CLOSABLE, true );
        closableTabsLabel.putClientProperty( TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close" );
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

        p.add(sp, BorderLayout.CENTER);
        p.add(controlBar, BorderLayout.SOUTH);
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

    private void addTab(String TabName) {
        closableTabsLabel.addTab(TabName, getTextArea());
        closableTabsLabel.setSelectedIndex(closableTabsLabel.getTabCount()-1);
    }

    public RTextScrollPane getTextArea(){
        Font font=new Font(fontStyle ,Font.PLAIN, fontSize);
        textArea = new RSyntaxTextArea(32, 80);

        textArea.setSyntaxEditingStyle(null);
        textArea.setCodeFoldingEnabled(true);

        setThemes(theme, textArea);
        textArea.setFont(font);

        RTextScrollPane rTextScrollPane = new RTextScrollPane();
        rTextScrollPane.setLayout(new BorderLayout());
        rTextScrollPane.add(textArea, BorderLayout.CENTER);
        rTextScrollPane.add(controlBar, BorderLayout.SOUTH);

        textArea.setFont(font);
        return rTextScrollPane;
    }


}
