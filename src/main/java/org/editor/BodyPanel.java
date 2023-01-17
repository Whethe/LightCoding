package org.editor;

import javax.swing.*;
import java.awt.*;

public class BodyPanel extends JPanel{
    public JPanel mainPanel;
    private JToolBar toolBarTop;
    private JPanel panelBottom;
    private JComboBox changeSyntaxComboBox;
    private JButton button1;
    private JLabel bottomLabel;
    private JPanel textAreaPanel;

    BodyPanel() {
        {
            bottomLabel.setText("0:0");
            panelBottom.add(bottomLabel, BorderLayout.WEST);
        }
        add(panelBottom, BorderLayout.SOUTH);
        add(textAreaPanel, BorderLayout.CENTER);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
