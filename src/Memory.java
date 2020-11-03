import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Memory {

    private final short[] memory;
    private final boolean[] debug; // usado pra identificar se é opcode ou dado

    public Memory(int size) {
        memory = new short[size];
        debug = new boolean[size];
    }

    // Carrega o arquivo passado por parâmetro para a memória a partir da última posição ocupada pela pilha.
    // Faz as correções necessárias de endereçamento.
    public short loadFileToMemory(File file) {
        short re = 0;
        // Vetor com os modos de endereçamento de cada instrução, índice = opcode
        int[] opMode = new int[] {1,1,2,2,1,1,2,1,2,0,2,0,1,3,2,1};
        // Modos de endereçamento:
        // 0 = Sem operando / Nenhum endereço.
        // 1 = Direto ou Indireto
        // 2 = Direto, Indireto ou Imediato.
        // 3 = Dois operandos, usado somente pelo COPY.

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;

            //short stackSize = (short) (memory[2] + 3); // valor armazenado + espaços livres no inicio
            //-----------------------------------
            short stackSize = (short) 3; //MUDAR DEPOIS DE PILHA PRONTA!
            //-----------------------------------
            //pc = (short) (stackSize + 1);

            //primeira word é o endereço da memória de dados
            if ((line = reader.readLine()) != null) {
                short word = (short) (Integer.parseInt(line, 2) + stackSize);
                memory[i + stackSize] = word;
                re = word;
                i++;
            }

            //System.out.println(re);

            //lê até acabar memória de dados
            while ((line = reader.readLine()) != null && (i + stackSize) < re) {
                short word = (short) Integer.parseInt(line, 2);
                int opCode = (word & 0xF000) >> 12;
                int mode = opMode[opCode];
                boolean immediate = (word & 0x200) >> 9  != 0;

                //System.out.println(OPCODE.values()[opCode]);

                memory[i + stackSize] = word;   // Endereço = Tamanho da pilha + contador de palavra
                switch (mode) {
                    case 0: // Sem operador
                        // do nothing
                        break;
                    case 1: // Direto, Indireto
                        i++;
                        memory[i + stackSize] = (short) (getNextWord(reader) + stackSize);
                        break;
                    case 2: // Direto, Indireto, Imediato
                        i++;
                        if (immediate){
                            memory[i + stackSize] = getNextWord(reader);
                        } else {
                            memory[i + stackSize] = (short) (getNextWord(reader) + stackSize);
                        }   break;
                    case 3: // COPY -> 2 operandos
                        i++;
                        memory[i + stackSize] = (short) (getNextWord(reader) + stackSize); // op1 Direto, Indireto
                        i++;
                        if (immediate){ // op2 pode ser imediato, nesse caso não corrije endereço
                            memory[i + stackSize] = getNextWord(reader); // op2 Imediato
                        } else {
                            memory[i + stackSize] = (short) (getNextWord(reader) + stackSize); // op2 Direto, Indireto
                        }   break;
                    default:
                        break;
                }
                i++;
            }

            //lê até o fim do arquivo apenas dados
            while (line != null) {
                memory[i + stackSize] = (short) Integer.parseInt(line, 2);
                i++;

                line = reader.readLine();
                //System.out.println(memory[i + stackSize]);
            }

            reader.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return re;
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
        short s = memory[pos];

        System.out.print(" " + s);

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
        System.out.println("---DUMP---");

        for (int j = 0; j < 20; j++)
            if (debug[j] )
                System.out.println(j + " - " + TestOnly.OPCODE.values()[(short) ((memory[j] & 0xF000) >> 12)]);
            else
                System.out.println(j + " - " + memory[j]);
    }

    public void setDebug(int pos) {
        debug[pos] = true;
    }

}
