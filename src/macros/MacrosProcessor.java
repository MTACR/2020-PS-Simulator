package macros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class MacrosProcessingError extends Exception {
    public MacrosProcessingError(String message) {
        super(message);
    }
}

class Macro {
    private String name;
    private String []parameters;
    private String[]code;

    public Macro(String name, String []parameters, String []code) {
        this.name = name;
        this.parameters = parameters;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String []getParameters() {
        return parameters;
    }

    public String []getCode() {
        return code;
    }
}

/* Classe que armazena os dados de uma expansão.
    * Ela internamente se comporta como uma pilha, o elemento que estiver em
    * expansionData é o atualmente processado e os anteriores a ele se comportam
    * como uma pilha. Isso é necessário, dentre outras coisas,  para o processador de macros saber para qual linha / código voltar
    * após terminar uma expansão. */

class MacrosExpansionData {
    String []code;
    int lineNumber;
    MacrosExpansionData previous;

    public MacrosExpansionData(String []code, int lineNumber, MacrosExpansionData previous) {
        this.code = code;
        this.lineNumber = lineNumber;
        this.previous = previous;
    }

    // Pega o código associado a esse dado de expansão

    public String []getCode() {
        return code;
    }

    // Pega o número da linha que está sendo processada pelo processador de macros

    public int getLineNumber() {
        return lineNumber;
    }

    // Pega o elemento anterior da pilha de dados de expansão

    public MacrosExpansionData getPrevious() {
        return previous;
    }

    // Usado internamente: pega a próxima linha do código

    public String getNextLine() {
        try {
            return code[lineNumber ++];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}

public class MacrosProcessor {
    protected Map <String, Macro> macros; // Mapa que armazenará as macros já definidas
    private BufferedReader reader;
    private int lineNumber;
    private MacrosExpansionData expansionData;

    // Cria o processador de macros

    public MacrosProcessor() {
        macros = new HashMap <String, Macro>();
        lineNumber = 0;
        expansionData = null;
    }

// Pega a próxima linha do arquivo

    private String getNextLine() throws IOException {
        if (expansionData != null) {
            return expansionData.getNextLine();
        } else {
            lineNumber ++;
            return reader.readLine();
        }
    }

    private int getLineNumber() {
        if (expansionData != null) {
            return expansionData.getLineNumber();
        } else {
            return lineNumber;
        }
    }

// Joga um erro de processamento de macro

    private void error(String message) throws MacrosProcessingError {
        throw new MacrosProcessingError(message);
    }

    // Tenta pegar uma linha do arquivo de entrada. Se não conseguir, joga um erro.

    private String getNextLineOrError(String errorMessage) throws MacrosProcessingError, IOException {
        String line = getNextLine();
        if (line == null) {
            error(errorMessage);
        }
        return line;
    }

    // Escreve uma linha no arquivo que terá a saída do processador de macros (por enquanto imprime na tela)

    private void writeLine(String line) {
        System.out.println(line);
    }

// Processa uma definição de macro completa. Retorna a linha do MEND em caso de sucesso.

    private int processMacro() throws MacrosProcessingError, IOException {
    int insideMacro = 0;
        String macroDefinitionLine = getNextLineOrError("Não encontrou a linha com a definição da macro");

        String []macroParts = macroDefinitionLine.split(" ");

        if (macroParts.length == 0)
            error("Esperava ao menos o nome da macro a criar");

        String name = macroParts[0];

        // Verifica se tem parâmetros e se eles estão certos
        String []parameters;

        if (macroParts.length == 2) { // Tem parâmetros
            parameters = macroParts[1].split(",");

            for (String parameter: parameters) {
                if (!parameter.startsWith("&"))
                    error("Parâmetro " + parameter + " deve começar com &");
            }
        } else
            parameters = new String [0];

        // Lê o código

        ArrayList <String> code = new ArrayList <String>(); // Para não se preocupar com ficar redimencionando o array do código

        String line;

        while ((line = getNextLineOrError("Esperava o MEND para terminar a macro " + name)) != null) {
            // Processa macros aninhadas
            if (line.toUpperCase().equals("MEND")) {
                if (insideMacro > 0) {
                    code.add("MEND");
                    insideMacro --;
                    continue;
                }
                break;
            } else {
                code.add(line);
                if (line.toUpperCase().equals("MACRO"))
                    insideMacro ++;
            }
        }

        System.out.println("Criando macro " + name + " com parâmetros " + Arrays.toString(parameters) + " e código " + code);

        // Converte o arrayList para o tamanho certo de array
        String[] codeArray = new String[code.size()];
        codeArray = code.toArray(codeArray);

        Macro m = new Macro(name, parameters, codeArray);

        macros.put(name, m);

        return getLineNumber();
    }

// Processa realmente a macro, independente se está dentro de outra ou é o arquivo puro

    private void doProcess() throws MacrosProcessingError, IOException {
        String line;

        while ((line = getNextLine()) != null) {
            boolean writeLineToFile = true;

            if (line.toUpperCase().equals("MACRO")) {
                writeLineToFile = false;
                processMacro();
                continue;
            }

            for (String macroName: macros.keySet()) {
                if (line.startsWith(macroName + " ") || line.equals(macroName)) {
                    writeLineToFile = false;
                    expandMacro(macroName, line);
                    break;
                }
            }

            if (writeLineToFile)
                writeLine(line);
        }
    }

// Expande uma macro. Line é a linha com o comando de expansão

    private void expandMacro(String macroName, String line) throws MacrosProcessingError, IOException {
        System.out.println("Expandindo a macro " + macroName);

        Macro m = (Macro)macros.get(macroName);

        // TODO Verificar parâmetros e expandir a macro

        // Cria novos dados de expansão na pilha, ligando-os ao anterior
        expansionData = new MacrosExpansionData(m.getCode(), 0, expansionData);

        // Processa o código na macro expandida
        doProcess();

        // Volta para a macro anterior ou para o arquivo
        expansionData = expansionData.getPrevious();
    }

    // Processa um arquivo contendo macros

    public void process(File input) throws IOException, MacrosProcessingError {
        reader = new BufferedReader(new FileReader(input));

        doProcess();
    }

    public static void main(String []args) {
        MacrosProcessor processor = new MacrosProcessor();
        try {
            processor.process(new File("input/testemacro.asm"));
        } catch (IOException error) {
            System.out.println("Erro no arquivo: " + error.getMessage());
        } catch (MacrosProcessingError error) {
            System.out.println("Erro no processamento das macros: " +
error.getMessage());
        }
    }
}
