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

    // carrega uma palavra e extrai seu opcode. Essa função é responsável apenas por opcodes, não deve ler operandos.
    // o operando deve ser tratado na função parseOpCode
    public void nextInstruction() {
        short word = memory[pc++];

        OPCODE opcode = OPCODE.values()[(word & 0xF000) >> 12];

        boolean f1 = (word & 0x800) >> 11 != 0;
        boolean f2 = (word & 0x400) >> 10 != 0;
        boolean f3 = (word & 0x200) >> 9  != 0;

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
                break;
            case LOAD:
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
