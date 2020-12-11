package assembler;

import javafx.util.Pair;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static assembler.FirstPass.getSymbolsTable;
import static assembler.SecondPass.ADDRMODE.*;

public class SecondPass {

    // Tabela dos modos que suportam modo imediato
    private static final List<String> table = Arrays.asList(
            "ADD", "DIVIDE", "LOAD", "MULT", "SUB", "WRITE");

    enum ADDRMODE {
        DIRETO(0), INDIRETO(1), IMEDIATO(2);

        private final int op;

        ADDRMODE(int op) {
            this.op = op;
        }

        public int getValue()
        {
            return op;
        }
    }

    public static List<ObjectCode> pass(File file) {
        // Informações do passo 1
        SymbolsTable data = getSymbolsTable(file);

        // Lista símbolos (que deverão ser convertidos em código objeto nesse passo)
        List<Symbol> symbols = data.symbols;

        // Mapa de labels intrnas (as externas foram excluídas no passo 1)
        Map<String, Pair<Integer, Character>> labels = data.labels;

        // Mapa de variáveis a serem alocadas
        Map<Integer, Pair<Integer, Character>> vars = new TreeMap<>();

        // Lista de código objeto
        List<ObjectCode> objects = new ArrayList<>();

        for (Symbol symbol : symbols) {
            String operator = symbol.operator;
            String opd1 = symbol.opd1;
            String opd2 = symbol.opd2;

            ADDRMODE modeOpd1 = null;
            ADDRMODE modeOpd2 = null;
            int addrOpd1 = -1;
            int addrOpd2 = -1;
            int size = 0;

            // Se o operando é uma label, obtém seu endereço
            if (labels.containsKey(opd1)) {
                modeOpd1 = DIRETO;
                addrOpd1 = labels.get(opd1).getKey();
                size++;

            } else if (!opd1.isEmpty()) {
                ADDRMODE a = getAddrMode(opd1);

                if (a == IMEDIATO && !table.contains(operator))
                    throw new RuntimeException("Modo de endereçamento inválido em: " + operator + " " + opd1);

                if (a != null) {

                    if (a == IMEDIATO)
                        opd1 = opd1.substring(1);

                    else if (a == INDIRETO)
                        opd1 = opd1.substring(0, opd1.indexOf(",I"));

                    modeOpd1 = a;
                    addrOpd1 = Integer.parseInt(opd1);
                }

                else throw new RuntimeException("Label não definida: " + opd1);

                size++;
            }

            if (labels.containsKey(opd2)) {
                modeOpd2 = DIRETO;
                addrOpd2 = labels.get(opd2).getKey();
                size++;

            } else if (!opd2.isEmpty()) {
                ADDRMODE a = getAddrMode(opd2);

                if (a == IMEDIATO && !operator.equals("COPY"))
                    throw new RuntimeException("Modo de endereçamento inválido em: " + operator + " " + opd2);

                if (a != null) {

                    if (a == IMEDIATO)
                        opd2 = opd2.substring(1);

                    else if (a == INDIRETO)
                        opd2 = opd2.substring(0, opd2.indexOf(",I"));

                    modeOpd2 = a;
                    addrOpd2 = Integer.parseInt(opd2);
                }

                else throw new RuntimeException("Label não definida: " + opd2);

                size++;
            }

            int o = getOpcode(operator);

            // Se instrução não for opcode, será uma instrução (não código objeto) de alocação de espaço
            if (o == -1) {
                switch (operator) {
                    case "SPACE":
                        vars.put(Integer.parseInt(opd1), new Pair(0, 'a'));
                        objects.add(new ObjectCode(symbol.address, 1, new Pair<>(Integer.parseInt(opd1), 'r')));

                        break;

                    case "CONST":
                        vars.put(Integer.parseInt(opd1), new Pair(Integer.parseInt(opd2), 'a'));
                        objects.add(new ObjectCode(symbol.address, 1, new Pair<>(Integer.parseInt(opd1), 'r')));

                        break;

                    case "LABEL":
                        vars.put(symbol.address, new Pair(Integer.parseInt(opd1), 'r'));

                        break;

                    default: throw new RuntimeException("Instrução inválida");
                }

            // Se for instrução, gera código objeto
            } else {
                size++;

                int op = o;

                if (modeOpd1 != null) {
                    switch (modeOpd1) {
                        case INDIRETO: op |= 32;
                        break;

                        case IMEDIATO: op |= 128;
                        break;
                    }
                }

                if (modeOpd2 != null) {
                    switch (modeOpd2) {
                        case INDIRETO: op |= 64;
                            break;

                        case IMEDIATO: op |= 128;
                            break;
                    }
                }

                Pair<Integer, Character>[] words = new Pair[3];

                // opcode é absoluto
                words[0] = new Pair<>(op, 'a');

                // Define modo do operando
                // Se for label é relativo
                // Se for símbolo externo é absoluto, pois operando representa offset
                // Se for direto ou indireto é relativo
                // Se for imediato é absoluto
                if (addrOpd1 != -1) {
                    if (labels.containsKey(opd1)) {
                        words[1] = new Pair<>(addrOpd1, 'r');

                    } else {
                        if (symbol.ext1) {
                            words[1] = new Pair<>(addrOpd1, 'a');

                        } else {
                            //TODO verificar modos de endereçamento se está ok
                            switch (modeOpd1) {
                                case DIRETO:
                                case INDIRETO:
                                    words[1] = new Pair<>(addrOpd1, 'r');
                                    break;

                                case IMEDIATO:
                                    words[1] = new Pair<>(addrOpd1, 'a');
                                    break;
                            }
                        }
                    }
                }

                if (addrOpd2 != -1) {
                    if (labels.containsKey(opd2)) {
                        words[2] = new Pair<>(addrOpd2, 'r');

                    } else {
                        //TODO verificar modos de endereçamento se está ok
                        if (symbol.ext2) {
                            words[2] = new Pair<>(addrOpd1, 'a');

                        } else {
                            switch (modeOpd2) {
                                case DIRETO:
                                case INDIRETO:
                                    words[2] = new Pair<>(addrOpd2, 'r');
                                    break;

                                case IMEDIATO:
                                    words[2] = new Pair<>(addrOpd2, 'a');
                                    break;
                            }
                        }
                    }
                }
                // Adiciona à lista de código objeto com endereço, quantidade de palavras e palavras
                objects.add(new ObjectCode(symbol.address, size, words));
            }
        }

        // Para cada variável alocada, gera um espaço de memória com seu devido endereço e modo
        vars.forEach((addr, pair) -> objects.add(new ObjectCode(addr, 1, new Pair<>(pair.getKey(), pair.getValue()))));

        File obj = new File("output/" + file.getName().substring(0, file.getName().indexOf('.')) + ".obj");

        try {
            FileWriter outObj = new FileWriter(obj);
            String sObj = "";

            for (ObjectCode objectCode : objects)
                sObj += objectCode.address + " " + objectCode.size + " " + objectCode.printWords() + "\n";

            outObj.write(sObj);
            outObj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objects;
    }

    private static ADDRMODE getAddrMode(String opd) {
        if (opd.startsWith("#")) 		return IMEDIATO; //imediato
        if (opd.endsWith(",I")) 	    return INDIRETO; //indireto

        try {
            Double.parseDouble(opd);
                                        return DIRETO;   //direto
        } catch (NumberFormatException nfe) {
                                        return null;     //erro
        }
    }

    private static int getOpcode(String opcode) {
        switch (opcode.toLowerCase()) {
            case "br":
                return 0;
            case "brpos":
                return 1;
            case "add":
                return 2;
            case "load":
                return 3;
            case "brzero":
                return 4;
            case "brneg":
                return 5;
            case "sub":
                return 6;
            case "store":
                return 7;
            case "write":
                return 8;
            case "ret":
                return 9;
            case "divide":
                return 10;
            case "stop":
                return 11;
            case "read":
                return 12;
            case "copy":
                return 13;
            case "mult":
                return 14;
            case "call":
                return 15;
        }

        return -1;
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-10s\n", "Address", "Size", "Machine");
        pass(new File("input/firstpass.asm")).forEach(objectCode -> {
            System.out.printf("%-10s %-10s %-10s\n", objectCode.address, objectCode.size, objectCode.printWords());
        });
    }

}
