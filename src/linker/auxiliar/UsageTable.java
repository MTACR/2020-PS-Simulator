package linker.auxiliar;

import linker.Usage;

import java.util.HashMap;
import java.util.Map;

public class UsageTable extends HashMap<String, Usage> {
    public UsageTable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public UsageTable(int initialCapacity) {
        super(initialCapacity);
    }

    public UsageTable() {
    }

    public UsageTable(Map<? extends String, ? extends Usage> m) {
        super(m);
    }
}
