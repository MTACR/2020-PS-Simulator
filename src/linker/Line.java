package linker;

public class Line {
    /*public int address;*/ //Pode ser deduzida da posição no vetor
    public int word;
    public char reallocMode;
    public boolean instruction;

    public Line(int word, char reallocMode, boolean instruction) {
        this.word = word;
        this.reallocMode = reallocMode;
        this.instruction = instruction;
    }

    public void offset(int offset){
        if(reallocMode == 'r'){
            word += offset;
        }
    }

    @Override
    public String toString() {
        return word +
                " " + reallocMode;
    }
}
