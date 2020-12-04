import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class Assembler {
	
	//transforma o opcode de String pra inteiro
	private static int getOpcode(String opcode) {
		switch (opcode.toLowerCase()) {
			case "br":
				return 0;
			case "brpos":
				return 1;
			case "add":
				return 2;
			case "load":
				return 3;
			case "brzero":
				return 4;
			case "brneg":
				return 5;
			case "sub":
				return 6;
			case "store":
				return 7;
			case "write":
				return 8;
			case "ret":
				return 9;
			case "divide":
				return 10;
			case "stop":
				return 11;
			case "read":
				return 12;
			case "copy":
				return 13;
			case "mult":
				return 14;
			case "call":
				return 15;
		}
		
		return -1;
	}
	
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
	}
	
	public static File convert(File fileAssembly, int stackSize) {
		String binaryOut = "";
		//List<String> macrosList = new ArrayList<>();
		//Stack<String> macrosStack = new Stack<>();
		List<String> variables = new ArrayList<>();
		
		try {
            BufferedReader reader = new BufferedReader(new FileReader(fileAssembly));
            String line;
					
            while ((line = reader.readLine()) != null) {

            	if (line.length() > 80)
					throw new RuntimeException("Linha muito longa");

				//ignora comentários
				if (line.contains("*"))
					line = line.substring(0, line.indexOf("*"));

				//ignora linhas em branco
				if (line.isEmpty())
					continue;

				//divide linha quando acha um espaço
				String[] lineArr = line.split(" ");
				String opcode = lineArr[0];
				System.out.println(opcode);

				//se é diretiva
				//if (opcode.equalsIgnoreCase("start")) {
					//TODO inicio programa
					//continue;
				//}

				//TODO acho q isso é o processador....
				//lida com declaração de macro
				/*if (opcode.equalsIgnoreCase("macro")) {
					if ((line = reader.readLine()) != null) {

						String[] macroArr = line.replace(",", "").split(" ");
						String macroName = macroArr[0];
						boolean hasLabel = false;

						if (getOpcode(macroName) != -1)
							throw new RuntimeException("Erro de sintaxe");

						//TODO não entendi esse &LAB
						if (macroArr[0].startsWith("&LAB")) {
							//TODO handle LAB

							macroName = macroArr[1];
							hasLabel = true;

							if (getOpcode(macroName) != -1)
								throw new RuntimeException("Erro de sintaxe");
						}

						if (macrosList.contains(macroName))
							throw new RuntimeException("Símbolo redefinido");

						//coloca a macro na pilha de macros
						macrosStack.push(macroName);
						macrosList.add(macroName);

						if (hasLabel) {
							if (macroArr.length > 2) {
								//TODO lidar com parametros
							}
						} else {
							if (macroArr.length > 1) {
								//TODO lidar com parametros
							}
						}

						continue;
					} else
						throw new RuntimeException("Macro esperada");
				}


				//lida com final da macro
				if (opcode.equalsIgnoreCase("mend")) {
					if (macrosStack.pop() != null)
						continue;
					else
						throw new RuntimeException("Erro de sintaxe");
				}*/

				// verifica quantidade de parametros
				// se for 1 e for um opcode do tipo stop ou ret continua
				// senão, testa se é um decimal, se não for lança exceção
				if (lineArr.length == 1) {
					if (getOpcode(opcode) == -1) {
						try {
							Integer.parseInt(opcode);
						} catch (NumberFormatException e) {
							throw new RuntimeException("Instrução inválida");
						}

						binaryOut += dataToBinary(opcode);
						continue;

						// verifica quantidade de parametros
					} else if (!opcode.equalsIgnoreCase("stop") && !opcode.equalsIgnoreCase("ret"))
						throw new RuntimeException("Erro de sintaxe");
				}

				// verifica quantidade de parametros
				// se for 2 e for um opcode do tipo copy continua
				// senão verifica se é uma declaração de variável
				if (lineArr.length == 2) {
					if (getOpcode(opcode) == -1) {
						String firstChar = String.valueOf(opcode.charAt(0));
						boolean isNumber = true;

						try {
							Integer.parseInt(firstChar);
						} catch (NumberFormatException e) {
							isNumber = false; //Que gambiarra KKKKKKKKKK
						}

						if (isNumber)
							throw new RuntimeException("Erro de sintaxe");

						//TODO lidar com declarações
						if (lineArr[1].equalsIgnoreCase("space")) {
							if (variables.contains(opcode))
								throw new RuntimeException("Símbolo redefinido");

							variables.add(opcode);

						} else if (lineArr[1].equalsIgnoreCase("const")) {
							//TODO lidar com constantes

						} else throw new RuntimeException("Erro de sintaxe");

						continue;

					} else if (opcode.equalsIgnoreCase("copy"))
						throw new RuntimeException("Erro de sintaxe");
				}

				//TODO suportar variáveis

				String opd1 = "";
				String opd2 = "";
				int addrOpd1 = -1;
				int addrOpd2 = -1;

				if (lineArr.length > 1)
					if (!opcode.equalsIgnoreCase("stop") && !opcode.equalsIgnoreCase("ret")) {
						opd1 = lineArr[1];
						System.out.println(opd1);

						addrOpd1 = getAddrMode(opd1);

						if (opd1.isEmpty())
							throw new RuntimeException("Erro de sintaxe");

						if (lineArr.length > 2)
							if (opcode.equalsIgnoreCase("copy")) {
								opd2 = lineArr[2];
								System.out.println(opd2);

								addrOpd2 = getAddrMode(opd2);

								if (opd2.isEmpty())
									throw new RuntimeException("Erro de sintaxe");

							} else
								throw new RuntimeException("Erro de sintaxe");
					} else
						throw new RuntimeException("Erro de sintaxe");

				binaryOut += opcodeBinary(getOpcode(opcode), addrOpd1, addrOpd2);

				if (!opd1.isEmpty())
					binaryOut += opdBinary(opd1);

				if (!opd2.isEmpty())
					binaryOut += opdBinary(opd2);

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

		//if (!macrosStack.isEmpty())
			//throw new RuntimeException("Falta diretiva MEND");
		
		//String startData = Integer.toBinaryString(numWords + 1);
		//startData = fillBinary(startData, 16, 'l');
		
		String stack = fillBinary("0", 16, 'l') + "\n" + fillBinary("0", 16, 'l') + "\n"; //duas primeiras linhas zeradas
		stack += fillBinary(Integer.toBinaryString(stackSize), 16, 'l') + "\n"; //tamanho da pilha
		
		for (int i = 0; i < stackSize; i++) {
			stack += fillBinary("0", 16, 'l') + "\n";
		}
		
		
		//binaryOut = stack + startData + "\n" + binaryOut;
		binaryOut = stack + binaryOut;


		//Placeholder (não lembro bem como faz isso em java
		File fileOut = new File("input/" + fileAssembly.getName() + "_bin");
		
		try {
			FileWriter out = new FileWriter(fileOut);
			out.write(binaryOut);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileOut;
	}

	public static void main(String[] args) {
		File file = new File("input/assembler_test");

		Assembler.convert(file, 10);
	}
	
	/*public void teste(File file) {
		//System.out.println(opcode);
		
		file = convert(file);
		
		try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
					
            while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
