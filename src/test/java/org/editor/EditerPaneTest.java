package org.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Scanner;

public class EditerPaneTest {
    TextEditorPane textEditorPane;
    @Test
    void test1() {
        Tab tab = new Tab("test", " ", "java");
        tab.initRSyntaxTextArea(new RSyntaxTextArea(32, 80), null);
        JFrame frame = new JFrame();

        textEditorPane = new TextEditorPane();
        textEditorPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textEditorPane.setVisible(true);
        frame.add(textEditorPane);
        frame.setVisible(true);

        Scanner input = new Scanner(System.in);
        while (input.hasNext())
            System.out.println(input.nextLine());
    }
}
