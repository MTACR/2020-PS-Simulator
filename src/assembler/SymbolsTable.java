package assembler;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SymbolsTable {
    public List<Symbol> symbols;
    public Map<String, Pair<Integer, Character>> labels;

    public SymbolsTable(List<Symbol> symbols, Map<String, Pair<Integer, Character>> labels) {
        this.symbols = symbols;
        this.labels = labels;
    }

    public static final List<String> table0 = Arrays.asList(
            "RET", "STOP", "END", "EXTR", "SPACE", "MACRO");

    public static final List<String> table1 = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "DIVIDE", "LOAD", "MULT", "READ",
            "STORE", "SUB", "WRITE", "CONST", "EXTDEF", "INIT", "STACK", "START");

    public static final List<String> table2 = Arrays.asList(
            "COPY", "PROC");

}
