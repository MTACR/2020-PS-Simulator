package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
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
    private final static StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private final static AttributeSet opcodeAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(22, 84, 248));
    private final static AttributeSet labels1AttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(246, 80, 170));
    private final static AttributeSet labels2AttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(111, 22, 158));
    private final static AttributeSet commonAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    private final static AttributeSet commentsAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(27, 175, 89));
    private final static AttributeSet symbolsAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(243, 153, 22));
    //private final AttributeSet numbersAttrSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(210, 30, 80));

    private final Pattern opcode = buildOPPattern();
    private final Pattern label1 = buildRTPattern();
    private final Pattern label2 = buildLBPattern();
    private final Pattern comments = buildCommentsPattern();
    private final Pattern symbols = buildSymbolsPattern();
    //Pattern numbers = buildNumbersPattern();

    private static final List<String> tableOP = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "COPY", "DIVIDE", "LOAD", "MULT", "READ", "RET", "STOP",
            "STORE", "SUB", "WRITE");

    private static final List<String> tableRT = Arrays.asList(
           "CONST", "EXTDEF", "EXTR", "SPACE", "STACK");

    private static final List<String> tableLB = Arrays.asList(
            "END", "START", "MACRO", "MEND");

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

    private Pattern buildOPPattern() {
        StringBuilder sb = new StringBuilder();

        for (String token : tableOP) {
            sb.append("\\b"); // Start of word boundary
            sb.append(token);
            sb.append("\\b|"); // End of word boundary and an or for the next word
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        }

        return Pattern.compile(sb.toString());
    }

    private static Pattern buildRTPattern() {
        StringBuilder sb = new StringBuilder();

        for (String token : tableRT) {
            sb.append("\\b"); // Start of word boundary
            sb.append(token);
            sb.append("\\b|"); // End of word boundary and an or for the next word
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        }

        return Pattern.compile(sb.toString());
    }

    private static Pattern buildLBPattern() {
        StringBuilder sb = new StringBuilder();

        for (String token : tableLB) {
            sb.append("\\b"); // Start of word boundary
            sb.append(token);
            sb.append("\\b|"); // End of word boundary and an or for the next word
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        }

        return Pattern.compile(sb.toString());
    }

    private static Pattern buildCommentsPattern() {
        return Pattern.compile("\\*.*");
    }

    private static Pattern buildSymbolsPattern() {
        return Pattern.compile("&([^,\\s]*)");
    }

    private static Pattern buildNumbersPattern() {
        return Pattern.compile("[0-9]+");
    }

    private void updateTextStyles() {

        styledDocument.setCharacterAttributes(0, pane.getText().length(), commonAttrSet, true);

        Matcher matcher;
        String text = pane.getText().replace("\n", "").toUpperCase();

        matcher = opcode.matcher(text);
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), opcodeAttrSet, false);

        matcher = label1.matcher(text);
        while (matcher.find()) {
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), labels1AttrSet, false);
        }

        matcher = label2.matcher(text);
        while (matcher.find()) {
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), labels2AttrSet, false);
        }

        matcher = symbols.matcher(text);
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), symbolsAttrSet, false);

        matcher = comments.matcher(text);
        while (matcher.find())
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentsAttrSet, false);

        //matcher = numbers.matcher(pane.getText());
        //while (matcher.find())
            //styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), numbersAttrSet, false);
    }

}
