package assembler;

import javafx.util.Pair;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static assembler.FirstPass.getSymbolsTable;
import static assembler.SecondPass.ADDRMODE.*;

public class SecondPass {

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
        SymbolsTable data = getSymbolsTable(file);
        List<Symbol> symbols = data.symbols;
        Map<String, Pair<Integer, Character>> labels = data.labels;
        Map<Integer, Pair<Integer, Character>> vars = new TreeMap<>();
        List<ObjectCode> objects = new ArrayList<>();

        for (Symbol symbol : symbols) {
            String operator = symbol.operator;
            String opd1 = symbol.opd1;
            String opd2 = symbol.opd2;

            int modeOpd1 = -1;
            int modeOpd2 = -1;
            int addrOpd1 = -1;
            int addrOpd2 = -1;
            int size = 0;

            if (labels.containsKey(opd1)) {
                modeOpd1 = 0;
                addrOpd1 = labels.get(opd1).getKey();
                size++;

            } else if (!opd1.isEmpty()) {
                ADDRMODE a = getAddrMode(opd1);

                if (a == IMEDIATO && !table.contains(operator))
                    throw new RuntimeException("Modo de endereçamento inválido em: " + operator + " " + opd1);

                if (a == IMEDIATO || a == INDIRETO)
                    opd1 = opd1.substring(1);

                if (a != null) {
                    modeOpd1 = a.getValue();
                    addrOpd1 = Integer.parseInt(opd1);
                }

                else throw new RuntimeException("Label não definida: " + opd1);

                size++;
            }

            if (labels.containsKey(opd2)) {
                modeOpd2 = 0;
                addrOpd2 = labels.get(opd2).getKey();
                size++;

            } else if (!opd2.isEmpty()) {
                ADDRMODE a = getAddrMode(opd2);

                if (a == IMEDIATO && !operator.equals("COPY"))
                    throw new RuntimeException("Modo de endereçamento inválido em: " + operator + " " + opd2);

                if (a == IMEDIATO || a == INDIRETO)
                    opd2 = opd2.substring(1);

                if (a != null) {
                    modeOpd2 = a.getValue();
                    addrOpd2 = Integer.parseInt(opd2);
                }

                else throw new RuntimeException("Label não definida: " + opd2);

                size++;
            }

            //TODO lidar com códigos q n são opcode
            int o = getOpcode(operator);

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

            } else {
                size++;

                int op = o;

                if (modeOpd1 != -1)
                    op += modeOpd1;

                if (modeOpd2 != -1)
                    op += modeOpd2;

                Pair<Integer, Character>[] words = new Pair[3];

                words[0] = new Pair<>(op, 'a');

                if (addrOpd1 != -1) {
                    if (labels.containsKey(opd1)) {
                        if (symbol.flag1 == '-')
                            addrOpd1 -= symbol.offset1;
                        else
                            addrOpd1 += symbol.offset1;

                        words[1] = new Pair<>(addrOpd1, 'r');

                    } else {
                        //TODO verificar modos de endereçamento
                        switch (ADDRMODE.values()[modeOpd1]) {
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

                if (addrOpd2 != -1) {
                    if (labels.containsKey(opd2)) {
                        if (symbol.flag2 == '-')
                            addrOpd2 -= symbol.offset2;
                        else
                            addrOpd2 += symbol.offset2;

                        words[2] = new Pair<>(addrOpd2, 'r');

                    } else {
                        //TODO verificar modos de endereçamento
                        switch (ADDRMODE.values()[modeOpd2]) {
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

                objects.add(new ObjectCode(symbol.address, size, words));
            }
        }

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
        if (opd.charAt(0) == '#') 		return IMEDIATO; //imediato
        if (opd.charAt(0) == 'I') 	    return INDIRETO; //indireto

        try {
            Double.parseDouble(opd);
                                        return DIRETO; //direto
        } catch (NumberFormatException nfe) {
                                        return null; //erro
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
