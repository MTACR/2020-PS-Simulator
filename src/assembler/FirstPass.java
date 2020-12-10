package assembler;

import javafx.util.Pair;
import linker.Usage;

import java.io.*;
import java.util.*;
import static assembler.SymbolsTable.*;

public class FirstPass {

    public static SymbolsTable getSymbolsTable(File file) {
        List<Symbol> symbols = new ArrayList<>();
        Map<String, Pair<Integer, Character>> labels = new TreeMap<>();
        List<String> extdef = new ArrayList<>();
        List<String> extr = new ArrayList<>();
        List<Usage> usages = new ArrayList<>();

        boolean hasStart = false;
        boolean hasEnd = false;
        int address = 0;
        int line = 1;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;

            while ((string = reader.readLine()) != null) {
                string = string.toUpperCase().replaceAll("\\s+"," ").trim();

                if (string.length() > 80)
                    throw new RuntimeException("Linha muito longa em" + line);

                if (string.contains("*"))
                    string = string.substring(0, string.indexOf("*"));

                if (string.isEmpty()) {
                    line++;
                    continue;
                }

                String[] lineArr = string.split(" ");
                for (int i = 0; i < lineArr.length; i++)
                    lineArr[i] = lineArr[i].trim();

                switch (lineArr.length) {

                    case 1:

                        if (table0.contains(lineArr[0])) {

                            if (lineArr[0].equals("END")) {
                                hasEnd = true;
                                break;
                            }

                            if (lineArr[0].equals("EXTR"))
                                throw new RuntimeException("Instrução exige um label em " + line);

                            symbols.add(new Symbol(line, address, "", lineArr[0], "", ""));
                            address += 1;

                        } else throw new RuntimeException("Instrução inválida em " + line);

                        break;

                    case 2:

                        if (table0.contains(lineArr[1])) {

                            if (lineArr[1].equals("EXTR")) {
                                if (!extr.contains(lineArr[0])) {
                                    extr.add(lineArr[0]);
                                    labels.put(lineArr[0], new Pair<>(0, 'r'));
                                }

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                            } else {

                                if (table.contains(lineArr[0]))
                                    throw new RuntimeException("Label inválida " + line);

                                symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));

                                if (!labels.containsKey(lineArr[0]))
                                    labels.put(lineArr[0], new Pair<>(address, 'r'));

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                                address += 1;
                            }
                        }

                        else if (table1.contains(lineArr[0])) {

                            if (lineArr[0].equals("START")) {
                                hasStart = true;
                                break;
                            }

                            if (lineArr[0].equals("EXTDEF")) {
                                if (!extdef.contains(lineArr[1])) {
                                    extdef.add(lineArr[1]);
                                    usages.add(new Usage(lineArr[1], address, '?'));
                                }

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[1] + " em " + line);

                            } else {

                                symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], ""));

                                address += 2;
                            }
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

        if (!hasStart)
            throw new RuntimeException("Modulo não possui início declarado");

        if (!hasEnd)
            throw new RuntimeException("Modulo não possui fim declarado");

        List<Symbol> labels2Alloc = new ArrayList<>();

        for (Symbol symbol : symbols) {
            if (!symbol.label.isEmpty()) {
                switch (symbol.operator) {

                    case "SPACE":
                        symbol.opd1 = String.valueOf(address++);

                        //System.out.println("SPACE: " + symbol.label + " in " + labels.get(symbol.label));

                        break;

                    case "CONST":
                        symbol.opd2 = symbol.opd1;
                        symbol.opd1 = String.valueOf(address++);

                        //System.out.println("CONST: " + symbol.label + " in " + labels.get(symbol.label));

                        break;

                    default:
                        labels2Alloc.add(new Symbol(line++, address, symbol.label, "LABEL", String.valueOf(labels.get(symbol.label).getKey()), ""));
                        labels.replace(symbol.label, new Pair<>(address, 'r'));

                        //System.out.println("LABEL: " + symbol.label + " in " + labels.get(symbol.label));

                        address++;

                        break;
                }
            }

            if (extr.contains(symbol.opd1)) {
                if (symbol.flag1 != null) {
                    usages.add(new Usage(symbol.opd1, symbol.address + 1, symbol.flag1));
                    symbol.opd1 = String.valueOf(symbol.offset1);
                } else
                    usages.add(new Usage(symbol.opd1, symbol.address + 1, '+'));
            }

            if (extr.contains(symbol.opd2))
                if (symbol.flag2 != null) {
                    usages.add(new Usage(symbol.opd2, symbol.address + 2, symbol.flag2));
                    symbol.opd2 = String.valueOf(symbol.offset2);
                } else
                    usages.add(new Usage(symbol.opd2, symbol.address + 2, '+'));
        }

        symbols.addAll(labels2Alloc);

        String string = "<definition>\n";

        for (Map.Entry<String, Pair<Integer, Character>> entry : labels.entrySet()) {
            String label = entry.getKey();
            Pair<Integer, Character> addr = entry.getValue();

            if (!extr.contains(label))
                string += label + " " + addr.getKey() + " " + addr.getValue() + "\n";

            extdef.remove(label);
        }

        if (!extdef.isEmpty())
            throw new RuntimeException("Simbolo global não definido " + extdef);

        File tbl = new File("output/" + file.getName().substring(0, file.getName().indexOf('.')) + ".tbl");

        try {
            FileWriter out = new FileWriter(tbl);

            string += "</definition>\n";
            string += "<usage>\n";

            for (Usage usage : usages) {
                if (extr.contains(usage.symbol))
                    string += usage.symbol + " " + usage.locationCounter + " " + usage.opsign + "\n";
                else
                    string += usage.symbol + " " + usage.locationCounter + " " + "r" + "\n";
            }

            string += "</usage>\n";

            out.write(string);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SymbolsTable(symbols, labels);
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "Line", "Address", "Label", "Operator", "Opd1", "Op2");
        getSymbolsTable(new File("input/testemacro.asm.proc")).symbols.forEach(symbol -> {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);
        });
    }

}
