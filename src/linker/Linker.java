package linker;

import linker.auxiliar.Definition;
import linker.auxiliar.Segment;
import linker.auxiliar.Usage;

import java.util.ArrayList;

public class Linker {
    ArrayList<Segment> segments;
    ArrayList<Usage> usageTable;    //Tabela de Uso: Lista cada uso interno de um símbolo global
    ArrayList<Definition> tgs;      //Tabela de Símbolos Globais (TSG): Armazena todos os símbolos globais definidos. União das tabelas de definição dos diferentes segmentos.




}
