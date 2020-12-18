package macros;

import simulator.Interface;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*class RuntimeException extends Exception {
    public RuntimeException(String message) {
        super(message);
    }
}*/

/* Classe que armazena os dados de uma expansão.
    * Ela internamente se comporta como uma pilha, o elemento que estiver em
    * expansionData é o atualmente processado e os anteriores a ele se comportam
    * como uma pilha. Isso é necessário, dentre outras coisas,  para o processador de macros saber para qual linha / código voltar
    * após terminar uma expansão. */

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

    private String getNextLine() {
        if (expansionData != null) {
            return expansionData.getNextLine();
        } else {
            lineNumber ++;
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private int getLineNumber() {
        if (expansionData != null) {
            return expansionData.getLineNumber();
        } else {
            return lineNumber;
        }
    }

// Joga um erro de processamento de macro

    private void error(String message) {//throws RuntimeException {
        // Adiciona informações de onde o erro ocorreu
        if (expansionData == null)
            message = "on the main file, line " + lineNumber + ": " + message;
        else
            message = "when expanding macro " + expansionData.getMacroName() + ", line " + expansionData.getLineNumber() + ": " + message;

        throw new RuntimeException(message);
    }

    // Tenta pegar uma linha do arquivo de entrada. Se não conseguir, joga um erro.

    private String getNextLineOrError(String errorMessage) {
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

    private int processMacro() {
        int insideMacro = 0;
        String macroDefinitionLine = getNextLineOrError("Macro definition line not found");

        String []macroParts = macroDefinitionLine.split(" ");
        int startParametersIndex = 1;

        if (macroParts.length == 0)
            error("At least the macro name was expected to create");

        String name = macroParts[0];
        String labelParameter = null;

        // Verifica se é o parâmetro da label
        if (name.startsWith("&")) {
            labelParameter = name;

            if (macroParts.length == 1)
                error("Only the macro's label parameter was specified in " + macroDefinitionLine);

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
                    error(parameter + " parameter must start with an & sign");

                checkIfParameterNameIsValidOrError(parameter);
            }
        } else
            parameters = new String [0];

        // Lê o código

        ArrayList <String> code = new ArrayList <String>(); // Para não se preocupar com ficar redimencionando o array do código

        String line;

        while ((line = getNextLineOrError("MEND expected to finish macro " + name)) != null) {
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

    private String getParameterValueOrError(String parameter) {
        MacrosExpansionData current = expansionData;

        while (current != null) {
            String value = current.getParameter(parameter);

            if (value != null)
                return value;

            current = macros.get(current.getMacroName()).getWhereWasDefined();
        }

        error("Parameter not found: " + parameter );
        return null;
    }

    // Verifica se o nome do parâmetro é válido

    public static boolean isParameterNameValid(String parameter) {
        return !parameter.equals("&") && parameter.indexOf(",") == -1 && parameter.indexOf(" ") == -1;
    }

    // Checa se o parâmetro é válido. Se o código passar dessa função o nome do parâmetro é válido

    private void checkIfParameterNameIsValidOrError(String parameter) throws RuntimeException {
        if (!isParameterNameValid(parameter))
            error("Invalid parameter name: " + parameter);
    }

    // Substitui os parâmetros com os passados na macro. Retorna a string modificada.

    public String replaceParameters(String line) {
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

    private void doProcess(String labelName) {
        String line;

        while ((line = getNextLine()) != null) {
            boolean writeLineToFile = true;

            line = line.toUpperCase().replaceAll("\\s,+"," ").trim(); //TODO funcionou?

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
                    error("Label without instructions");

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
                try {
                    if (labelName != null) {
                        writeLine(labelName + " " + line);
                        labelName = null;
                    } else
                        writeLine(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// Expande uma macro. Line é a linha com o comando de expansão e label é a label na qual essa macro deve ser expandida

    private void expandMacro(String macroName, String line, String label) {
        //Interface.instance().printMessage("Expandindo a macro " + macroName + "...");
        // Tira o nome da macro da linha
        int space = line.indexOf(' ');
        line = space > -1 ? line.substring(space + 1) : "";

        Macro m = (Macro)macros.get(macroName);

        String []parameters;

        if (!line.isEmpty())
            parameters = line.split(",");
        else
            parameters = new String[0];

        if (parameters.length != m.getParameters().length)
            error("Invalid number of parameters on call to expand macro " + macroName + ": " + parameters.length + " passed and " + m.getParameters().length + " required");

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

    public File process(File file) {
        File output = new File("tmp/" + file.getName().substring(0, file.getName().indexOf(".")) + ".proc");

        Interface.instance().printMessage("Expanding macros...");

        try {
            reader = new BufferedReader(new FileReader(file));
            writer = new FileWriter(output);

            doProcess(null);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    /*public static void main(String []args) {
        MacrosProcessor processor = new MacrosProcessor();
        processor.process(new File("input/testemacro.asm"));
    }*/

}
