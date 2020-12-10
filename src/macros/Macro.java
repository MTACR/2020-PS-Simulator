package macros;

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
