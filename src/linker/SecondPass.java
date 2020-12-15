package linker;

import linker.auxiliar.DefinitionTable;
import simulator.Interface;

import java.util.ArrayList;

public class SecondPass {
    //Atualiza os endereços para refletir a união dos segmentos, já define o inicio do programa (com base no primeiro endereço do primeiro segmento) e seta o tamanho da pilha
    public static ArrayList<Line> updateAddresses(ArrayList<Segment> segments, int offset){
        ArrayList<Line> updated = new ArrayList<>();

        for (int i = 0; i < offset; i++){
            updated.add(new Line(0, 'a'));   //Cria o cabeçalho e a pilha
        }
        updated.get(0).word = offset;       //Armazena o inicio do programa na posição 0 Necessario?. Na verdade, não dá pra deduzir pelo tamanho da pilha
        updated.get(2).word = offset - 3;   //Armazena o tamanho da pilha no endereço 2

        for(Segment seg : segments){

            for(Usage usage : seg.usageTable){
                usage.offset(offset);
            }

            for(Line line : seg.lines){
                line.offset(offset);
                updated.add(line);
            }
            offset += seg.length();
        }

        return updated;
    }

    //Atualizar as referências externas baseado na TSG e tabelas
    //de uso (o endereço é atualizado)
    public static void updateReferences(ArrayList<Line> lines, ArrayList<Segment> segments, DefinitionTable tgs){
        for(Segment seg : segments){
            for(Usage use : seg.usageTable){
                Definition def = tgs.get(use.symbol);
                Line line = lines.get(use.locationCounter);

                try {
                    if (use.opsign == '+') {
                        line.word = def.address + line.word;
                    } else {
                        line.word = def.address - line.word;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Interface.instance().printMessage("Undefined Symbol in " + seg.fileName + ": " + use.symbol + "'s definition not found");
                    Interface.instance().printMessage("Ou pode ser dado algum problema da linha não existir");
                }
            }
        }
    }
}
