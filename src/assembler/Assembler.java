package assembler;

import linker.Linker;
import macros.MacrosProcessor;
import simulator.Interface;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Assembler {

    public static File assemble(File[] files) {
        List<File> files2Link = new ArrayList<>();

        for (File file : files) {
            try {
                files2Link.add(SecondPass.pass(new MacrosProcessor().process(file)));
            } catch (RuntimeException e) {
                Interface.instance().printError(e.getMessage());
            }
        }

        return Linker.link(files2Link);
    }

	// ------------------------------------------------
	// CÓDIGO VELHO ABAIXO
	// ------------------------------------------------
    /*
	
	//retorna o modo de endereçamento (0 p/ direto, 1 p/ indireto ou 2 p/ imediato)
	//no assembly, direto é '$' antes da posição; indireto é '*' antes da posição; imediato é só o numero.
	private static int getAddrMode(String opd) {
		if (opd.charAt(0) == '#') 		return 2; //imediato
		else if (opd.charAt(0) == 'I') 	return 1; //indireto
		else 							return 0; //direto
	}
	
	//recebe o numero da operação, retorna o binário
	private static String opcodeBinary(int opcode, int addrOpd1, int addrOpd2) {
		String opcodeBin = Integer.toBinaryString(opcode);
		opcodeBin = fillBinary(opcodeBin, 4, 'l');
		
		//adicionar mais um bit porque a descrição do trabalho tá confusa
		opcodeBin = '0' + opcodeBin;
		
		//bit 5
		if (addrOpd1 == 1) opcodeBin = '1' + opcodeBin; 
		else opcodeBin = '0' + opcodeBin;
		
		//bit 6
		if (addrOpd2 == 1) opcodeBin = '1' + opcodeBin; 
		else opcodeBin = '0' + opcodeBin;
		
		//bit 7
		if (addrOpd1 == 2 || addrOpd2 == 2) opcodeBin = '1' + opcodeBin; 
		else opcodeBin = '0' + opcodeBin;
		
		opcodeBin = fillBinary(opcodeBin, 16, 'l');
		
		return opcodeBin + "\n";
	}
	
	//recebe o operando (incluindo o char de tipo de endereçamento) e retorna o binário
	private static String opdBinary(String opd) {
		if (opd.charAt(0) == '#' || opd.charAt(0) == 'I') opd = opd.substring(1);
		
		String opdBin = Integer.toBinaryString(Integer.parseInt(opd));
		opdBin = fillBinary(opdBin, 16, 'l');
		
		return opdBin + '\n';
	}
	
	// size = num bits final; 
	//fill = qual lado adicionar os 0's; 'l' pra inicio, 'r' pra fim
	private static String fillBinary(String numBinary, int size, char fill) {
		String aux = "";
		
		while (numBinary.length() + aux.length() < size) aux += '0';

		if (fill == 'l') return aux + numBinary;
		else return numBinary + aux;
	}
	
	private static String dataToBinary(String data) {
		int dataInt = Integer.parseInt(data);

		if (dataInt > Short.MAX_VALUE)
			throw new RuntimeException("Valor fora dos limites");

		String strBin = Integer.toBinaryString(dataInt);
		
		strBin = fillBinary(strBin, 16, 'l');
		
		return strBin + "\n";
	}*/
}
