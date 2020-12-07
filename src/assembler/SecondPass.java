package assembler;

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

    public static void pass(File file) {
        AssembleData data = getSymbolsTable(file);
        List<Symbol> symbols = data.symbols;
        Map<String, Integer> labels = data.labels;
        List<String> consts = new ArrayList<>();

        String binaryOut = "";
        int stackSize = 0;

        for (Symbol symbol : symbols) {
            String label = symbol.label;
            String operator = symbol.operator;
            String opd1 = symbol.opd1;
            String opd2 = symbol.opd2;

            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", symbol.line, symbol.address, symbol.label, symbol.operator, symbol.opd1, symbol.opd2);

            int modeOpd1 = -1;
            int modeOpd2 = -1;
            int addrOpd1 = -1;
            int addrOpd2 = -1;

            if (labels.containsKey(opd1)) {
                modeOpd1 = 0;
                addrOpd1 = labels.get(opd1);

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
                else
                    throw new RuntimeException("Label não definida: " + opd1);
            }

            if (labels.containsKey(opd2)) {
                modeOpd2 = 0;
                addrOpd2 = labels.get(opd2);

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
                else
                    throw new RuntimeException("Label não definida: " + opd2);
            }

            //TODO lidar com códigos q n são opcode
            int o = getOpcode(operator);

            if (o == -1) {
                if (operator.equals("SPACE"))
                    binaryOut += opdBinary(Integer.parseInt(opd1));

                else if (operator.equals("CONST")) {
                    binaryOut += opdBinary(Integer.parseInt(opd2));
                    consts.add(opd1);
                }

                else throw new RuntimeException("Instrução inválida");

            } else {
                binaryOut += opcodeBinary(o, modeOpd1, modeOpd2);

                if (!opd1.isEmpty())
                    binaryOut += opdBinary(addrOpd1);

                if (!opd2.isEmpty())
                    binaryOut += opdBinary(addrOpd2);
            }
        }

        /*String stack = fillBinary("0", 16, 'l') + "\n" + fillBinary("0", 16, 'l') + "\n"; //duas primeiras linhas zeradas
        stack += fillBinary(Integer.toBinaryString(stackSize), 16, 'l') + "\n"; //tamanho da pilha

        for (int i = 0; i < stackSize; i++)
            stack += fillBinary("0", 16, 'l') + "\n";

        binaryOut = stack + binaryOut;*/

        // salva constantes
        for (String s : consts)
            binaryOut += opdBinary(Integer.parseInt(s));

        File fileOut = new File("input/secondpass");

        try {
            FileWriter out = new FileWriter(fileOut);
            out.write(binaryOut);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String opcodeBinary(int opcode, int addrOpd1, int addrOpd2) {
        String opcodeBin = Integer.toBinaryString(opcode);
        opcodeBin = fillBinary(opcodeBin, 4, 'l');

        //adicionar mais um bit porque a descrição do trabalho tá confusa
        opcodeBin = '0' + opcodeBin;

        //bit 5
        if (addrOpd1 == 1) opcodeBin = '1' + opcodeBin;
        else opcodeBin = '0' + opcodeBin;

        //bit 6
        if (addrOpd2 == 1) opcodeBin = '1' + opcodeBin;
        else opcodeBin = '0' + opcodeBin;

        //bit 7
        if (addrOpd1 == 2 || addrOpd2 == 2) opcodeBin = '1' + opcodeBin;
        else opcodeBin = '0' + opcodeBin;

        opcodeBin = fillBinary(opcodeBin, 16, 'l');

        return opcodeBin + "\n";
    }

    private static String fillBinary(String numBinary, int size, char fill) {
        String aux = "";

        while (numBinary.length() + aux.length() < size) aux += '0';

        if (fill == 'l') return aux + numBinary;
        else return numBinary + aux;
    }

    private static String opdBinary(int addr) {
        String opdBin = Integer.toBinaryString(addr);
        opdBin = fillBinary(opdBin, 16, 'l');

        return opdBin + '\n';
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
        pass(new File("input/firstpass"));
    }

}