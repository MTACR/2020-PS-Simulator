package assembler;

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

        if (checkLabel(label))
            throw new RuntimeException(" Erro de sintaxe em " + label);

        if (checkOpd(opd1))
            throw new RuntimeException(" Erro de sintaxe em " + opd1);

        if (checkOpd(opd2))
            throw new RuntimeException(" Erro de sintaxe em " + opd2);
    }

    private static boolean checkLabel(String s) {
        if (s.isEmpty())
            return false;

        try {
            Double.parseDouble(String.valueOf(s.charAt(0)));
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    private static boolean checkOpd(String s) {
        if (s.isEmpty())
            return false;

        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return checkLabel(s);
        }

        return true;
    }

}
