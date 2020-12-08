package assembler;

import javafx.util.Pair;

import java.io.*;
import java.util.*;
import static assembler.SymbolsTable.*;

public class FirstPass {

    /*private static final List<String> table = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "COPY", "DIVIDE", "LOAD", "MULT", "READ", "RET", "STOP",
            "STORE", "SUB", "WRITE", "CONST", "END", "EXTDEF", "EXTR", "INIT", "PROC", "SPACE", "STACK", "START");*/

    public static SymbolsTable getSymbolsTable(File file) {
        List<Symbol> symbols = new ArrayList<>();
        Map<String, Pair<Integer, Character>> labels = new TreeMap<>();
        List<String> extrs = new ArrayList<>();

        int address = 0;
        int line = 1;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;

            while ((string = reader.readLine()) != null) {
                string = string.toUpperCase().trim();

                //TODO receber arquivo sem , do processador de macros
                //string = string.replace(",", "");

                if (string.length() > 80)
                    throw new RuntimeException("Linha muito longa em" + line);

                //ignora comentários
                if (string.contains("*"))
                    string = string.substring(0, string.indexOf("*"));

                //ignora linhas em branco
                if (string.isEmpty())
                    continue;

                //divide linha quando acha um espaço
                String[] lineArr = string.split(" ");
                for (int i = 0; i < lineArr.length; i++)
                    lineArr[i] = lineArr[i].trim();

                if (lineArr.length == 1) {

                    if (table0.contains(lineArr[0])) {
                        if (lineArr[0].equals("EXTR"))
                            throw new RuntimeException("Instrução necessita label em " + line);

                        symbols.add(new Symbol(line, address, "", lineArr[0], "", ""));
                        address += 1;

                    } else throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 2) {

                    if (table0.contains(lineArr[1])) {

                        if (lineArr[1].equals("EXTR")) {
                            if (!extrs.contains(lineArr[0].toUpperCase()))
                                extrs.add(lineArr[0].toUpperCase());

                            else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                        } else {
                            symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));
                        }

                        if (!labels.containsKey(lineArr[0])) {
                            if (!lineArr[1].equals("EXTR"))
                                labels.put(lineArr[0], new Pair<>(address, 'r'));

                        } else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                        if (!lineArr[1].equals("EXTR"))
                            address += 1;

                    }

                    else if (table1.contains(lineArr[0])) {
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], ""));
                        address += 2;
                    }

                    else throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 3) {

                    if (table1.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], ""));

                        if (!labels.containsKey(lineArr[0]))
                            labels.put(lineArr[0], new Pair<>(address, 'r'));

                        else throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);

                        if (lineArr[1].equals("CONST"))
                            address += 1;
                        else
                            address += 2;
                    }

                    else if (table2.contains(lineArr[0])) {
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], lineArr[2]));
                        address += 3;
                    }

                    else throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 4) {

                    if (table2.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], lineArr[3]));

                        if (!labels.containsKey(lineArr[0]))
                            labels.put(lineArr[0], new Pair<>(address, 'r'));

                        else throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);

                        address += 3;
                    }

                    else throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length > 4)
                    throw new RuntimeException("Erro de sintaxe em " + line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Symbol> labels2Alloc = new ArrayList<>();

        // Aloca endereço para variáveis
        for (Symbol symbol : symbols) {
            if (!symbol.label.isEmpty()) {
                switch (symbol.operator) {

                    case "SPACE":
                        symbol.opd1 = String.valueOf(address++);

                        break;

                    case "CONST":
                        symbol.opd2 = symbol.opd1;
                        symbol.opd1 = String.valueOf(address++);

                        break;

                    default:
                        Symbol s = new Symbol(line++, address, symbol.label, "LABEL", String.valueOf(labels.get(symbol.label).getKey()), "");

                        if (extrs.contains(symbol.label.toUpperCase())) {
                            labels.replace(symbol.label, new Pair<>(address, '+'));
                            s.operator = "EXTR";
                        } else
                            labels.replace(symbol.label, new Pair<>(address, 'r'));

                        labels2Alloc.add(s);

                        address++;

                        break;
                }
            }
        }

        symbols.addAll(labels2Alloc);

        File lst = new File("output/MASMAPRG.lst");

        try {
            FileWriter out = new FileWriter(lst);
            String string = "";

            for (Map.Entry<String, Pair<Integer, Character>> entry : labels.entrySet()) {
                String label = entry.getKey();
                Pair<Integer, Character> addr = entry.getValue();

                string += label + " " + addr.getKey() + " " + addr.getValue() + "\n";
            }

            out.write(string);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SymbolsTable(symbols, labels);
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "Line", "Address", "Label", "Operator", "Opd1", "Op2");
        getSymbolsTable(new File("input/firstpass")).symbols.forEach(symbol -> {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);
        });
    }

}
