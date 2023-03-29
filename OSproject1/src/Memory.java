import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
	//memory array
	final static int[] memory = new int[2000]; 
	public static int read(int address) {
		return memory[address];
	}


	public static void write(int address, int data) {
		memory[address] = data;
	}
	
	public static void main(String args[]) {
		try {
			//gets filename from CPU
			Scanner in = new Scanner(System.in);
			File file = null;
			
			if (in.hasNextLine()) 
			{
				file = new File(in.nextLine());
				if (!file.exists()) 
				{
					System.out.println("Try Again");
					System.exit(0);
				}
			}

			//populates memory from file
			try {
				Scanner scanner = new Scanner(file);
				String command;
				int counter = 0;

				while (scanner.hasNext()) {
					if (scanner.hasNextInt()) {
						
						memory[counter++] = scanner.nextInt();
					} else {
						command = scanner.next();
						if (command.charAt(0) == '.') {
							counter = Integer.parseInt(command.substring(1));
						}
						else
							scanner.nextLine();
					}
					
				}
//				for(int k = 0;k<memory.length;k++) {
//					System.out.println(memory[k]);
//				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			String line;
			//int temp2;
			
			while (true) {
				
				if (in.hasNext()) {
					line = in.nextLine(); 
					if (!line.isEmpty()) {
						String[] j = line.split(","); 
						if (j[0].equals("r")) {
							System.out.println(read(Integer.parseInt(j[1])));
						}
						else {
							write(Integer.parseInt(j[1]), Integer.parseInt(j[2]));
						}
					} else
						break;
				} else
					break;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}




	

}