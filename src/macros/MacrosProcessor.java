package macros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        private Map <String, String> parameters;
    private String []code;
    private int lineNumber;
    private MacrosExpansionData previous;

    public MacrosExpansionData(String []code, int lineNumber, MacrosExpansionData previous) {
        this.code = code;
        this.lineNumber = lineNumber;
        this.previous = previous;
        parameters = new HashMap <String, String>();
    }

    // Pega um parâmetro dessa expansão

    public String getParameter(String name) {
        return parameters.get(name);
    }

    // Coloca um parâmetro nessa expansão

    public void setParameter(String name, String value) {
        parameters.put(name, value);
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
    private FileWriter writer;
    private int lineNumber;
    private MacrosExpansionData expansionData;

    // Cria o processador de macros

    public MacrosProcessor() {
        macros = new HashMap <String, Macro>();
        lineNumber = 0;
        expansionData = null;
        reader = null;
        writer = null;
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

    private void writeLine(String line) throws IOException {
        writer.write(line + "\n");
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

                checkIfParameterNameIsValidOrError(parameter);
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

        // Converte o arrayList para o tamanho certo de array
        String[] codeArray = new String[code.size()];
        codeArray = code.toArray(codeArray);

        Macro m = new Macro(name, parameters, codeArray);

        macros.put(name, m);

        return getLineNumber();
    }

    // Pega o valor do parâmetro especificado da expansão ou joga um erro se não encontrar

    private String getParameterValueOrError(String parameter) throws MacrosProcessingError {
        // Navega na árvore dos dados de expansão, tentando achar o parâmetro
        for (MacrosExpansionData current = expansionData; current != null; current = current.getPrevious()) {
            String value = current.getParameter(parameter);
            if (value != null)
                return value;
        }

        error("Parâmetro não existe: " + parameter);
        return null;
    }

    // Verifica se o nome do parâmetro é válido

    public static boolean isParameterNameValid(String parameter) {
        return !parameter.equals("&") && parameter.indexOf(",") == -1 && parameter.indexOf(" ") == -1;
    }

    // Checa se o parâmetro é válido. Se o código passar dessa função o nome do parâmetro é válido

    private void checkIfParameterNameIsValidOrError(String parameter) throws MacrosProcessingError {
        if (!isParameterNameValid(parameter))
            error("Nome de parâmetro inválido: " + parameter);
    }

    // Substitui os parâmetros com os passados na macro. Retorna a string modificada.

    public String replaceParameters(String line) throws MacrosProcessingError {
        // Pega o parâmetro
        int andSign;

        while ((andSign = line.indexOf('&')) > -1) {
            // Calcula onde termina o parâmetro
            int end = -1;

            int comma = line.indexOf(',', andSign);
            if (comma > -1)
                end = comma;

            int space = line.indexOf(' ', andSign);
            if (end == -1 && space > -1)
                end = space;

            String parameter = null;

            if (end == -1)
                parameter = line.substring(andSign);
            else if (end > -1)
                parameter = line.substring(andSign, end);

            checkIfParameterNameIsValidOrError(parameter); // Sem if, pois se o código passar não lançou a exceção

            String firstPart = line.substring(0, andSign);
            String secondPart = end > -1 ? line.substring(end) : "";

            line = firstPart + getParameterValueOrError(parameter) + secondPart;
        }

        return line;
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

            line = replaceParameters(line);

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
        // Tira o nome da macro da linha
        int space = line.indexOf(' ');
        line = space > -1 ? line.substring(space + 1) : "";

        Macro m = (Macro)macros.get(macroName);

        String []parameters = line.split(",");

        if (parameters.length != m.getParameters().length)
            error("Número de parâmetros inválidos na chamada da macro " + macroName + ": " + parameters.length + " passados e " + m.getParameters().length + " requeridos.");

        // Cria novos dados de expansão na pilha, ligando-os ao anterior
        expansionData = new MacrosExpansionData(m.getCode(), 0, expansionData);

        // Ajusta os parâmetros
        for (int parameter = 0; parameter < m.getParameters().length; parameter ++) {
            expansionData.setParameter(m.getParameters()[parameter], parameters[parameter]);
        }

        // Processa o código na macro expandida
        doProcess();

        // Volta para a macro anterior ou para o arquivo
        expansionData = expansionData.getPrevious();
    }

    // Processa um arquivo contendo macros, gerando a saída num arquivo

    public void process(File input, File output) throws IOException, MacrosProcessingError {
        reader = new BufferedReader(new FileReader(input));
        writer = new FileWriter(output);

        doProcess();

        writer.close();

    }

    public static void main(String []args) {
        MacrosProcessor processor = new MacrosProcessor();
        try {
            processor.process(new File("input/testemacro.asm"), new File("input/testemacro.asm.proc"));
        } catch (IOException error) {
            System.out.println("Erro no arquivo: " + error.getMessage());
        } catch (MacrosProcessingError error) {
            System.out.println("Erro no processamento das macros: " +
error.getMessage());
        }
    }
}
