import java.io.File;
import java.util.Scanner;

public class Processor {

    private final short re;
    private short pc;
    private short acc = 0;
    private short sp = 0;
    private short ri = 0;
    private byte mop = 0;

    private final Memory memory;

    public Processor(File file) {
        memory = new Memory(1024);
        re = memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados

        short stackSize = (short) 3; //MUDAR DEPOIS DE PILHA PRONTA!
        //-----------------------------------
        pc = (short) (stackSize + 1);

        nextInstruction();
    }

    public void nextInstruction() {
        if (pc == 0)
            System.err.println("Stack overflow");

        if (pc >= re)
            System.err.println("Program counter cannot access data memory");

        memory.setDebug(pc);

        short word = memory.getWord(pc++, false, true);

        ri = (short) ((word & 0xF000) >> 12);

        boolean f1 = (word & 0x800) >> 11 != 0;
        boolean f2 = (word & 0x400) >> 10 != 0;
        boolean f3 = (word & 0x200) >> 9  != 0;

        TestOnly.OPCODE opcode = TestOnly.OPCODE.values()[ri];
        System.out.print("\n" + opcode);

        parseOpCode(opcode, f1, f2, f3);
    }

    // seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private void parseOpCode(TestOnly.OPCODE opcode, boolean f1, boolean f2, boolean f3) {
        boolean stop = false;

        switch (opcode) {
            case BR:
                branch(f1);
                break;
            case BRPOS:
                branchPos(f1);
                break;
            case ADD:
                add(f1, f3);
                System.out.println("\n= " + acc);
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
                store(f1);
                break;
            case WRITE:
                write(f1, f3);
                break;
            case RET:
                ret();
                break;
            case DIVIDE:
                divide(f1, f3);
                System.out.println("= " + acc);
                break;
            case STOP:
                //stop();
                stop = true;
                System.out.println("\n");
                break;
            case READ:
                read(f1);
                break;
            case COPY:
                copy(f1, f2, f3);
                break;
            case MULT:
                mult(f1, f3);
                System.out.println("= " + acc);
                break;
            case CALL:
                call(f1);
                break;
        }

        if (!stop)
            try {
                nextInstruction();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void write(boolean f1, boolean f3){
        //Placeholder?
        short word = memory.getWord(pc++, f1, f3);
        System.out.println("Output: " + word);
        //TODO?
    }

    private void ret(){
        //TODO
    }

    private void read(boolean f1){
        //Placeholder?
        Scanner inputScanner = new Scanner(System.in);
        short input = inputScanner.nextByte();

        short address = memory.getAddress(pc++, f1);
        memory.storeWord(address, input);
    }

    private void copy(boolean f1, boolean f2, boolean f3){
        short address = memory.getAddress(pc++, f1);
        short word = memory.getWord(pc++, f2, f3);
        memory.storeWord(address, word);
    }

    private void call(boolean f1){
        //TODO
        //pc = memory.getWord(pc++, f1, false);
    }

    private void branch(boolean f1){
        pc = memory.getWord(pc++, f1, false);
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
        acc += memory.getWord(pc++, f1, f3);
    }

    private void divide(boolean f1, boolean f3) {
        acc /= memory.getWord(pc++, f1, f3);
    }

    private void load(boolean f1, boolean f3) {
        acc = memory.getWord(pc++, f1, f3);
    }

    private void mult(boolean f1, boolean f3) {
        acc *= memory.getWord(pc++, f1, f3);
    }

    private void sub(boolean f1, boolean f3) {
        acc -= memory.getWord(pc++, f1, f3);
    }

    private void store(boolean f1) {
        memory.storeWord(memory.getAddress(pc++, f1), acc);
        //short storeAddress = loadWord(f1, false);
        //memory[storeAddress] = acc;
    }

    private void stop() {
        //NaN
    }

    public void dump() {
        memory.dumpMemory();
    }

}
