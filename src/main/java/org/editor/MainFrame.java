package org.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGUtils;
import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.search.FindToolBar;
import org.fife.rsta.ui.search.ReplaceToolBar;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.formdev.flatlaf.FlatLaf.updateUI;

public class MainFrame extends JFrame implements SearchListener {
    private volatile static MainFrame mainFrame;

    private String recentFile;

    JSplitPane TextAreaJSplitPane = new JSplitPane();
    BodyPanel bodyPanel;
    String workSpacePath = "";
    WorkSpacePanel workSpacePanel = new WorkSpacePanel();

    private boolean currentPageChanged;

    private MainFrame (){
        this(DEFAULT_CONFIG);
    }

    private MainFrame (Config config){
        this.config = (config == null ? DEFAULT_CONFIG : config);

        assert config != null;
        initFrame(config);
    }

    public static MainFrame getInstance(Config config) {
        if (mainFrame == null) {
            synchronized (MainFrame.class) {
                if (mainFrame == null) {
                    mainFrame = new MainFrame(config);
                }
            }
        }
        return mainFrame;
    }

    private void initFrame(Config config) {
        bodyPanel = new BodyPanel(config.getTextAreaTheme(), config.getFontStyle(), config.getFontSize());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        windowHeight = config.getHeight();
        windowWidth = config.getWidth();

        //---- Set window in the middle of screen---------------
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        screenHeight = (int) screenSize.getHeight();
        screenWidth = (int) screenSize.getWidth();

        setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);

        //---- window --------------------
        setJMenuBar(menuBar1);
        getRootPane().setBackground(Color.darkGray);
        setSize(windowWidth, windowHeight);
        setIconImages(FlatSVGUtils.createWindowIconImages( "/images/div.svg" ));

        //TextAreaJSplitPane
        TextAreaJSplitPane.setOneTouchExpandable(true);
        TextAreaJSplitPane.setContinuousLayout(true);

        setVisible(true);

        add(contentPanel, BorderLayout.NORTH);
        bodyPanel = bodyPanel.TextPanel();
        add(bodyPanel, BorderLayout.CENTER);
    }

    private static final Config DEFAULT_CONFIG = new Config();

    JPanel contentPanel = new JPanel();

    private static int windowHeight;
    private static int windowWidth;
    private static int screenHeight;
    private static int screenWidth;

    private Config config;

    //---- menu ----------------------
    JMenuBar menuBar1 = new JMenuBar();
    JMenu fileMenu = new JMenu();
    JMenuItem newMenuItem = new JMenuItem();
    JMenuItem openMenuItem = new JMenuItem();
    JMenuItem openFolderMenuItem = new JMenuItem();
    JMenuItem saveAsMenuItem = new JMenuItem();
    JMenuItem closeMenuItem = new JMenuItem();
    JMenuItem exitMenuItem = new JMenuItem();

    JMenu editMenu = new JMenu();
    JMenuItem undoMenuItem = new JMenuItem();
    JMenuItem redoMenuItem = new JMenuItem();
    JMenuItem cutMenuItem = new JMenuItem();
    JMenuItem copyMenuItem = new JMenuItem();
    JMenuItem pasteMenuItem = new JMenuItem();
    JMenuItem selectAllMenuItem = new JMenuItem();
    JMenuItem deleteMenuItem = new JMenuItem();

    JMenu findMenu = new JMenu();
    JMenuItem findMenuItem = new JMenuItem();
    JMenuItem findNextMenuItem = new JMenuItem();
    JMenuItem findPreviousMenuItem = new JMenuItem();
    JMenuItem replaceMenuItem = new JMenuItem();
    JMenuItem replaceNextMenuItem = new JMenuItem();
    JMenuItem showFindSearchBarMenuItem = new JMenuItem();
    JMenuItem showReplaceSearchBarMenuItem = new JMenuItem();
    JMenuItem useSelectForFindMenuItem = new JMenuItem();
    JMenuItem useSelectForReplaceMenuItem = new JMenuItem();
    CollapsibleSectionPanel csp;
    FindToolBar findToolBar;
    ReplaceToolBar replaceToolBar;


    //---- toolbar -----------------------
    JToolBar toolBar = new JToolBar();
    JToolBar toolBar2 = new JToolBar();
    JButton backButton = new JButton();
    JButton forwardButton = new JButton();
    JButton cutButton = new JButton();
    JButton copyButton = new JButton();
    JButton pasteButton = new JButton();
    JButton searchButton = new JButton();
    JButton refreshButton = new JButton();
    JButton addButton = new JButton();
    JToggleButton sidebarButton = new JToggleButton();

    //======== menuBar1 ========
    {
        //======== fileMenu ========
        {
            fileMenu.setText("File");
            fileMenu.setMnemonic('F');

            //---- newMenuItem ----
            newMenuItem.setText("New");
            newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            newMenuItem.setMnemonic('N');
            newMenuItem.addActionListener(this::actionPerformed);
            fileMenu.add(newMenuItem);

            //---- openFolder ----
            openFolderMenuItem.setText("Open Folder");
            openFolderMenuItem.addActionListener(this::actionPerformed);
            fileMenu.add(openFolderMenuItem);

            //---- openMenuItem ----
            openMenuItem.setText("Open...");
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            openMenuItem.setMnemonic('O');
            openMenuItem.addActionListener(e -> openActionPerformed());
            fileMenu.add(openMenuItem);

            //---- saveAsMenuItem ----
            saveAsMenuItem.setText("Save As...");
            saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            saveAsMenuItem.setMnemonic('S');
            saveAsMenuItem.addActionListener(this::actionPerformed);
            fileMenu.add(saveAsMenuItem);
            fileMenu.addSeparator();

            //---- closeMenuItem ----
            closeMenuItem.setText("Close");
            closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            closeMenuItem.setMnemonic('C');
            closeMenuItem.addActionListener(this::actionPerformed);
            fileMenu.add(closeMenuItem);
            fileMenu.addSeparator();

            //---- exitMenuItem ----
            exitMenuItem.setText("Exit");
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            exitMenuItem.setMnemonic('X');
            exitMenuItem.addActionListener(this::actionPerformed);
            fileMenu.add(exitMenuItem);
        }
        menuBar1.add(fileMenu);

        //======== editMenu ========
        {
            editMenu.setText("Edit");
            editMenu.setMnemonic('E');

            //---- undoMenuItem ----
            undoMenuItem.setText("Undo");
            undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            undoMenuItem.setMnemonic('U');
            undoMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(undoMenuItem);

            //---- redoMenuItem ----
            redoMenuItem.setText("Redo");
            redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            redoMenuItem.setMnemonic('R');
            redoMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(redoMenuItem);
            editMenu.addSeparator();

            //---- cutMenuItem ----
            cutMenuItem.setText("Cut");
            cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            cutMenuItem.setMnemonic('C');
            cutMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(cutMenuItem);

            //---- copyMenuItem ----
            copyMenuItem.setText("Copy");
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            copyMenuItem.setMnemonic('O');
            copyMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(copyMenuItem);

            //---- pasteMenuItem ----
            pasteMenuItem.setText("Paste");
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            pasteMenuItem.setMnemonic('P');
            pasteMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(pasteMenuItem);

            //---- selectAllMenuItem ----
            selectAllMenuItem.setText("Select All");
            selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            selectAllMenuItem.setMnemonic('S');
            selectAllMenuItem.addActionListener(this::editActionPerformedEdit);
            editMenu.add(selectAllMenuItem);
            editMenu.addSeparator();

            //---- deleteMenuItem ----
            deleteMenuItem.setText("Delete");
            deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
            deleteMenuItem.setMnemonic('D');
//            deleteMenuItem.addActionListener(e -> menuItemActionPerformed(e));
            editMenu.add(deleteMenuItem);
        }
        menuBar1.add(editMenu);

        //======== findMenu ========
        {
            int ctrl = getToolkit().getMenuShortcutKeyMask();
            int shift = InputEvent.SHIFT_MASK;
            csp = new CollapsibleSectionPanel();


            findMenu.setText("Find");
            findMenu.setMnemonic('F');

            //---- findMenuItem ----
            findMenuItem.setText("Find");
            findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F, ctrl);
            Action a = csp.addBottomComponent(ks, findToolBar);
            a.putValue(Action.NAME, "Find");
            findMenu.add(new JMenuItem(a));

            //---- findNextMenuItem ----
            findNextMenuItem.setText("Find Next");
            a.putValue(Action.NAME, "Find Next");
            findMenu.add(new JMenuItem(a));

            //---- findPreviousMenuItem ----
            findPreviousMenuItem.setText("Find Previous");
            findNextMenuItem.setText("Find Previous");
            a.putValue(Action.NAME, "Find Previous");
            findMenu.add(new JMenuItem(a));
            findMenu.addSeparator();

            //---- replaceMenuItem ----
            replaceMenuItem.setText("Replace");
            replaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            findMenu.add(replaceMenuItem);

            //---- replaceNextMenuItem ----
            replaceNextMenuItem.setText("Replace Next");
            findMenu.add(replaceNextMenuItem);
            findMenu.addSeparator();

            //---- showFindSearchBarMenuItem ----
            showFindSearchBarMenuItem.setText("Show Find Search Bar");
            findMenu.add(showFindSearchBarMenuItem);

            //---- showReplaceSearchBarMenuItem ----
            showReplaceSearchBarMenuItem.setText("Show Replace Search Bar");
            findMenu.add(showReplaceSearchBarMenuItem);
            findMenu.addSeparator();

            //---- useSelectForFindMenuItem ----
            useSelectForFindMenuItem.setText("Use Selection For Find");
            findMenu.add(useSelectForFindMenuItem);

            //---- useSelectForReplaceMenuItem ----
            useSelectForReplaceMenuItem.setText("Use Selection For Replace");
            findMenu.add(useSelectForReplaceMenuItem);
        }
        menuBar1.add(findMenu);

    }

    //======== Bar ========
    {
        //======== toolBar Button ========
        {
            toolBar.setMargin(new Insets(1, 1, 1, 1));
            //---- backButton ----
            backButton.setToolTipText("Undo");
            backButton.addActionListener(this::editActionPerformedEdit);
            toolBar.add(backButton);

            //---- forwardButton ----
            forwardButton.setToolTipText("Redo");
            forwardButton.addActionListener(this::editActionPerformedEdit);
            toolBar.add(forwardButton);
            toolBar.addSeparator();

            //---- cutButton ----
            cutButton.setToolTipText("Cut");
            cutButton.addActionListener(this::editActionPerformedEdit);
            toolBar.add(cutButton);

            //---- copyButton ----
            copyButton.setToolTipText("Copy");
            copyButton.addActionListener(this::editActionPerformedEdit);
            toolBar.add(copyButton);

            //---- pasteButton ----
            pasteButton.setToolTipText("Paste");
            pasteButton.addActionListener(this::editActionPerformedEdit);
            toolBar.add(pasteButton);
            toolBar.addSeparator();

            //---- searchButton ----
            searchButton.setToolTipText("Search");
            toolBar.add(searchButton);
            toolBar.addSeparator();

            //---- refreshButton ----
            refreshButton.setToolTipText("Refresh");
            toolBar.add(refreshButton);

            //---- addButton ----
            addButton.setToolTipText("New File (Ctrl+N)");
            addButton.addActionListener(this::actionPerformed);
            toolBar2.add(addButton);

            //---- sidebarButton ----
            sidebarButton.setToolTipText("Sidebar");
            sidebarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JToggleButton toggleBtn = (JToggleButton) e.getSource();
                    if (toggleBtn.isSelected()){
                        remove(bodyPanel);
                        TextAreaJSplitPane.setLeftComponent(workSpacePanel);
                        TextAreaJSplitPane.setRightComponent(bodyPanel);
                        add(TextAreaJSplitPane, BorderLayout.CENTER);
                        updateUI();
                        repaint();
                    }else{
                        remove(TextAreaJSplitPane);
                        add(bodyPanel, BorderLayout.CENTER);
                        repaint();
                    }
                }
            });
            toolBar2.add(sidebarButton);

            backButton.setIcon( new FlatSVGIcon( "images/undo.svg" ) );
            forwardButton.setIcon( new FlatSVGIcon( "images/redo.svg" ) );
            cutButton.setIcon( new FlatSVGIcon( "images/menu-cut.svg" ) );
            copyButton.setIcon( new FlatSVGIcon( "images/copy.svg" ) );
            pasteButton.setIcon( new FlatSVGIcon( "images/menu-paste.svg" ) );
            searchButton.setIcon( new FlatSVGIcon( "images/search2.svg" ) );
            refreshButton.setIcon( new FlatSVGIcon( "images/refresh.svg" ) );
            addButton.setIcon( new FlatSVGIcon( "images/add.svg" ) );
            sidebarButton.setIcon( new FlatSVGIcon( "images/sidebar.svg" ) );
        }
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();

        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel1.add(toolBar);
        jPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanel2.add(toolBar2);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(toolBar, BorderLayout.WEST);
        contentPanel.add(toolBar2, BorderLayout.EAST);
    }


    //---- ActionListener -----------------
    public void actionPerformed(ActionEvent e){

        //---- File MenuItem ActionListener -----------------
        {
            if(e.getSource()==newMenuItem){
                bodyPanel.addTab("Untitled");
            }

            if(e.getSource()==addButton){
                bodyPanel.addTab("Untitled");
            }

            if(e.getSource()==openFolderMenuItem){
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int n = fileChooser.showOpenDialog(this.getContentPane());
                    if (n == JFileChooser.APPROVE_OPTION)
                    {
                        workSpacePath = fileChooser.getSelectedFile().getPath();
                        System.out.println("here is directories" + workSpacePath);
                        workSpacePanel.initJTree(workSpacePath);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if(e.getSource()==saveAsMenuItem){
                JOptionPane.showMessageDialog(null,"非内部类事件监听","注意", JOptionPane.ERROR_MESSAGE,null);
            }

            if(e.getSource()==closeMenuItem){
                JOptionPane.showMessageDialog(null,"非内部类事件监听","注意", JOptionPane.ERROR_MESSAGE,null);
            }

            if(e.getSource()==exitMenuItem){
                JOptionPane.showMessageDialog(null,"非内部类事件监听","注意", JOptionPane.ERROR_MESSAGE,null);
            }
        }

    }

    public void editActionPerformedEdit(ActionEvent e){

        //---- File MenuItem ActionListener -----------------
        {
            int index = this.bodyPanel.closableTabsLabel.getSelectedIndex();
            Tab tab = this.bodyPanel.getTabsTextArea(index);

            if (tab != null){
                // ==== editMenu =============
                if(e.getSource()==undoMenuItem){
                    tab.Undo();
                }
                if(e.getSource()==redoMenuItem){
                    tab.Redo();
                }
                if(e.getSource()==cutMenuItem){
                    tab.Cut();
                }
                if(e.getSource()==copyMenuItem){
                    tab.Copy();
                }
                if(e.getSource()==pasteMenuItem){
                    tab.Paste();
                }
                if(e.getSource()==selectAllMenuItem){
                    tab.SelectAll();
                }

                // ==== toolbar =============
                if(e.getSource()==backButton){
                    tab.Undo();
                }
                if(e.getSource()==forwardButton){
                    tab.Redo();
                }
                if(e.getSource()==cutButton){
                    tab.Cut();
                }
                if(e.getSource()==copyButton){
                    tab.Copy();
                }
                if(e.getSource()==pasteButton){
                    tab.Paste();
                }
            }

        }
    }

    private void openActionPerformed() {
//        JFileChooser chooser = new JFileChooser();
//        chooser.showOpenDialog( this );
        openFile();
    }

    //==== open file ========
    private void openFile() {
        JFileChooser chooser = new JFileChooser();
//        chooser.showOpenDialog( this );
        JTextArea ta = new JTextArea();
        try {
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.CANCEL_OPTION)
                return;
            if (result == JFileChooser.APPROVE_OPTION) {
                String filename;

                File file = chooser.getSelectedFile();
                recentFile = file.getAbsolutePath();
                filename = file.getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                FileReader reader;
                BufferedReader in;
                try{
                    reader=new FileReader(file);
                    in=new BufferedReader(reader);
                    String info=in.readLine();
                    while(info!=null){
                        ta.append(info+"\n");
                        info=in.readLine();
                    }
                    in.close();
                    reader.close();

                    bodyPanel.addTab(filename, recentFile, ta);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void searchEvent(SearchEvent searchEvent) {

    }

    @Override
    public String getSelectedText() {
        return null;
    }

    private static JMenuItem createMenuItem(Action action, String tip) {
        JMenuItem item = new JMenuItem(action);
        item.setToolTipText(tip); // Swing annoyingly adds tool tip text to the menu item
        return item;
    }
}
