package org.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Scanner;

public class TabTest {
    @Test
    void test1() {
        Tab tab = new Tab("test");
        tab.initRSyntaxTextArea(new RSyntaxTextArea(32, 80), null);
        JFrame frame = new JFrame();
        frame.add(tab);
        frame.setVisible(true);

        Scanner input = new Scanner(System.in);
        while (input.hasNext())
            System.out.println("你输入了:"+input.nextLine());
        System.out.println("我赌它执行不到我");

    }
}
