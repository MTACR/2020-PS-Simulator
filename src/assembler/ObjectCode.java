package assembler;

import javafx.util.Pair;
import java.util.List;

public class ObjectCode {
    public int address;
    public int size;
    public List<Pair<Integer, Character>> words;

    public ObjectCode(int address, int size, List<Pair<Integer, Character>> words) {
        this.address = address;
        this.size = size;
        this.words = words;
    }

    public String printWords() {
        String out = "";

        for (int i = 0; i < size; i++) {
            out += words.get(i).getKey() + " " + words.get(i).getValue() + " ";
        }

        return out;
    }

    public void offset(int offset){
        address += offset;

        for(int i = 0; i < words.size(); i++){
            Pair<Integer, Character> currentWord = words.get(i);

            if("r".equals(currentWord.getValue())){ //Fui obrigado a recorrer a gambiarra
                Pair<Integer, Character> newWord = new Pair<Integer, Character>(currentWord.getKey() + offset, currentWord.getValue());
                words.set(i, newWord);
            }
        }
    }

    @Override
    public String toString() {
        return address +
                " " + size +
                " " + words;
    }
}


