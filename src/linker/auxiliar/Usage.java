package linker.auxiliar;

public class Usage {
    public String symbol;
    public int locationCounter;
    public REALLOCMODE reallocmode;
    public OPSIGN opsign;

    public Usage(String symbol, int locationCounter, REALLOCMODE reallocmode, OPSIGN opsign) {
        this.symbol = symbol;
        this.locationCounter = locationCounter;
        this.reallocmode = reallocmode;
        this.opsign = opsign;
    }
}
