package assembler;

import simulator.Interface;

import java.util.regex.Pattern;

public class Symbol {
    public final int line;
    public final String label;
    public final String operator;

    public int address;
    public String opd1;
    public String opd2;

    public boolean ext1;
    public boolean ext2;

    public Symbol(int line, int address, String label, String operator, String opd1, String opd2) {
        if (!isLabelValid(label))
            throw new RuntimeException(" Erro de sintaxe em " + label);

        if (!isOpdValid(opd1))
            throw new RuntimeException(" Erro de sintaxe em " + opd1);

        if (!isOpdValid(opd2))
            throw new RuntimeException(" Erro de sintaxe em " + opd2);

        this.line = line;
        this.address = address;
        this.label = label;
        this.operator = operator;
        this.opd1 = opd1;
        this.opd2 = opd2;
    }

    private static boolean isLabelValid(String s) {
        if (s.isEmpty())
            return true;

        if (startsWithNumber(s))
            return false;

        return !Pattern.compile("[^A-Za-z0-9-_]").matcher(s).find();
    }

    private static boolean isOpdValid(String s) {
        if (s.isEmpty())
            return true;

        if (s.startsWith("#")) {
            if (startsWithNumber(s.substring(1)))
                return !Pattern.compile("[^0-9]").matcher(s.substring(1)).find();
        }

        if (s.endsWith(",I")) {
            Interface.instance().printMessage(s.substring(0, s.indexOf(",I")));
            return !Pattern.compile("[^0-9]").matcher(s.substring(0, s.indexOf(",I"))).find();
        }

        if (startsWithNumber(s))
            return !Pattern.compile("[^0-9]").matcher(s).find();

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
