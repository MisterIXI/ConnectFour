import java.net.*;
import java.util.Scanner;
import java.io.*;

public class CFClient {
	
	private Socket socket = null;
	private DataInputStream dataIn = null;
	private DataOutputStream dataOut = null;
	private boolean isPlayer1 = false;
	
	public CFClient(String address, int port) {
		try {
			System.out.println("Connecting to " + address + " at port " + port + "...");
			socket = new Socket(address, port);
			dataIn = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));
			dataOut = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			if(dataIn.readUTF().equals("P1"))
				isPlayer1 = true;
			else
				isPlayer1 = false;
			boolean isTurn = false;
			boolean endLoop = false;
			while(socket.isConnected() && !endLoop) {
				if(isTurn) {
					Scanner scanner = new Scanner(System.in);
					dataOut.writeUTF(scanner.nextLine());
					//scanner.close();
					dataOut.flush();
					String tmpLine;
					if((tmpLine = dataIn.readUTF()).equals("Correct Input"))
						isTurn = false;
					else 
						System.out.println("Wrong Input! " + tmpLine);
				}
				else {
					String line = dataIn.readUTF();
					if(line.equals("exit"))
						endLoop = true;
					else if(line.equals("Your turn"))
						isTurn = true;
					else 
						System.out.println(line);
				}
			}
			
			
		}
		catch(IOException e) {
			System.out.println(e);
		}
		finally {
			try {
				if(socket != null)
					socket.close();	
			}
			catch(IOException e) {}
			try {
				if(dataIn != null)
					dataIn.close();
			}
			catch (IOException e) {}
			try {
				if(dataOut != null)
					dataOut.close();
			}
			catch(IOException e) {}
		}
	}
	
	
	
	public static void main(String[] args) {
		CFClient x;
		if(args.length == 1)
			x = new CFClient(args[0], 54242);
		else if(args.length == 2)
			x = new CFClient(args[0], Integer.parseInt(args[1]));
		else
			System.out.println("Please provide at least the address. The port is optional. Aborting...");
	}
}
