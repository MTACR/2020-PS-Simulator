import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Memory {

    private final short[] memory;
    private final boolean[] debug; // usado pra identificar se é opcode ou dado
    private int sp; //stackpointer

    private final int stackZero = 2; //posição anterior ao inicio da pilha //definidos assim pra facilitar possiveis modificações
    private final short stackSize = 10; //tamanho maximo da pilha

    public Memory(int size) {
        memory = new short[size];
        debug = new boolean[size];
        sp = stackZero;
        memory[stackZero] = stackZero + stackSize; //usar memory[2] para obter o endereço limite da pilha
    }

    //Pilha

    public void push(short word){ //insere na pilha
        sp++;
        if(sp > memory[2]){ //verifica se a pilha está cheia
            System.out.println("Stack overflow");
            sp = 0; //"causando um desvio para o endereço 0 (zero)"
        }
        memory[sp] = word;
    }

    public short pop() { //retira da pilha
        if(sp <= stackZero){ //verifica se a pilha está vazia
            System.out.println("Stack underflow");
            sp = memory[2];
        }
        return memory[sp--];
    }

    public short getPcStart(){
        return (short) (memory[2] + 1);
    }

    /*public boolean isFull(){ //confere se pilha cheia
        return (sp==12);
    }

    public boolean isEmpty(){ //confere se pilha vazia
        return (sp==2);
    }*/

    //Fim pilha

    // Carrega o arquivo passado por parâmetro para a memória a partir da última posição ocupada pela pilha.
    // Faz as correções necessárias de endereçamento.
    // Retorna a posição da área de dados (0 se der erro)
    public void loadFileToMemory(File file) {
        // Vetor com os modos de endereçamento de cada instrução, índice = opcode
        int[] opMode = new int[] {1,1,2,2,1,1,2,1,2,0,2,0,1,3,2,1};
        // Modos de endereçamento:
        // 0 = Sem operando / Nenhum endereço.
        // 1 = Direto ou Indireto
        // 2 = Direto, Indireto ou Imediato.
        // 3 = Dois operandos, usado somente pelo COPY.
        
        // Pilha para endereços de operadores indiretos, para corrigir depois de todo o arquivo lido
        Stack indStack = new Stack<Short>(); 
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            //short stackSize = (short) (memory[2] + 3); // valor armazenado + espaços livres no inicio
            //-----------------------------------
            short stackSize = (short) 0; //TODO: MUDAR DEPOIS DE PILHA PRONTA!
            //-----------------------------------
            //pc = (short) (stackSize + 1);
            
            int i = stackSize;
            
            
            //primeira word é o endereço da memória de dados
            if ((line = reader.readLine()) != null) {
                short word = (short) (Integer.parseInt(line, 2) + stackSize);
                memory[i] = word;
                i++;
            }

            //System.out.println(re);

            //lê até acabar memória de dados
            while ((line = reader.readLine()) != null) {
                short word = (short) Integer.parseInt(line, 2);
                int opCode = (word & 0xF000) >> 12;
                int mode = opMode[opCode];
                boolean immediate = (word & 0x200) >> 9  != 0;
                boolean op1Ind = (word & 0x800) >> 11 != 0;
                boolean op2Ind = (word & 0x400) >> 10 != 0;


                //System.out.println(OPCODE.values()[opCode]);

                memory[i] = word;   // Endereço = Tamanho da pilha + contador de palavra
                switch (mode) {
                    case 0: // Sem operador
                        // do nothing
                        break;
                    case 1: // Direto, Indireto
                        i++;
                        memory[i] = (short) (getNextWord(reader) + stackSize);
                        if (op1Ind) indStack.push(memory[i]);
                        break;
                    case 2: // Direto, Indireto, Imediato
                        i++;
                        if (immediate){
                            memory[i] = getNextWord(reader);
                        } else {
                            memory[i] = (short) (getNextWord(reader) + stackSize);
                            if (op1Ind) indStack.push(memory[i]); // Se o operador for indireto, é necessario corrigir o endereço o qual foi apontado também;
                        }   break;
                    case 3: // COPY -> 2 operandos
                        i++;
                        memory[i] = (short) (getNextWord(reader) + stackSize); // op1 Direto, Indireto
                        if (op1Ind) indStack.push(memory[i]);
                        i++;
                        if (immediate){ // op2 pode ser imediato, nesse caso não corrije endereço
                            memory[i] = getNextWord(reader); // op2 Imediato
                        } else {
                            memory[i] = (short) (getNextWord(reader) + stackSize); // op2 Direto, Indireto
                            if (op2Ind) indStack.push(memory[i]);
                        }   break;
                    default:
                        break;
                }
                i++;
            }

            //lê até o fim do arquivo apenas dados
            while (line != null) {
                memory[i] = (short) Integer.parseInt(line, 2);
                i++;

                line = reader.readLine();
                //System.out.println(memory[i + stackSize]);
            }
            reader.close();
            
            while (!indStack.empty()){ // Acessa a pilha com os endereços indiretos e corrige com o tamanho da pilha da máquina
                short address = (short) indStack.pop();
                short value = memory[address];
                if (value != 0)  memory[address] = (short) (value + stackSize);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private short getNextWord(BufferedReader reader) throws IOException {
        String line;
        line = reader.readLine();
        return (short) Integer.parseInt(line,2);
    }

    public void storeWord(short pos, short word) {
        memory[pos] = word;
    }

    // retorna um endereço da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto
    public short getAddress(int pos, boolean f1) {
        if (pos > memory.length)
            throw new RuntimeException("Out of bounds");

        short s = memory[pos];

        if (f1) {
            return memory[s];
        } else {
            return s;
        }
    }

    // retorna um valor da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto e f3 diz se é imediato
    public short getWord(int pos, boolean f1, boolean f3) {
        if (pos > memory.length)
            throw new RuntimeException("Out of bounds");

        short s = memory[pos];

        //System.out.print(" " + s);

        if (f3) {
            return s;
        } else if (f1) {
            short contentsAddress = memory[s];
            return memory[contentsAddress];
        } else {
            return memory[s];
        }
    }

    // imprime o conteudo da memória
    public void dumpMemory() {
        //System.out.println("---DUMP---");

        for (int j = 0; j < 20; j++)
            if (debug[j])
                System.out.println("0x" + j + " -\t" + TestOnly.OPCODE.values()[(short) ((memory[j] & 0xF000) >> 12)]);
            else
                System.out.println("0x" + j + " -\t" + memory[j]);
    }

    public void setDebug(int pos) {
        debug[pos] = true;
    }

    public int size() {
        return memory.length;
    }

}
