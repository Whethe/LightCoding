package org.editor;

import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.rsta.ui.*;
import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tab extends JPanel {
    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public RSyntaxTextArea getRSyntaxTextArea() {
        return rSyntaxTextArea;
    }

    public void setRSyntaxTextAreaOutlook(TextEditorPane rSyntaxTextArea){
        this.rSyntaxTextArea = rSyntaxTextArea;
    }

    public void setRSyntaxTextAreaContent(JTextArea textArea){
        if (textArea == null) return;
        this.rSyntaxTextArea.setText(textArea.getText());
    }

    /***/
    public Tab initRSyntaxTextArea(TextEditorPane rSyntaxTextArea, JTextArea textArea){
        setRSyntaxTextAreaOutlook(rSyntaxTextArea);
//        setRSyntaxTextAreaContent(textArea);
        this.rSyntaxTextArea.setEncoding("UTF-8");
        setRSyntaxTextAreaContent(textArea);

        this.rSyntaxTextArea.setPaintTabLines(true);
        this.rSyntaxTextArea.setMarginLineEnabled(true);
        this.rSyntaxTextArea.setMarginLinePosition(100);

        if (syntax.isEmpty() || fileExtension == null){
            this.rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }else this.rSyntaxTextArea.setSyntaxEditingStyle(syntax.getOrDefault(fileExtension,
                SyntaxConstants.SYNTAX_STYLE_NONE));

        this.rSyntaxTextArea.setCodeFoldingEnabled(true);
        this.rSyntaxTextArea.setMarkOccurrences(true);

        ErrorStrip errorStrip = new ErrorStrip(rSyntaxTextArea);
        add(errorStrip, BorderLayout.LINE_END);
        LanguageSupportFactory.get().register(rSyntaxTextArea);

        this.rSyntaxTextArea.addCaretListener(e -> {
            try {
                int pos = this.rSyntaxTextArea.getCaretPosition();
                int lineOfC = this.rSyntaxTextArea.getLineOfOffset(pos) + 1;
                int col = pos - this.rSyntaxTextArea.getLineStartOffset(lineOfC - 1) + 1;

                controlPanel.setLabelText( lineOfC, col);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        this.rSyntaxTextArea.getDocument().addUndoableEditListener(und);

        RTextScrollPane sp = new RTextScrollPane(this.rSyntaxTextArea);
        //Enable bookmarking
        Gutter gutter = sp.getGutter();
        gutter.setBookmarkingEnabled(true);
        URL url = getClass().getResource("/images/bookmark.png");
        assert url != null;
        gutter.setBookmarkIcon(new ImageIcon(url));

        addListener();
        add(sp, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        controlPanel.changeSyntaxComboBox.setSelectedItem(textType.getOrDefault(fileExtension, "Plain Text"));
        return this;
    }

    static Map<String, String> syntax = new HashMap<>();
    static Map<String, String> textType = new HashMap<>();
    static Map<String, String> extentToSelectBox = new HashMap<>();
    String tabName;
    String filePath;
    String fileExtension;
    private UndoManager und;
//    RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea(32, 80);
    TextEditorPane rSyntaxTextArea = new TextEditorPane();
    ControlPanel controlPanel = new ControlPanel();

    Tab (String tabName) {
        this(tabName, null, null);
    }

    Tab(String tabName, String filePath, String fileExtension) {
        setLayout(new BorderLayout());
        und = new UndoManager();

        this.tabName = tabName;
        this.filePath = filePath;
        if (fileExtension != null){
            this.fileExtension = fileExtension.toLowerCase();
        }else{
            this.fileExtension = null;
        }


        initHashMap();
    }

    static void initHashMap() {
        //==== syntax ==================
        {
            syntax.put("txt", SyntaxConstants.SYNTAX_STYLE_NONE);
            syntax.put("tex", SyntaxConstants.SYNTAX_STYLE_LATEX);
            syntax.put("html", SyntaxConstants.SYNTAX_STYLE_HTML);
            syntax.put("css", SyntaxConstants.SYNTAX_STYLE_CSS);
            syntax.put("csv", SyntaxConstants.SYNTAX_STYLE_CSV);
            syntax.put("xml", SyntaxConstants.SYNTAX_STYLE_XML);
            syntax.put("xsl", SyntaxConstants.SYNTAX_STYLE_XML);
            syntax.put("yml", SyntaxConstants.SYNTAX_STYLE_YAML);
            syntax.put("yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
            syntax.put("md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
            syntax.put("js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            syntax.put("c", SyntaxConstants.SYNTAX_STYLE_C);
            syntax.put("cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            syntax.put("h", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            syntax.put("c#", SyntaxConstants.SYNTAX_STYLE_CSHARP);
            syntax.put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);
            syntax.put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
            syntax.put("json", SyntaxConstants.SYNTAX_STYLE_JSON);
            syntax.put("php", SyntaxConstants.SYNTAX_STYLE_PHP);
            syntax.put("rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
            syntax.put("bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
            syntax.put("sal", SyntaxConstants.SYNTAX_STYLE_SQL);

            //==== Objective-C ================
            syntax.put("m", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            syntax.put("mm", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        }

        //==== text ==================
        {
            textType.put("as", "ActionScript");
            textType.put("asm", "Assembler X86");
            textType.put("c", "C");
            textType.put("h", "C");
            textType.put("cs", "C#");
            textType.put("cpp", "C++");
            textType.put("clj", "Clojure");
            textType.put("css", "CSS");
            textType.put("csv", "CSV");
            textType.put("go", "GO");
            textType.put("groovy", "Groovy");
            textType.put("html", "HTML");
            textType.put("java", "Java");
            textType.put("js", "Java Script");
            textType.put("json", "JSON");
            textType.put("lua", "Lua");
            textType.put("tex", "Latex");
            textType.put("md", "Markdown");
            textType.put("mm", "Objective-C");
            textType.put("m", "Objective-C");
            textType.put("mxml", "MXML");
            textType.put("pl", "Perl");
            textType.put("php", "PHP");
            textType.put("py", "Python");
            textType.put("txt", "Plain Text");
            textType.put("rb", "Ruby");
            textType.put("sql", "SQL");
            textType.put("xml", "XML");
            textType.put("xsl", "XSL");
            textType.put("yml", "Yaml");
            textType.put("yaml", "Yaml");
        }
        //==== text ==================
        {
            extentToSelectBox.put("ActionScript", SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
            extentToSelectBox.put("Assembler X86", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
            extentToSelectBox.put("Assembler 6502", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
            extentToSelectBox.put("C", SyntaxConstants.SYNTAX_STYLE_C);
            extentToSelectBox.put("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP);
            extentToSelectBox.put("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            extentToSelectBox.put("Clojure", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
            extentToSelectBox.put("CSS", SyntaxConstants.SYNTAX_STYLE_CSS);
            extentToSelectBox.put("CSV", SyntaxConstants.SYNTAX_STYLE_CSV);
            extentToSelectBox.put("GO", SyntaxConstants.SYNTAX_STYLE_GO);
            extentToSelectBox.put("Groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
            extentToSelectBox.put("HTML", SyntaxConstants.SYNTAX_STYLE_HTML);
            extentToSelectBox.put("Java", SyntaxConstants.SYNTAX_STYLE_JAVA);
            extentToSelectBox.put("Java Script", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            extentToSelectBox.put("JSON", SyntaxConstants.SYNTAX_STYLE_JSON);
            extentToSelectBox.put("Lua", SyntaxConstants.SYNTAX_STYLE_LUA);
            extentToSelectBox.put("Latex", SyntaxConstants.SYNTAX_STYLE_LATEX);
            extentToSelectBox.put("Markdown", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
            extentToSelectBox.put("Objective-C", SyntaxConstants.SYNTAX_STYLE_C);
            extentToSelectBox.put("Makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
            extentToSelectBox.put("MXML", SyntaxConstants.SYNTAX_STYLE_MXML);
            extentToSelectBox.put("Perl", SyntaxConstants.SYNTAX_STYLE_PERL);
            extentToSelectBox.put("PHP", SyntaxConstants.SYNTAX_STYLE_PHP);
            extentToSelectBox.put("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON);
            extentToSelectBox.put("Plain Text", SyntaxConstants.SYNTAX_STYLE_NONE);
            extentToSelectBox.put("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
            extentToSelectBox.put("SQL", SyntaxConstants.SYNTAX_STYLE_SQL);
            extentToSelectBox.put("XML", SyntaxConstants.SYNTAX_STYLE_XML);
            extentToSelectBox.put("XSL", SyntaxConstants.SYNTAX_STYLE_XML);
            extentToSelectBox.put("Yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
        }
    }

    private CompletionProvider createCompletionProvider() {

        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider. This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        // Add completions for all Java keywords. A BasicCompletion is just
        // a straightforward word completion.
        provider.addCompletion(new BasicCompletion(provider, "abstract"));
        provider.addCompletion(new BasicCompletion(provider, "assert"));
        provider.addCompletion(new BasicCompletion(provider, "break"));
        provider.addCompletion(new BasicCompletion(provider, "case"));
        // ... etc ...
        provider.addCompletion(new BasicCompletion(provider, "transient"));
        provider.addCompletion(new BasicCompletion(provider, "try"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "volatile"));
        provider.addCompletion(new BasicCompletion(provider, "while"));

        // Add a couple of "shorthand" completions. These completions don't
        // require the input text to be the same thing as the replacement text.
        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
                "System.out.println(", "System.out.println("));
        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
                "System.err.println(", "System.err.println("));

        return provider;
    }

    public void addListener() {
        controlPanel.changeSyntaxComboBox.addItemListener(e -> {
            rSyntaxTextArea.setSyntaxEditingStyle(extentToSelectBox.get((String) Objects.requireNonNull(controlPanel.changeSyntaxComboBox.getSelectedItem())));
        });
    }

    public void Undo() {
        if (und.canUndo()) und.undo();
    }
    public void Redo() {
        if (und.canRedo()) und.redo();
    }

    public void Cut() {
        rSyntaxTextArea.cut();
    }

    public void Copy() {
        rSyntaxTextArea.copy();
    }

    public void SelectAll() {
        rSyntaxTextArea.selectAll();
    }

    public void Paste() {
        rSyntaxTextArea.paste();
    }
}
