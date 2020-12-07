package linker.auxiliar;

public enum OPSIGN {
    MINUS('-'), RELATIVE('+');

    private final char sign;

    OPSIGN(char mode) {
        this.sign = mode;
    }

    public char getValue() {
        return sign;
    }
}
