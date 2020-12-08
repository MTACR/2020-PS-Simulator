package linker;

public class Definition {
    public String symbol;
    public int address;
    public char reallocmode;

    public Definition(String symbol, int address, char reallocmode) {
        this.symbol = symbol;
        this.address = address;
        this.reallocmode = reallocmode;
    }

    public void offset(int offset){
        if(reallocmode == 'r'){
            address += offset;
        }
    }

    @Override
    public String toString() {
        return "Definition {" +
                "symbol = '" + symbol + '\'' +
                ", address = " + address +
                ", reallocmode = " + reallocmode +
                '}';
    }
}
