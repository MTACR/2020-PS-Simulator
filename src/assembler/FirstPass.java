package assembler;

import javafx.util.Pair;
import linker.Usage;
import simulator.Interface;

import java.io.*;
import java.util.*;
import static assembler.SymbolsTable.*;

public class FirstPass {

    public static SymbolsTable getSymbolsTable(File file) {
        // Lista de símbolos válidos, ou seja, uma linha de um arquivo, com os campos propriamente organizados
        List<Symbol> symbols = new ArrayList<>();
        // Mapa de labels/variáveis
        Map<String, Pair<Integer, Character>> labels = new TreeMap<>();
        // Lista de definições externas
        List<String> extdefLabels = new ArrayList<>();
        // Lista de usos externos
        List<String> extuseLabels = new ArrayList<>();
        // Lista de variáveis sendo usadas
        List<Usage> extuseVars = new ArrayList<>();

        boolean hasStart = false;
        boolean hasEnd = false;
        boolean hasStack = false;
        int address = 1;
        int line = 1;
        String name = "";

        Interface.instance().printMessage("Executando primeiro passo...");

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

                // até aqui foram verificações de formatação da string

                // esse switch decide a quantidade de tokens na linha sendo processada
                // as table* são listas que contém todos tokens válidos, organizados por quantidade de parâmetros
                // ou seja, table0 -> 0 parâmetros, table1 -> 1 parâmetro, etc.

                switch (lineArr.length) {

                    case 1:

                        if (table0.contains(lineArr[0])) {

                            if (lineArr[0].equals("END")) {
                                if (hasEnd)
                                    throw new RuntimeException("Fim redefinido em " + line);
                                else
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

                            // Se o 2 token for EXTR, adiciona 1 token às labels e aos usos externos
                            if (lineArr[1].equals("EXTR")) {
                                if (!extuseLabels.contains(lineArr[0])) {
                                    extuseLabels.add(lineArr[0]);
                                    labels.put(lineArr[0], new Pair<>(0, 'r'));
                                }

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                            } else {

                                if (table.contains(lineArr[0]))
                                    throw new RuntimeException("Label inválida " + line);

                                symbols.add(new Symbol(line, address, lineArr[0], lineArr[1], "", ""));

                                // Se token 1 for label, adiciona ás labels
                                if (!labels.containsKey(lineArr[0]))
                                    labels.put(lineArr[0], new Pair<>(address, 'r'));

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[0] + " em " + line);

                                address += 1;
                            }
                        }

                        else if (table1.contains(lineArr[0])) {

                            if (lineArr[0].equals("START")) {
                                if (hasStart)
                                    throw new RuntimeException("Início redefinido em " + line);
                                else
                                    hasStart = true;

                                name = lineArr[1];

                                break;
                            }

                            // Se token 1 for EXTDEF, adiciona labels às definições externas
                            // E à lista de variáveis em uso
                            if (lineArr[0].equals("EXTDEF")) {
                                if (!extdefLabels.contains(lineArr[1]))
                                    extdefLabels.add(lineArr[1]);

                                else throw new RuntimeException("Símbolo redefinido: " + lineArr[1] + " em " + line);

                            } else if (lineArr[0].equals("STACK")) {
                                if (hasStack)
                                    throw new RuntimeException("Tamanho da stack redefinido em " + line);
                                else
                                    hasStack = true;

                                if (Integer.parseInt(lineArr[1]) > 10 || Integer.parseInt(lineArr[1]) < 0)
                                    throw new RuntimeException("Tamanho de pilha inválido em " + line);

                                symbols.add(0, new Symbol(line, 0, "", lineArr[0], lineArr[1], ""));

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

                            // Se token 1 for label, adiciona ás labels
                            if (!labels.containsKey(lineArr[0]))
                                labels.put(lineArr[0], new Pair<>(address, 'r'));

                            else throw new RuntimeException("Símbolo redefinido: " + lineArr[0]);

                            // Se token 2 for CONST, soma apenas 1 endereço
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

                            // Se token 1 for label, adiciona ás labels
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

        // Lista de labels, que são na verdade variáveis, para serem alocadas na memória
        List<Symbol> labels2Alloc = new ArrayList<>();

        // Percorre todos os símbolos à fim de lidar com alocação das labels, que ocorrerá no segundo passo
        for (Symbol symbol : symbols) {

            // Se possui label, verifica se é declarção de variável ou label
            if (!symbol.label.isEmpty()) {
                switch (symbol.operator) {

                    // Se for declaração, opd1 = novo endereço válido
                    case "SPACE":
                        symbol.opd1 = String.valueOf(address++);

                        break;

                    // Se for constante, opd2 = valor da constante e opd1 = novo endereço válido
                    case "CONST":
                        symbol.opd2 = symbol.opd1;
                        symbol.opd1 = String.valueOf(address++);

                        break;

                    // Se for label, cria um novo símbolo com operador LABEL e adiciona à lista de alocações e
                    // ao mapa de labels
                    default:
                        labels2Alloc.add(new Symbol(line++, address, symbol.label, "LABEL",
                                String.valueOf(labels.get(symbol.label).getKey()), ""));
                        labels.replace(symbol.label, new Pair<>(address, 'r'));

                        address++;

                        break;
                }
            }

            // Se a lista de uso possui a label definida no opd, se adiciona label à lista de usos,
            // e marca a flag de variável externa (isso serve para que o offset (0) seja absoluto no passo 2
            if (extuseLabels.contains(symbol.opd1)) {
                extuseVars.add(new Usage(symbol.opd1, symbol.address + 1, '+'));
                symbol.opd1 = String.valueOf(0);
                symbol.ext1 = true;
            }

            if (extuseLabels.contains(symbol.opd2)) {
                extuseVars.add(new Usage(symbol.opd2, symbol.address + 2, '+'));
                symbol.opd2 = String.valueOf(0);
                symbol.ext2 = true;
            }
        }

        // Adiciona à lista de símbolos as variáveis a serem alocadas
        symbols.addAll(labels2Alloc);

        if (!hasStack)
            symbols.add(0, new Symbol(0, 0, "", "STACK", "10", ""));

        String string = "";

        // Lista de variáveis externas que devem ser removidas do mapa de labels
        List<String> ext2Remove = new ArrayList<>();

        // Percorre mapa de labels, criando o arquivo de usos/definições
        for (Map.Entry<String, Pair<Integer, Character>> entry : labels.entrySet()) {
            String label = entry.getKey();
            Pair<Integer, Character> addr = entry.getValue();

            // Se definições externas possui label, salva no arquivo
            if (extdefLabels.contains(label)) {
                string += label + " " + addr.getKey() + " " + addr.getValue() + "\n";
                extdefLabels.remove(label);

            // Se usos externos possui label, marca para ser removido
            // Isso serve para que no segundo passo labels externas não sejam encontrdas e o offset receba flag 'a'
            } else if (extuseLabels.contains(label)) {
                ext2Remove.add(label);
            }
        }

        // Remove todos símbolos externos do mapa
        for (String s : ext2Remove)
            labels.remove(s);

        // Caso ainda haja algum símbolo, significa que ele não foi usado
        if (!extdefLabels.isEmpty())
            throw new RuntimeException("Simbolo global não definido " + extdefLabels);

        File tbl = new File("output/" + name + ".tbl");

        try {
            FileWriter out = new FileWriter(tbl);

            // Gera a tabela de usos
            for (Usage usage : extuseVars)
                string += usage.symbol + " " + usage.locationCounter + " " + usage.opsign + "\n";

            out.write(string);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SymbolsTable(symbols, labels, name);
    }

    /*public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "Line", "Address", "Label", "Operator", "Opd1", "Op2");
        getSymbolsTable(new File("input/firstpass.asm")).symbols.forEach(symbol -> {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);
        });
    }*/

}
