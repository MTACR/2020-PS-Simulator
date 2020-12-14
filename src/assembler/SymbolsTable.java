package assembler;

import javafx.util.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SymbolsTable {
    public List<Symbol> symbols;
    public Map<String, Pair<Integer, Character>> labels;
    public String name;

    public SymbolsTable(List<Symbol> symbols, Map<String, Pair<Integer, Character>> labels, String name) {
        this.symbols = symbols;
        this.labels = labels;
        this.name = name;
    }

    public static final List<String> table = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "COPY", "DIVIDE", "LOAD", "MULT", "READ", "RET", "STOP",
            "STORE", "SUB", "WRITE", "CONST", "END", "EXTDEF", "EXTR", "SPACE", "STACK", "START");

    public static final List<String> table0 = Arrays.asList(
            "RET", "STOP", "END", "EXTR", "SPACE");//, "MACRO");

    public static final List<String> table1 = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "DIVIDE", "LOAD", "MULT", "READ",
            "STORE", "SUB", "WRITE", "CONST", "EXTDEF", "STACK", "START");

    public static final List<String> table2 = Arrays.asList(
            "COPY");

}
