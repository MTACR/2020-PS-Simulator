package macros;

import java.util.HashMap;
import java.util.Map;

class MacrosExpansionData {
    private String macroName;
    private Map<String, String> parameters;
    private String []code;
    private int lineNumber;
    private MacrosExpansionData previous;

    public MacrosExpansionData(String macroName, String []code, int lineNumber, MacrosExpansionData previous) {
        this.macroName = macroName;
        this.code = code;
        this.lineNumber = lineNumber;
        this.previous = previous;
        parameters = new HashMap<String, String>();
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
