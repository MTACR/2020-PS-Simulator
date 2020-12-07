package linker;

public class Linker {
    //Tabela de Definição: Lista cada símbolo global definido internamente (Construída pelo montador)
    // Símbolo
    // Endereço
    // Modo de relocabilidade (relativo ou absoluto)

    //Tabela de Uso: Lista cada uso interno de um símbolo global (Construída pelo montador)
    // Símbolo
    // Location counter (Endereço relativo) do campo de operandos
    // Modo de relocabilidade (relativo ou absoluto)
    // Sinal da operação

    //Tabela de Símbolos Globais (TSG): Armazena todos os símbolos globais definidos. União das tabelas de definição dos diferentes segmentos.
    // Símbolo
    // Endereço
    // Modo de relocabilidade (relativo ou absoluto)

}
