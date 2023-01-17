package org.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.awt.*;

public class BodyFrame extends JPanel{
        private JToolBar toolBarTop = new JToolBar();
        private JPanel panelBottom = new JPanel();
        private JComboBox changeSyntaxComboBox = new JComboBox<>();
        private JButton button1 = new JButton();
        private JLabel bottomLabel = new JLabel();
        private JPanel textAreaPanel = new JPanel();

        private RSyntaxTextArea textArea = new RSyntaxTextArea(32, 80);

    BodyFrame() {
            {
                bottomLabel.setText("0:0");
                panelBottom.add(bottomLabel, BorderLayout.WEST);
            }

            {
                textAreaPanel.add(textArea);
            }
            add(toolBarTop, BorderLayout.NORTH);
            add(textAreaPanel, BorderLayout.CENTER);
//            add(panelBottom, BorderLayout.SOUTH);
        }
}
