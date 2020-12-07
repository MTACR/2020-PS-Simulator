package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static assembler.AssembleData.*;

public class FirstPass {

    /*private static final List<String> table = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "COPY", "DIVIDE", "LOAD", "MULT", "READ", "RET", "STOP",
            "STORE", "SUB", "WRITE", "CONST", "END", "EXTDEF", "EXTR", "INIT", "PROC", "SPACE", "STACK", "START");*/

    //TODO lidar com macros
    //TODO reservar endereços
    public static AssembleData getSymbolsTable(File file) {
        List<Symbol> symbols = new ArrayList<>();
        Map<String, Integer> labels = new HashMap<>();
        int address = 0;
        int line = 1;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;

            while ((string = reader.readLine()) != null) {
                string = string.toUpperCase();

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
                        symbols.add(new Symbol(line, address, "", lineArr[0], "", ""));
                        address += 1;

                    } else
                        throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 2) {

                    if (table0.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));
                        address += 1;

                        if (!labels.containsKey(lineArr[0]))
                            labels.put(lineArr[0], address);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else if (table1.contains(lineArr[0])) {
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], ""));
                        address += 2;
                    }

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 3) {

                    if (table1.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], ""));
                        address += 2;

                        if (!labels.containsKey(lineArr[0]))
                            labels.put(lineArr[0], address);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else if (table2.contains(lineArr[0])) {
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], lineArr[2]));
                        address += 3;
                    }

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    line++;
                    continue;
                }

                if (lineArr.length == 4) {

                    if (table2.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], lineArr[3]));
                        address += 3;

                        if (!labels.containsKey(lineArr[0]))
                            labels.put(lineArr[0], address);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

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

        // Aloca endereço para variáveis
        for (Symbol symbol : symbols)
            if (symbol.operator.equals("SPACE"))
                symbol.opd1 = String.valueOf(address++);

            else if (symbol.operator.equals("CONST"))
                symbol.opd2 = String.valueOf(address++);

        return new AssembleData(symbols, labels);
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "Line", "Address", "Label", "Operator", "Opd1", "Op2");
        getSymbolsTable(new File("input/firstpass")).symbols.forEach(symbol -> {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);
        });
    }

}
