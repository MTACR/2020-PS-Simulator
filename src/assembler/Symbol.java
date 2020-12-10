package assembler;

import java.util.regex.Pattern;

public class Symbol {
    public final int line;
    public final String label;
    public final String operator;
    public final Character flag1;
    public final int offset1;
    public final Character flag2;
    public final int offset2;

    public int address;
    public String opd1;
    public String opd2;
    public ObjectCode objectCode;

    public Symbol(int line, int address, String label, String operator, String opd1, String opd2) {

        if (opd1.contains("+")) {
            flag1 = '+';
            offset1 = Integer.parseInt(opd1.substring(opd1.indexOf("+") + 1));
            opd1 = opd1.substring(0, opd1.indexOf("+"));

        } else if (opd1.contains("-")) {
            flag1 = '-';
            offset1 = Integer.parseInt(opd1.substring(opd1.indexOf("-") + 1));
            opd1 = opd1.substring(0, opd1.indexOf("-"));

        } else {
            flag1 = '+';
            offset1 = 0;
        }

        if (opd2.contains("+")) {
            flag2 = '+';
            offset2 = Integer.parseInt(opd2.substring(opd2.indexOf("+") + 1));
            opd2 = opd2.substring(0, opd2.indexOf("+"));

        } else if (opd2.contains("-")) {
            flag2 = '-';
            offset2 = Integer.parseInt(opd2.substring(opd2.indexOf("-") + 1));
            opd2 = opd2.substring(0, opd2.indexOf("-"));

        } else {
            flag2 = '+';
            offset2 = 0;
        }

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

    public String printMachineCode() {
        String s = operator;

        if (!opd1.isEmpty())
            s += " " + opd1;

        if (!opd2.isEmpty())
            s += " " + opd2;

        return s;
    }

    //TODO na verdade as vars usam SUfixo
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
