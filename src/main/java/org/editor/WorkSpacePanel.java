package org.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

/**
 * SideBar panel, to deliver a project folder structure
 * @author Haosen
 */
public class WorkSpacePanel extends JPanel {
    private String projectFolderPath;
    static DefaultTreeModel newModel;
    static DefaultMutableTreeNode Node;
    static DefaultMutableTreeNode temp;
    static JTree tree;
    DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
    WorkSpacePanel() {
        this.setLayout(new BorderLayout());

        setSize(400, 600 );
    }

    private void initRender() {
        render.setOpenIcon(new ImageIcon( "images/folder-open.svg" ));
        render.setClosedIcon(new ImageIcon( "images/folder.svg" ));
        render.setLeafIcon(new ImageIcon( "images/file.svg" ));
    }

    public void initJTree(String folderPath) {
        this.projectFolderPath = folderPath;

        if (projectFolderPath == null) return;

        Node = traverseFolder(projectFolderPath);
        tree = new JTree(Node);
//        newModel=new DefaultTreeModel(Node);

        tree.setShowsRootHandles(true);
        tree.setEditable(true);
        tree.setCellRenderer(render);
        initRender();

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(tree);
        this.add(sp, BorderLayout.CENTER);
        updateUI();
//        repaint();
    }

    public String getProjectFolderPath() {
        return projectFolderPath;
    }

    public void setProjectFolderPath(String projectFolderPath) {
        this.projectFolderPath = projectFolderPath;
    }

    public static @Nullable DefaultMutableTreeNode traverseFolder(String path) {
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(new File(path).getName());
        File file = new File(path);

        if (file.exists()) {
            File[] files = file.listFiles();
            assert files != null;
            if (files.length == 0) {
                if(file.isDirectory()) {
                    return new DefaultMutableTreeNode(file.getName(), false);
                }
            }else{
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        parentNode.add(traverseFolder(file2.getAbsolutePath()));
                    }else{
                        temp=new DefaultMutableTreeNode(file2.getName());
                        parentNode.add(temp);
                    }
                }
            }
        } else {
            return null;
        }
        return parentNode;
    }

}
