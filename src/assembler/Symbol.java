package assembler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Symbol {
    public int line;
    public int address;
    public String label;
    public String operator;
    public String opd1;
    public String opd2;

    public Symbol(int line, int address, String label, String operator, String opd1, String opd2) {
        this.line = line;
        this.address = address;
        this.label = label;
        this.operator = operator;
        this.opd1 = opd1;
        this.opd2 = opd2;

        if (!isLabelValid(label))
            throw new RuntimeException(" Erro de sintaxe em " + label);

        if (!isOpdValid(opd1))
            throw new RuntimeException(" Erro de sintaxe em " + opd1);

        if (!isOpdValid(opd2))
            throw new RuntimeException(" Erro de sintaxe em " + opd2);
    }

    private static boolean isLabelValid(String s) {
        if (s.isEmpty())
            return true;

        if (startsWithNumber(s))
            return false;

        return !Pattern.compile("[^A-Za-z0-9]").matcher(s).find();
    }

    private static boolean isOpdValid(String s) {
        if (s.isEmpty())
            return true;

        if (startsWithNumber(s))
            return !Pattern.compile("[^0-9]").matcher(s).find();

        if (s.startsWith("#"))
            if (startsWithNumber(s.substring(1)))
                return !Pattern.compile("[^0-9]").matcher(s.substring(1)).find();

        return !Pattern.compile("[^A-Za-z0-9]").matcher(s).find();
    }

    private static boolean startsWithNumber(String s) {
        if (s.isEmpty())
            return false;

        try {
            Double.parseDouble(String.valueOf(s.charAt(0)));
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

}
