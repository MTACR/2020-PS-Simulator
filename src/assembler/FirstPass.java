package assembler;

import javafx.util.Pair;

import java.io.*;
import java.util.*;
import static assembler.SymbolsTable.*;

public class FirstPass {

    public static SymbolsTable getSymbolsTable(File file) {
        List<Symbol> symbols = new ArrayList<>();
        Map<String, Pair<Integer, Character>> labels = new TreeMap<>();
        List<String> extdef = new ArrayList<>();

        int address = 0;
        int line = 1;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;

            while ((string = reader.readLine()) != null) {
                string = string.toUpperCase().replaceAll("\\s+"," ").trim();

                //TODO receber arquivo sem , do processador de macros
                //string = string.replace(",", "");

                if (string.length() > 80)
                    throw new RuntimeException("Linha muito longa em" + line);

                //ignora comentários
                if (string.contains("*"))
                    string = string.substring(0, string.indexOf("*"));

                //ignora linhas em branco
                if (string.isEmpty()) {
                    line++;
                    continue;
                }

                //divide linha quando acha um espaço
                String[] lineArr = string.split(" ");
                for (int i = 0; i < lineArr.length; i++)
                    lineArr[i] = lineArr[i].trim();

                switch (lineArr.length) {

                    case 1:

                        if (table0.contains(lineArr[0])) {
                            if (lineArr[0].equals("EXTR"))
                                throw new RuntimeException("Instrução exige um label em " + line);

                            symbols.add(new Symbol(line, address, "", lineArr[0], "", ""));
                            address += 1;

                        } else throw new RuntimeException("Instrução inválida em " + line);

                        break;

                    case 2:

                        if (table0.contains(lineArr[1])) {

                            if (table.contains(lineArr[0]))
                                throw new RuntimeException("Label inválida " + line);

                            symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));

                            if (!labels.containsKey(lineArr[0]))
                                labels.put(lineArr[0], new Pair<>(address, 'r'));

                            else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                            address += 1;
                        }

                        else if (table1.contains(lineArr[0])) {

                            if (lineArr[0].equals("EXTDEF")) {
                                if (!extdef.contains(lineArr[1]))
                                    extdef.add(lineArr[1]);

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[1] + " em " + line);
                            }

                            symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], ""));

                            if (!lineArr[0].equals("EXTDEF"))
                                address += 2;
                        }

                        else throw new RuntimeException("Instrução inválida em " + line);

                        break;

                    case 3:

                        if (table1.contains(lineArr[1])) {

                            if (table.contains(lineArr[0]) || table.contains(lineArr[2]))
                                throw new RuntimeException("Label inválida em " + line);

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

                        break;

                    case 4:

                        if (table2.contains(lineArr[1])) {

                            if (table.contains(lineArr[0]) || table.contains(lineArr[2]) || table.contains(lineArr[3]))
                                throw new RuntimeException("Label inválida " + line);

                            symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], lineArr[3]));

                            if (!labels.containsKey(lineArr[0]))
                                labels.put(lineArr[0], new Pair<>(address, 'r'));

                            else throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);

                            address += 3;
                        }

                        else throw new RuntimeException("Instrução inválida em " + line);

                        break;

                    default: throw new RuntimeException("Erro de sintaxe em " + line);
                }

                line++;
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

                    case "EXTR":
                        if (labels.containsKey(symbol.label)) {
                            symbol.opd1 = String.valueOf(0);
                            labels.replace(symbol.label, new Pair<>(symbol.address, '-'));

                        } else throw new RuntimeException("Símbols global não existe: " + symbol.label);
                        //address++;

                        break;

                    case "CONST":
                        symbol.opd2 = symbol.opd1;
                        symbol.opd1 = String.valueOf(address++);

                        break;

                    default:
                        labels2Alloc.add(new Symbol(line++, address, symbol.label, "LABEL", String.valueOf(labels.get(symbol.label).getKey()), ""));
                        labels.replace(symbol.label, new Pair<>(address, 'r'));

                        address++;

                        break;
                }

            }  else if (symbol.operator.equals("EXTDEF")) {
                if (labels.containsKey(symbol.opd1))
                    labels.replace(symbol.opd1, new Pair<>(symbol.address, '+'));

                else throw new RuntimeException("Símbolos global não existe: " + symbol.opd1);
            }
        }

        //if (!extdef.isEmpty())
            //throw new RuntimeException("Símbolos globais não existem: " + extdef);

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
