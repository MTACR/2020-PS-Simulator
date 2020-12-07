package linker.auxiliar;

public enum REALLOCMODE {
    ABSOLUTE(0), RELATIVE(1);

    private final int mode;

    REALLOCMODE(int mode) {
        this.mode = mode;
    }

    public int getValue() {
        return mode;
    }
}
