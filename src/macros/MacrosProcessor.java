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

/* Classe que armazena os dados de uma expansão.
    * Ela internamente se comporta como uma pilha, o elemento que estiver em
    * expansionData é o atualmente processado e os anteriores a ele se comportam
    * como uma pilha. Isso é necessário, dentre outras coisas,  para o processador de macros saber para qual linha / código voltar
    * após terminar uma expansão. */

class MacrosExpansionData {
    private String macroName;
    private Map <String, String> parameters;
    private String []code;
    private int lineNumber;
    private MacrosExpansionData previous;

    public MacrosExpansionData(String macroName, String []code, int lineNumber, MacrosExpansionData previous) {
        this.macroName = macroName;
        this.code = code;
        this.lineNumber = lineNumber;
        this.previous = previous;
        parameters = new HashMap <String, String>();
    }

    // Pega o nome da macro sendo expandida

    public String getMacroName() {
        return macroName;
    }

    // Pega um parâmetro dessa expansão

    public String getParameter(String parameter) {
        return parameters.get(parameter);
    }

    // Coloca um parâmetro nessa expansão

    public void setParameter(String parameter, String value) {
        parameters.put(parameter, value);
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

class Macro {
    private String name;
    private String labelParameter;
    private String []parameters;
    private String[]code;
    private int expandedCount;
    private MacrosExpansionData whereWasDefined;

    public Macro(String name, String labelParameter, String []parameters, String []code, MacrosExpansionData whereWasDefined) {
        this.name = name;
        this.labelParameter = labelParameter;
        this.parameters = parameters;
        this.code = code;
        this.whereWasDefined = whereWasDefined;
        expandedCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getLabelParameter() {
        return labelParameter;
    }

    public String []getParameters() {
        return parameters;
    }

    public String []getCode() {
        return code;
    }

    // Retorna os dados de expansão de onde essa macro foi definida, para acesso a parâmetros globais

    public MacrosExpansionData getWhereWasDefined() {
        return whereWasDefined;
    }

    // Macro foi expandida. Retorna o novo valor de expansões

    public int wasExpanded() {
        return ++ expandedCount;
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
        // Adiciona informações de onde o erro ocorreu
        if (expansionData == null)
            message = "no arquivo principal, linha " + lineNumber + ": " + message;
        else
            message = "ao expandir a macro " + expansionData.getMacroName() + ", linha " + expansionData.getLineNumber() + ": " + message;

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
        int startParametersIndex = 1;

        if (macroParts.length == 0)
            error("Esperava ao menos o nome da macro a criar");

        String name = macroParts[0];
        String labelParameter = null;

        // Verifica se é o parâmetro da label
        if (name.startsWith("&")) {
            labelParameter = name;

            if (macroParts.length == 1)
                error("Apenas o r�tulo da macro foi especificado em " + macroDefinitionLine);

            labelParameter = name;
            name = macroParts[1];
            startParametersIndex = 2;
        }

        // Verifica se tem parâmetros e se eles estão certos
        String []parameters;

        if (macroParts.length == (startParametersIndex + 1)) { // Tem parâmetros
            parameters = macroParts[startParametersIndex].split(",");

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

        Macro m = new Macro(name, labelParameter, parameters, codeArray, expansionData);
        macros.put(name, m);

        return getLineNumber();
    }

    // Pega o valor do parâmetro especificado da expansão ou joga um erro se não encontrar

    private String getParameterValueOrError(String parameter) throws MacrosProcessingError {
        MacrosExpansionData current = expansionData;

        while (current != null) {
            String value = current.getParameter(parameter);

            if (value != null)
                return value;

            current = macros.get(current.getMacroName()).getWhereWasDefined();
        }

        error("Parâmetro não existe: " + parameter );
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
            int space = line.indexOf(' ', andSign);

            if (comma >= 0 && space >= 0 && comma > space)
                end = space; // Caso do parâmetro estar no início da linha
            else if (comma > -1)
                end = comma;
            else if (space > -1)
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

// Processa realmente a macro, independente se está dentro de outra ou é o arquivo puro. labelName é o nome do rótulo se deve inserir no início da primeira linha de código.

    private void doProcess(String labelName) throws MacrosProcessingError, IOException {
        String line;

        while ((line = getNextLine()) != null) {
            boolean writeLineToFile = true;

            if (line.toUpperCase().equals("MACRO")) {
                writeLineToFile = false;
                processMacro();
                continue;
            }

            boolean hasLabel = line.startsWith("&");

            line = replaceParameters(line);

            int space = line.indexOf(" ");
            String macroLine = line; // Tenta identificar a macro a partir da linha original
            String label = null;

            if (hasLabel) {
                if (space == -1)
                    error("Rótulo sem instrução");

                label = line.substring(0, space);
                macroLine = line.substring(space + 1); // Tira o nome da label do nome da macro
            }

            for (String macroName: macros.keySet()) {
                if (macroLine.startsWith(macroName + " ") || macroLine.equals(macroName)) {
                    writeLineToFile = false;
                    expandMacro(macroName, macroLine, label);
                    break;
                }
            }

            if (writeLineToFile) {
                if (labelName != null) {
                    writeLine(labelName + " " + line);
                    labelName = null;
                } else
                    writeLine(line);
            }
        }
    }

// Expande uma macro. Line é a linha com o comando de expansão e label é a label na qual essa macro deve ser expandida

    private void expandMacro(String macroName, String line, String label) throws MacrosProcessingError, IOException {
        System.out.println("Expandindo a macro " + macroName + "...");
        // Tira o nome da macro da linha
        int space = line.indexOf(' ');
        line = space > -1 ? line.substring(space + 1) : "";

        Macro m = (Macro)macros.get(macroName);

        String []parameters = line.split(",");

        if (parameters.length != m.getParameters().length)
            error("Número de parâmetros inválidos na chamada da macro " + macroName + ": " + parameters.length + " passados e " + m.getParameters().length + " requeridos.");

        // Cria novos dados de expansão na pilha, ligando-os ao anterior
        expansionData = new MacrosExpansionData(macroName, m.getCode(), 0, expansionData);

        // Ajusta os parâmetros
        for (int parameter = 0; parameter < m.getParameters().length; parameter ++) {
            expansionData.setParameter(m.getParameters()[parameter], parameters[parameter]);
        }

        if (m.getLabelParameter() != null) {
            if (label != null) // Se o programador forneceu um nome para o rótulo
                expansionData.setParameter(m.getLabelParameter(), label);
            else // Coloca o nome do rótulo com o número de vezes que a macro foi expandida
                expansionData.setParameter(m.getLabelParameter(), macroName + "_exp" + m.wasExpanded());
        }
        // Processa o código na macro expandida
        doProcess(label);

        // Volta para a macro anterior ou para o arquivo
        expansionData = expansionData.getPrevious();
    }

    // Processa um arquivo contendo macros, gerando a saída num arquivo

    public void process(File input, File output) throws IOException, MacrosProcessingError {
        reader = new BufferedReader(new FileReader(input));
        writer = new FileWriter(output);

        doProcess(null);

        writer.close();

    }

    public static void main(String []args) {
        MacrosProcessor processor = new MacrosProcessor();
        try {
            processor.process(new File("input/testemacro.asm"), new File("input/testemacro.asm.proc"));
        } catch (IOException error) {
            System.out.println("Erro no arquivo: " + error.getMessage());
        } catch (MacrosProcessingError error) {
            System.out.println("Erro no processamento das macros: " + error.getMessage());
        }
    }
}
