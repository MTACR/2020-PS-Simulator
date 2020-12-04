package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstPass {

    /*private static final List<String> table = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "COPY", "DIVIDE", "LOAD", "MULT", "READ", "RET", "STOP",
            "STORE", "SUB", "WRITE", "CONST", "END", "EXTDEF", "EXTR", "INIT", "PROC", "SPACE", "STACK", "START");*/

    private static final List<String> table0 = Arrays.asList(
            "RET", "STOP", "END", "EXTR", "SPACE", "MACRO");

    private static final List<String> table1 = Arrays.asList(
            "ADD", "BR", "BRNEG", "BRPOS", "BRZERO", "CALL", "DIVIDE", "LOAD", "MULT", "READ",
            "STORE", "SUB", "WRITE", "CONST", "EXTDEF", "INIT", "STACK", "START");

    private static final List<String> table2 = Arrays.asList(
            "COPY", "PROC");

    //TODO lidar com macros
    public static List<Symbol> getSymbolsTable(File file) {
        List<Symbol> symbols = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;
            int line = 1;
            int address = 0;

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

                    if (table0.contains(lineArr[0]))
                        symbols.add(new Symbol(line, address, "", lineArr[0], "", ""));

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    address += 1;
                    line++;
                    continue;
                }

                if (lineArr.length == 2) {

                    if (table0.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));

                        if (!labels.contains(lineArr[0]))
                            labels.add(lineArr[0]);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else if (table1.contains(lineArr[0]))
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], ""));

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    address += 2;
                    line++;
                    continue;
                }

                if (lineArr.length == 3) {

                    if (table1.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], ""));

                        if (!labels.contains(lineArr[0]))
                            labels.add(lineArr[0]);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else if (table2.contains(lineArr[0]))
                        symbols.add(new Symbol(line, address, "", lineArr[0], lineArr[1], lineArr[2]));

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    address += 3;
                    line++;
                    continue;
                }

                if (lineArr.length == 4) {

                    if (table2.contains(lineArr[1])) {
                        symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], lineArr[2], lineArr[3]));

                        if (!labels.contains(lineArr[0]))
                            labels.add(lineArr[0]);
                        else
                            throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);
                    }

                    else
                        throw new RuntimeException("Instrução inválida em " + line);

                    address += 3;
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

        return symbols;
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "Line", "Address", "Label", "Operator", "Opd1", "Op2");
        getSymbolsTable(new File("input/firstpass")).forEach(symbol -> {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);
        });
    }

}
