package linker.auxiliar;

import linker.Usage;

import java.util.ArrayList;
import java.util.Collection;

public class UsageTable extends ArrayList<Usage> {
    public UsageTable(int initialCapacity) {
        super(initialCapacity);
    }

    public UsageTable() {
    }

    public UsageTable(Collection<? extends Usage> c) {
        super(c);
    }
}
