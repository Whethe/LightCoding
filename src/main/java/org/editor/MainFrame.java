package org.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainFrame extends JFrame {
    private volatile static MainFrame mainFrame;

    private String recentFile;

    BodyFrame bodyFrame;

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
        bodyFrame = new BodyFrame(config.getTextAreaTheme(), config.getFontStyle(), config.getFontSize());
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

        setVisible(true);

        add(contentPanel, BorderLayout.NORTH);
        add(bodyFrame.TextPanel(), BorderLayout.CENTER);
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
    JMenuItem saveAsMenuItem = new JMenuItem();
    JMenuItem closeMenuItem = new JMenuItem();
    JMenuItem exitMenuItem = new JMenuItem();

    JMenu editMenu = new JMenu();
    JMenuItem undoMenuItem = new JMenuItem();
    JMenuItem redoMenuItem = new JMenuItem();
    JMenuItem cutMenuItem = new JMenuItem();
    JMenuItem copyMenuItem = new JMenuItem();
    JMenuItem pasteMenuItem = new JMenuItem();
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
    JButton sidebarButton = new JButton();

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
//            undoMenuItem.addActionListener(e -> menuItemActionPerformed(e));
            editMenu.add(undoMenuItem);

            //---- redoMenuItem ----
            redoMenuItem.setText("Redo");
            redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            redoMenuItem.setMnemonic('R');
//            redoMenuItem.addActionListener(e -> menuItemActionPerformed(e));
            editMenu.add(redoMenuItem);
            editMenu.addSeparator();

            //---- cutMenuItem ----
            cutMenuItem.setText("Cut");
            cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            cutMenuItem.setMnemonic('C');
            editMenu.add(cutMenuItem);

            //---- copyMenuItem ----
            copyMenuItem.setText("Copy");
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            copyMenuItem.setMnemonic('O');
            editMenu.add(copyMenuItem);

            //---- pasteMenuItem ----
            pasteMenuItem.setText("Paste");
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            pasteMenuItem.setMnemonic('P');
            editMenu.add(pasteMenuItem);
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
            findMenu.setText("Find");
            findMenu.setMnemonic('F');

            //---- findMenuItem ----
            findMenuItem.setText("Find");
            findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            findMenu.add(findMenuItem);

            //---- findNextMenuItem ----
            findNextMenuItem.setText("Find Next");
            findMenu.add(findNextMenuItem);

            //---- findPreviousMenuItem ----
            findPreviousMenuItem.setText("Find Previous");
            findMenu.add(findPreviousMenuItem);
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
            toolBar.add(backButton);

            //---- forwardButton ----
            forwardButton.setToolTipText("Redo");
            toolBar.add(forwardButton);
            toolBar.addSeparator();

            //---- cutButton ----
            cutButton.setToolTipText("Cut");
            toolBar.add(cutButton);

            //---- copyButton ----
            copyButton.setToolTipText("Copy");
            toolBar.add(copyButton);

            //---- pasteButton ----
            pasteButton.setToolTipText("Paste");
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
    public void actionPerformed(ActionEvent e) {

        //---- File MenuItem ActionListener -----------------
        {
            if(e.getSource()==newMenuItem){
                bodyFrame.addTab("Untitled");
            }

            if(e.getSource()==addButton){
                bodyFrame.addTab("Untitled");
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

                    bodyFrame.addTab(filename, recentFile, ta);
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
}
