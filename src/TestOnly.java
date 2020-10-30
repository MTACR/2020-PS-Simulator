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
    private short pc = 0;
    private short sp = 0;
    private short ri = 0;
    private short re = 0;
    private short acc = 0;
    private byte mop = 0;

    // carrega uma palavra e extrai seu opcode. Essa função é responsável apenas por opcodes, não deve ler operandos.
    // o operando deve ser tratado na função parseOpCode
    public void nextInstruction() {
        if (pc >= 1024)
            return;

        short word = memory[pc++];

        ri = (short) ((word & 0xF000) >> 12);

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
                branch(f1);
                break;
            case BRPOS:
                branchPos(f1);
                break;
            case ADD:
                add(f1, f3);
                //System.out.println("= " + acc);
                break;
            case LOAD:
                load(f1, f3);
                break;
            case BRZERO:
                branchZero(f1);
                break;
            case BRNEG:
                branchNeg(f1);
                break;
            case SUB:
                sub(f1, f3);
                System.out.println("= " + acc);
                break;
            case STORE:
                store(f1, f3);
                break;
            case WRITE:
                //write(f1, f3);
                break;
            case RET:
                //ret();
                break;
            case DIVIDE:
                divide(f1, f3);
                System.out.println("= " + acc);
                break;
            case STOP:
                stop();
                break;
            case READ:
                break;
            case COPY:
                break;
            case MULT:
                mult(f1, f3);
                System.out.println("= " + acc);
                break;
            case CALL:
                break;
        }

        nextInstruction();
    }

    private void branch(boolean f1){
        pc = loadWord(f1);
    }

    private void branchNeg(boolean f1){
        if (acc < 0){
            branch(f1);
        }
    }

    private void branchPos(boolean f1){
        if (acc > 0){
            branch(f1);
        }
    }

    private void branchZero(boolean f1){
        if (acc == 0){
            branch(f1);
        }
    }

    private void add(boolean f1, boolean f3) {
        acc += loadWord(f1, f3);
    }

    private void divide(boolean f1, boolean f3) {
        acc /= loadWord(f1, f3);
    }

    private void load(boolean f1, boolean f3) {
        acc = loadWord(f1, f3);
    }

    private void mult(boolean f1, boolean f3) {
        acc *= loadWord(f1, f3);
    }

    private void sub(boolean f1, boolean f3) {
        acc -= loadWord(f1, f3);
    }

    private void store(boolean f1, boolean f3) {
        short storeAddress = loadWord(f1, f3);
        memory[storeAddress] = acc;
    }

    private void stop() {
        pc = 1024;
    }

    // carrega um valor da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto e f3 diz se é imediato
    private short loadWord(boolean f1) {
        return loadWord(f1, false);
    }

    private short loadWord(boolean f1, boolean f3) {
        short s = memory[pc++];

        System.out.println(s);

        if (f3) {
            return s;
        } else if (f1) {
            short contentsAddress = memory[s];
            return memory[contentsAddress];
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
