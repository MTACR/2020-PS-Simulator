package gui;

import assembler.SymbolsTable;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDocumentFilter extends DocumentFilter {

    public CustomDocumentFilter(JTextPane pane) {
        this.pane = pane;
        this.styledDocument = pane.getStyledDocument();

        handleTextChanged();
    }

    private final JTextPane pane;
    private final StyledDocument styledDocument;
    private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private final AttributeSet reservedAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(22, 84, 248));
    private final AttributeSet commonAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    private final AttributeSet commentsAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(27, 175, 89));
    private final AttributeSet symbolsAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(243, 153, 22));
    private final AttributeSet numbersAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(210, 30, 80));

    Pattern reserved = buildReservedPattern();
    Pattern comments = buildCommentsPattern();
    Pattern symbols = buildSymbolsPattern();
    Pattern numbers = buildNumbersPattern();

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributeSet) throws BadLocationException {
        super.insertString(fb, offset, text, attributeSet);
        handleTextChanged();
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        handleTextChanged();
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributeSet) throws BadLocationException {
        super.replace(fb, offset, length, text, attributeSet);
        handleTextChanged();
    }

    private void handleTextChanged() {
        SwingUtilities.invokeLater(this::updateTextStyles);
    }

    private Pattern buildReservedPattern() {
        StringBuilder sb = new StringBuilder();

        for (String token : SymbolsTable.tablea) {
            sb.append("\\b"); // Start of word boundary
            sb.append(token);
            sb.append("\\b|"); // End of word boundary and an or for the next word
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        }

        return Pattern.compile(sb.toString());
    }

    private Pattern buildCommentsPattern() {
        return Pattern.compile("\\*.*");
    }

    private Pattern buildSymbolsPattern() {
        return Pattern.compile("&([^,\\s]*)");
    }

    private Pattern buildNumbersPattern() {
        return Pattern.compile("[0-9]+");
    }

    private void updateTextStyles() {
        styledDocument.setCharacterAttributes(0, pane.getText().length(), commonAttrSet, true);

        Matcher matcher;

        matcher = reserved.matcher(pane.getText());
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), reservedAttrSet, false);

        matcher = symbols.matcher(pane.getText());
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), symbolsAttrSet, false);

        matcher = comments.matcher(pane.getText());
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentsAttrSet, false);

        matcher = numbers.matcher(pane.getText());
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), numbersAttrSet, false);
    }

}
