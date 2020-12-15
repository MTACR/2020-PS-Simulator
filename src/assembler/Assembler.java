package assembler;

import linker.Linker;
import macros.MacrosProcessor;
import simulator.Interface;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Assembler {

    public static File assemble(List<File> files) throws RuntimeException {
        List<File> files2Link = new ArrayList<>();

        for (File file : files) {
            Interface.instance().printMessage("Montando arquivo " + file.getName());
            files2Link.add(SecondPass.pass(new MacrosProcessor().process(file)));
        }

        return Linker.link(files2Link);
    }

}
