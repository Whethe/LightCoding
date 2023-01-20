package org.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    JToolBar controlBar = new JToolBar();
    JLabel jLabel = new JLabel();
    JLabel jLabel2 = new JLabel();
    String[] listData = new String[]{"ActionScript", "Assembler X86", "Assembler 6502", "C", "Clojure","C++", "C#", "CSS", "CSV", "GO", "HTML","Java","Java Script", "JSON", "Lua","Latex",
            "Markdown", "Objective-C", "Perl", "PHP", "Plain Text", "Python", "R", "Ruby", "Rust", "SQL", "XML", "XSL", "YAML"};
    JComboBox<String> changeSyntaxComboBox = new JComboBox<>(listData);

    ControlPanel() {
        setLayout(new BorderLayout());

        controlBar.setMargin(new Insets(0, 0, 0, 0));
        setLabelText(1 ,1);
        controlBar.add(jLabel);

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel1.add(controlBar);

        add(jPanel1, BorderLayout.WEST);

//        changeSyntaxComboBox.setSelectedIndex(x);
        jLabel2.setText("Current: ");

        jPanel1 = new JPanel(new BorderLayout());
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jPanel1.add(changeSyntaxComboBox, BorderLayout.EAST);
        jPanel1.add(jLabel2, BorderLayout.WEST);
        add(jPanel1, BorderLayout.EAST);
    }

    public void setSelectSyntax(String fileType) {

    }

    public void setLabelText(int line, int column) {
        jLabel.setText("Line: " + line + ", Column: " + column);
    }
    public void setLabel2Text(String syntax) {
        jLabel2.setText("Current: " + syntax);
    }
}
