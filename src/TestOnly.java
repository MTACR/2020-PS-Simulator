import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestOnly {

    // enumerador para os trabalhar com os códigos por nome, ao invés de número
    public enum OPCODE {
        BR(0),
        BRPOS(1),
        ADD(2),
        LOAD(3),
        BRZERO(4),
        BRNEG(5),
        SUB(6),
        STORE(7),
        WRITE(8),
        RET(9),
        DIVIDE(10),
        STOP(11),
        READ(12),
        COPY(13),
        MULT(14),
        CALL(15);

        private final int op;

        OPCODE(int op) {
            this.op = op;
        }

        public int getValue()
        {
            return op;
        }
    }

    // vetor que representa a memória
    private final short[] memory = new short[1024];
    private int pc = 0;
    private int sp = 0;
    private int ri = 0;
    private int re = 0;
    private int acc = 0;
    private int mop = 0;

    // carrega uma palavra e extrai seu opcode. Essa função é responsável apenas por opcodes, não deve ler operandos.
    // o operando deve ser tratado na função parseOpCode
    public void nextInstruction() {
        if (pc >= 1024)
            return;

        short word = memory[pc++];

        ri = (word & 0xF000) >> 12;

        boolean f1 = (word & 0x800) >> 11 != 0;
        boolean f2 = (word & 0x400) >> 10 != 0;
        boolean f3 = (word & 0x200) >> 9  != 0;

        OPCODE opcode = OPCODE.values()[ri];
        System.out.print(opcode + " ");

        parseOpCode(opcode, f1, f2, f3);
    }

    // seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private void parseOpCode(OPCODE opcode, boolean f1, boolean f2, boolean f3) {
        switch (opcode) {
            case BR:
                break;
            case BRPOS:
                break;
            case ADD:
                add(f1, f3);
                System.out.println("= " + acc);
                break;
            case LOAD:
                load(f1, f3);
                break;
            case BRZERO:
                break;
            case BRNEG:
                break;
            case SUB:
                break;
            case STORE:
                break;
            case WRITE:
                break;
            case RET:
                break;
            case DIVIDE:
                break;
            case STOP:
                stop();
                break;
            case READ:
                break;
            case COPY:
                break;
            case MULT:
                break;
            case CALL:
                break;
        }

        nextInstruction();
    }

    // função add
    private void add(boolean f1, boolean f3) {
        acc += loadWord(f1, f3);
    }

    // função load
    private void load(boolean f1, boolean f3) {
        acc = loadWord(f1, f3);
    }

    // função stop
    private void stop() {
        pc = 1024;
    }

    // carrega um valor da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto e f3 diz se é imediato
    private short loadWord(boolean f1, boolean f3) {
        short s = memory[pc++];
        short offset = 0; //TODO

        System.out.println(s);

        if (f3) {
            return s;
        } else if (f1) {
            //TODO: indireto
            return memory[s + offset];
        } else {
            return memory[s];
        }
    }

    // carrega o arquivo passado por parâmetro para a memória a partir da posição zero
    public void loadFileToMemory(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            int i = 0;

            while ((line = reader.readLine()) != null)
                memory[i++] = (short) Integer.parseInt(line, 2);

            reader.close();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", file);
            e.printStackTrace();
        }
    }

    // imprime o conteudo da memória
    public void dumpMemory() {
        for (short s : memory)
            System.out.println(s);
    }

}
