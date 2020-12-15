package linker;

public class Line {
    /*public int address;*/ //Pode ser deduzida da posição no vetor
    public int word;
    public char reallocMode;
    /*public boolean instruction;*/ //Inutil

    public Line(int word, char reallocMode) {
        this.word = word;
        this.reallocMode = reallocMode;
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
