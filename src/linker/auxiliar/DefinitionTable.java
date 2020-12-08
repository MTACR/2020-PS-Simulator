package linker.auxiliar;

import linker.Definition;

import java.util.HashMap;
import java.util.Map;

public class DefinitionTable extends HashMap<String, Definition> {
    public DefinitionTable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public DefinitionTable(int initialCapacity) {
        super(initialCapacity);
    }

    public DefinitionTable() {
    }

    public DefinitionTable(Map<? extends String, ? extends Definition> m) {
        super(m);
    }
}
