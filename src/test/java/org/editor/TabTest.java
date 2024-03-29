package org.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Scanner;

public class TabTest {
    @Test
    void test1() {
        Tab tab = new Tab("test", " ", "java");
        tab.initRSyntaxTextArea(new RSyntaxTextArea(32, 80), null);
        JFrame frame = new JFrame();
        frame.add(tab);
        frame.setVisible(true);

        Scanner input = new Scanner(System.in);
        while (input.hasNext())
            System.out.println(input.nextLine());
    }
}
