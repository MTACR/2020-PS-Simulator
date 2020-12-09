package linker;

import assembler.ObjectCode;

import java.util.ArrayList;

public class SecondPass {
    //Atualiza os endereços para refletir a união dos segmentos
    public static ArrayList<ObjectCode> updateAddresses(ArrayList<Segment> segments){
        ArrayList<ObjectCode> updated = new ArrayList<>();

        Segment seg = segments.get(0);
        updated = (ArrayList<ObjectCode>) seg.lines.clone();
        int offset = seg.length;

        for(int i = 1; i < segments.size(); i++){
            seg = segments.get(i);

            //TODO: offset da usageTable

            for(ObjectCode oc : seg.lines){
                oc.offset(offset);
                updated.add(oc);
            }
            offset += seg.length;
        }

        return updated;
    }
}
