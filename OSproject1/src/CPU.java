import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class CPU {
	/*
	 * Declare required registers
	 */
	static int IR, PC = 0, AC, X, Y, SP = 1000, timer, counter = 0;
	static boolean mode = true;

	/*
	 * read function to read memory
	 */
	private static int read(PrintWriter pw, InputStream is, Scanner sc, OutputStream os, int address) {
		if (mode == true && address >= 1000) {
			System.out.println("Memory Violation");
		}
		// System.out.println("address: " + address);
		pw.printf("r," + address + "\n");
		pw.flush();
		if (sc.hasNext()) {
			// String temp = sc.next();
			// if (!temp.isEmpty()) {
			// int temp2 = Integer.parseInt(temp);

			return (Integer.parseInt(sc.next()));
			// }

		}
		return -1;
	}

	/*
	 * writes to memory with printwriter
	 */
	private static void write(PrintWriter pw, InputStream is, OutputStream os, int address, int data) {
		pw.printf("w," + address + "," + data + "\n");
		pw.flush();
	}

	/*
	 * gets the value of the instruction for IR and finds in the switch statement
	 */
	private static void instruction(int value, PrintWriter pw, InputStream is, Scanner sc, OutputStream os) {
		IR = value;
		// System.out.println("IR: " + IR);
		int op;
		if (mode == true) {
			counter++;
		}
		switch (IR) {
		// load val
		case 1:
			PC++;
			op = read(pw, is, sc, os, PC++);
			AC = op;
			// System.out.println("AC: " + AC);
			if (mode == true)
				counter++;
			// PC++;
//				PC++;
//				pw.println(PC);
//				pw.flush();
//				AC = read(pw,is,sc,os,PC);
//				PC++;
			break;
//load address
		case 2:
			PC++;
			op = read(pw, is, sc, os, PC++);
			AC = read(pw, is, sc, os, op);

			if (mode == true)
				counter++;
			// PC++;
			break;
//LoadInd addr   
		case 3:
			PC++;
			op = read(pw, is, sc, os, PC++);
			op = read(pw, is, sc, os, op);
			AC = read(pw, is, sc, os, op);
			if (mode == true)
				counter++;
			// PC++;
			break;
//LoadIdxX addr
		case 4:
			// System.out.println("got here");
			PC++;
			op = read(pw, is, sc, os, PC++);
			AC = read(pw, is, sc, os, op + X);
			if (mode == true)
				counter++;
			// PC++;
			break;
//LoadIdxY addr
		case 5:
			PC++;
			op = read(pw, is, sc, os, PC++);
			AC = read(pw, is, sc, os, op + Y);
			if (mode == true)
				counter++;
			// PC++;
			break;
// LoadSpX
		case 6:
			AC = read(pw, is, sc, os, SP + X);
			if (mode == true)
				counter++;
			PC++;
			break;
//Store addr
		case 7:
			PC++;
			op = read(pw, is, sc, os, PC++);
			write(pw, is, os, op, AC);
			if (mode == true)
				counter++;
			// PC++;
			break;
//get
		case 8:
			Random r = new Random();
			int i = r.nextInt(100) + 1;
			AC = i;
			if (mode == true)
				counter++;
			PC++;
			break;
//put port
		case 9:

			PC++;
			op = read(pw, is, sc, os, PC);
			if (op == 1) {
				System.out.print(AC);
				if (mode == true)
					counter++;
				PC++;
				break;

			} else if (op == 2) {
				System.out.print((char) AC);
				if (mode == true)
					counter++;
				PC++;
				break;
			} else {
				System.out.println("Error: Port = " + op);
				if (mode == true)
					counter++;
				PC++;
				System.exit(0);
				break;
			}
//AddX
		case 10:

			AC += X;
			if (mode == true)
				counter++;
			PC++;
			break;
//AddY
		case 11:

			AC += Y;
			if (mode == true)
				counter++;
			PC++;
			break;
//SubX
		case 12:
			AC -= X;
			if (mode == true)
				counter++;
			PC++;
			break;
		// subY
		case 13:
			AC -= Y;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyToX
		case 14:

			X = AC;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyFromX
		case 15:
			AC = X;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyToY
		case 16:
			Y = AC;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyFromY
		case 17:
			AC = Y;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyToSp
		case 18:
			SP = AC;
			if (mode == true)
				counter++;
			PC++;
			break;
//CopyFromSp   
		case 19:
			AC = SP;
			if (mode == true)
				counter++;
			PC++;
			break;
//Jump addr
		case 20:
			PC++;
			op = read(pw, is, sc, os, PC);
			PC = op;
			if (mode == true)
				counter++;
			break;
//JumpIfEqual addr
		case 21:
			PC++;
			op = read(pw, is, sc, os, PC++);
			if (AC == 0) {
				PC = op;
				if (mode == true)
					counter++;
				break;
			}
			if (mode == true)
				counter++;
			// PC++;
			break;
//JumpIfNotEqual addr
		case 22:
			PC++;
			op = read(pw, is, sc, os, PC);
			if (AC != 0) {
				PC = op;
				if (mode == true)
					counter++;
				break;
			}
			if (mode == true)
				counter++;
			PC++;
			break;
//Call addr
		case 23:
			PC++;
			op = read(pw, is, sc, os, PC);
			SP--;
			write(pw, is, os, SP, PC + 1);
			PC = op;
			if (mode == true)
				counter++;
			break;
//Ret
		case 24:
			op = pop(pw, is, sc, os);
			PC = op;
			if (mode == true)
				counter++;
			break;
//IncX
		case 25:
			X++;
			if (mode == true)
				counter++;
			PC++;
			break;
//DecX
		case 26:
			X--;
			if (mode == true)
				counter++;
			PC++;
			break;
//Push
		case 27:
			SP--;
			write(pw, is, os, SP, AC);
			PC++;
			if (mode == true)
				counter++;
			break;
//Pop
		case 28:
			AC = pop(pw, is, sc, os);
			PC++;
			if (mode == true)
				counter++;
			break;
//Int
		case 29:

			mode = false;
			op = SP;
			SP = 2000;
			SP--;
			write(pw, is, os, SP, op);

			op = PC + 1;
			PC = 1500;
			SP--;
			write(pw, is, os, SP, op);

			break;
//IRet
		case 30:

			PC = pop(pw, is, sc, os);
			SP = pop(pw, is, sc, os);
			mode = true;
			counter++;
			break;
//End
		case 50:
			if (mode == true)
				counter++;
			System.exit(0);
			break;

		}
	}

	/*
	 * handles interrupts
	 */
	private static void interrupt(PrintWriter pw, InputStream is, Scanner sc, OutputStream os) {
		int op;
		mode = false;
		op = SP;
		SP = 2000;
		SP--;
		write(pw, is, os, SP, op);

		op = PC;
		PC = 1000;
		SP--;
		write(pw, is, os, SP, op);

	}

	/*
	 * pops stack back from system
	 */
	private static int pop(PrintWriter pw, InputStream is, Scanner sc, OutputStream os) {
		int temp = read(pw, is, sc, os, SP);
		write(pw, is, os, SP, 0);
		SP++;
		return temp;
	}

	public static void main(String args[]) {

		// System.out.println("hello");

		String fileName = args[0];
		// fileName = "input.txt";
		timer = Integer.parseInt(args[1]);
		// timer = 10;

		try {

			// runtime exec to run Memory class
			Runtime rt = Runtime.getRuntime();
			@SuppressWarnings("deprecation")
			Process proc = rt.exec("java Memory");
			InputStream is = proc.getInputStream();
			OutputStream os = proc.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			Scanner sc = new Scanner(is);

			pw.printf(fileName + "\n");
			pw.flush();

			// boolean flag = true;
			// loop to read values from memory
			while (true) {
				// System.out.println("AC: " + AC);

				if ((counter % timer) == 0 && mode == true) {
					interrupt(pw, is, sc, os);
				}

				int value = read(pw, is, sc, os, PC);

				if (value != -1) {
					instruction(value, pw, is, sc, os);
				} else
					break;
				// flag = false;
			}

			proc.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
