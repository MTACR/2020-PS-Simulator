package simulator;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Processor {

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

        public int getValue() {
            return op;
        }
    }

    private static final short MEMORY_SIZE = 1024;

    // Registradores da maquina
    private short pc;
    private short acc;
    private short ri;
    private short re;
    private byte mop;

    private final Memory memory;
    //private final Interface gui;
    private OnStep step;
    private OnStop stop;

    // Função reflexiva, faz parte da integração com a interface, permitindo a alternancia entre nextInstruction() e parseOpCode()
    public interface OnStep {

        void onStep();
    }

    public interface OnStop {

        void onStop();
        void onFail();
    }

    public void step() {
        step.onStep();
    }

    // Construtores
    public Processor(File file, OnStop onStop) {
        memory = new Memory(MEMORY_SIZE);
        memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados
        pc = memory.firstPosition();
        step = this::nextInstruction;
        stop = onStop;
    }

    // Determina a proxima instrução
    private void nextInstruction() {
        //if (ri != 11) { // Se ri=11(STOP), parar execução
        if (pc == 0) {
            stop.onFail();
            throw new RuntimeException("Stack overflow");
        }

        if (pc > memory.size() || pc < 0) {
            throw new RuntimeException("Program counter out of memory bounds");
        }

        ri = memory.getWord(pc++, false, true);

        step = this::parseOpCode;
    }

    // Seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private void parseOpCode() {
        boolean f1 = (ri & 32) != 0;
        boolean f2 = (ri & 64) != 0;
        boolean f3 = (ri & 128) != 0;

        OPCODE opcode = OPCODE.values()[ri & 15];

        step = this::nextInstruction;

        switch (opcode) {
            case BR:
                branch(f1);
                break;
            case BRPOS:
                branchPos(f1);
                break;
            case ADD:
                add(f1, f3);
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
                break;
            case STOP:
                stop.onStop();
                break;
            case READ:
                read(f1);
                break;
            case COPY:
                copy(f1, f2, f3);
                break;
            case MULT:
                mult(f1, f3);
                break;
            case CALL:
                call(f1);
                break;
            default:
                throw new RuntimeException("Invalid opcode");
        }
    }

    // Carrega um enderço e coloca o valor na interface gráfica
    private void write(boolean f1, boolean f3) {
        short word = memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
        Interface.instance().setOutputLabel(word);
    }

    // Carrega o endereço do operando e abre uma janela com um campo para a entrada,
    // salva o valor lido no endereço do operando.
    private void read(boolean f1) {
        short address = memory.getWord(pc++, f1, false);
        re = memory.getAccessed();
        Short input = null;
        do {
            try {
                String in = JOptionPane.showInputDialog("Insira a entrada");
                if (in != null) {
                    input = Short.parseShort(in);
                    memory.storeWord(address, input);
                } else {
                    stop.onFail();
                    break;
                }
            } catch (NumberFormatException ex) {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "A entrada deve ser do tipo Short! (-32,768 até 32,767)", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (input == null);
    }

    // Copia um dado de um endereço da memória a outro
    private void copy(boolean f1, boolean f2, boolean f3) {
        short address = memory.getWord(pc++, f1, false);
        re = memory.getAccessed();

        short word = memory.getWord(pc++, f2, f3);
        re = memory.getAccessed();

        memory.storeWord(address, word);
    }

    // Empilha o PC atual e vai para o inicio da função/endereço determinado. Em caso de falha (Stack overflow), para o programa
    private void call(boolean f1) {
        memory.push((short) (pc + 1));
        pc = memory.getWord(pc++, f1, false);
        re = memory.getAccessed();
    }

    // Desempilha um valor para o PC. Em caso de falha (Stack Underflow), o programa para.
    private void ret() {
        //short pop = memory.pop();
        // if (pop >= 0) {
        pc = memory.pop();
    }

    // Desvio de fluxo incondicional
    private void branch(boolean f1) {
        pc = memory.getWord(pc, f1, false);
        re = memory.getAccessed();
    }

    // Desvios de fluxo condicionais
    private void branchNeg(boolean f1) {
        if (acc < 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    private void branchPos(boolean f1) {
        if (acc > 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    private void branchZero(boolean f1) {
        if (acc == 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    // Operações aritméticas
    private void add(boolean f1, boolean f3) {
        acc += memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void divide(boolean f1, boolean f3) {
        acc /= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void mult(boolean f1, boolean f3) {
        acc *= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void sub(boolean f1, boolean f3) {
        acc -= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    // Operações de memória
    // Carrega um valor da memória para o acumulador
    private void load(boolean f1, boolean f3) {
        acc = memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    // Armazena o valor do acumulador na memória
    private void store(boolean f1) {
        short address = memory.getWord(pc++, f1, false);
        re = memory.getAccessed();
        memory.storeWord(address, acc);
    }

    /*public void dump() {
        System.out.println("ACC -\t" + acc);
        System.out.println("RE -\t" + re);
        System.out.println("RI -\t" + ri);
        System.out.println("PC -\t" + pc);
        System.out.println("------------");
        memory.dumpMemory();
    }*/
    //Getters para a interface
    public short getPc() {
        return pc;
    }

    public short getSp() {
        return memory.getSp();
    }

    public short getAcc() {
        return acc;
    }

    public short getRi() {
        return ri;
    }

    public short getRe() {
        return re;
    }

    public byte getMop() {
        return mop;
    }

    public short[] getMemory() {
        return memory.getMemory();
    }

}
