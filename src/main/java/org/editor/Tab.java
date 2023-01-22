package org.editor;

import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

    public void setRSyntaxTextAreaOutlook(RSyntaxTextArea rSyntaxTextArea){
        this.rSyntaxTextArea = rSyntaxTextArea;
    }

    public void setRSyntaxTextAreaContent(JTextArea textArea) {
        if (textArea == null) return;
        this.rSyntaxTextArea.setText(textArea.getText());
    }

    public JPanel initRSyntaxTextArea(RSyntaxTextArea rSyntaxTextArea, JTextArea textArea) {
        setRSyntaxTextAreaOutlook(rSyntaxTextArea);
        setRSyntaxTextAreaContent(textArea);

        if (syntax.isEmpty() || fileExtension == null){
            this.rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }else this.rSyntaxTextArea.setSyntaxEditingStyle(syntax.getOrDefault(fileExtension,
                SyntaxConstants.SYNTAX_STYLE_NONE));

        this.rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        LanguageSupportFactory.get().register(rSyntaxTextArea);
        this.rSyntaxTextArea.setCodeFoldingEnabled(true);
        this.rSyntaxTextArea.setMarkOccurrences(true);

        rSyntaxTextArea.addCaretListener(e -> {
            try {
                int pos = rSyntaxTextArea.getCaretPosition();
                int lineOfC = rSyntaxTextArea.getLineOfOffset(pos) + 1;
                int col = pos - rSyntaxTextArea.getLineStartOffset(lineOfC - 1) + 1;

                controlPanel.setLabelText( lineOfC, col);
                System.out.println("Line: " + lineOfC + ", Column: " + col);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.setLabel2Text(fileExtension);

        add(rSyntaxTextArea, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        return this;
    }

    static Map<String, String> syntax = new HashMap<>();
    String tabName;
    String filePath;
    String fileExtension;
    RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea(32, 80);

    ControlPanel controlPanel = new ControlPanel();

    Tab (String tabName) {
        this(tabName, null, null);
    }

    Tab(String tabName, String filePath, String fileExtension) {
        setLayout(new BorderLayout());

        this.tabName = tabName;
        this.filePath = filePath;
        this.fileExtension = fileExtension;

        if (fileExtension != null) {
            initHashMap();
        }
    }

    static void initHashMap() {
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
        syntax.put("c++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        syntax.put(".h", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        syntax.put("c#", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        syntax.put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        syntax.put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        syntax.put("json", SyntaxConstants.SYNTAX_STYLE_JSON);
        syntax.put("php", SyntaxConstants.SYNTAX_STYLE_PHP);
        syntax.put("rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
        syntax.put("bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        syntax.put("sal", SyntaxConstants.SYNTAX_STYLE_SQL);

        //==== Objective-C ================
        syntax.put(".m", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        syntax.put(".mm", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
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

}
