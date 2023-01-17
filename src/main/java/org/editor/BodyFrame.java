package org.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BodyFrame extends JPanel{
    RSyntaxTextArea textArea = new RSyntaxTextArea(32, 80);
    JSplitPane TextAreaJSplitPane = new JSplitPane();
    BodyFrame() {

    }

    public JPanel TextPanel(String TextAreaTheme, String FontStyle, int FontSize){
        JPanel p = new JPanel(new BorderLayout());

        textArea = new RSyntaxTextArea(32, 80);
        textArea.setSyntaxEditingStyle(null);
        textArea.setCodeFoldingEnabled(true);
        setThemes(TextAreaTheme, textArea);

        Font font=new Font(FontStyle,Font.PLAIN,FontSize);
        textArea.setFont(font);

//        fileBar = new JTabbedPane();
//        fileBar.addTab(filename, sp);
//        fileBar.setSelectedComponent(sp);

        RTextScrollPane sp = new RTextScrollPane(textArea);
        p.add(sp, BorderLayout.CENTER);

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
}
