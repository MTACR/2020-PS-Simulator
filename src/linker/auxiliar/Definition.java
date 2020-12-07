package linker.auxiliar;

public class Definition {
    public String symbol;
    public int address;
    public REALLOCMODE reallocmode;

    public Definition(String symbol, int address, REALLOCMODE reallocmode) {
        this.symbol = symbol;
        this.address = address;
        this.reallocmode = reallocmode;
    }
}
