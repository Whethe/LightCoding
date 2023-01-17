package org.editor;

import java.awt.GridLayout;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import javax.swing.*;


public class addTab implements MouseListener{

    JPanel jp;

    JLabel lab ;

    JLabel lab3 = new JLabel();

    public addTab(){

        lab = new JLabel("选项卡1");

        JLabel lab1 = new JLabel("选项卡");

        jp = new JPanel();

        GridLayout gl = new GridLayout(1,1,10,0);

        jp.setLayout(gl);

        lab1.setHorizontalAlignment(JLabel.LEFT);//设置文字显示在最左边

        lab3.setHorizontalAlignment(JLabel.RIGHT);// 设置文字显示在最右边

        jp.add(lab1);

        jp.add(lab3);

        tab.pane.addTab("i",lab);

        tab.pane.setTabComponentAt(tab.pane.indexOfComponent(lab),jp);//实现这个功能的就这一条最重要的语句

        lab3.addMouseListener(this);

    }

    @Override

    public void mouseClicked(MouseEvent arg0) {

        // TODO Auto-generated method stub

        tab.pane.remove(tab.pane.indexOfTabComponent(jp));

    }

    @Override

    public void mouseEntered(MouseEvent e) {

        // TODO Auto-generated method stub

        lab3.setText("x ");

    }

    @Override

    public void mouseExited(MouseEvent arg0) {

        // TODO Auto-generated method stub

        lab3.setText("");

    }

    @Override

    public void mousePressed(MouseEvent arg0) {

        // TODO Auto-generated method stub

    }

    @Override

    public void mouseReleased(MouseEvent arg0) {

        // TODO Auto-generated method stub

    }
}

