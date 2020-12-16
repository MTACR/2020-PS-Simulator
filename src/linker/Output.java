package linker;

public class Output {
	public static String createStart(int stackSize) {
		String output = "";
		
		output += fillBinary("0", 16) + "\n" + fillBinary("0", 16) 
						+ "\n" +  fillBinary(Integer.toString(stackSize, 2), 16) + "\n";
		
		for (int i = 0; i < stackSize; i++) {
			output += fillBinary("0", 16) + "\n";
		}
		
		return output;
	}
	
	public static String opcodeToBin(int word) {
		return fillBinary(Integer.toString(word, 2), 16);
	}
	
	public static String opdToBin(int word) {	
		return fillBinary(Integer.toString(word, 2), 16);
	}
	
	private static String fillBinary(String bin, int size) {
		while (bin.length() < size) {
			bin = '0' + bin;
		}
		
		return bin;
	}
}
