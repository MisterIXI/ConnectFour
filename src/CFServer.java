import java.net.*;
import java.io.*;

public class CFServer {
	private Socket socket1 = null;
	private Socket socket2 = null;
	private ServerSocket server = null;
	private DataInputStream dataIn1 = null;
	private DataOutputStream dataOut1 = null;
	private DataInputStream dataIn2 = null;
	private DataOutputStream dataOut2 = null;

	private char charTopLeftCorner;
	private char charHorizontalWall;
	private char charTopCross;
	private char charTopRightCorner;
	private char charVerticalWall;
	private char charLeftCross;
	private char charRightCross;
	private char charMiddleCross;
	private char charBottomLeftCorner;
	private char charBottomCross;
	private char charBottomRightCorner;
	private char charMoveIndicator;
	
	public CFServer(int port, boolean compatibilityModeEnabled) {
		try {
			initializeChars(compatibilityModeEnabled);
			server = new ServerSocket(port);
			System.out.println("Server started.");
			System.out.println("Waiting for Player1...");
			socket1 = server.accept();
			System.out.println("Player 1 connected from IP: " + socket1.getInetAddress());
			dataIn1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
			dataOut1 = new DataOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
			dataOut1.writeUTF("P1");
			dataOut1.flush();
			dataOut1.writeUTF("Connection accepted! You are Player 1.");
			dataOut1.flush();
			System.out.println("Waiting for Player2...");
			socket2 = server.accept();
			System.out.println("Player 2 connected from IP: " + socket2.getInetAddress());

			dataIn2 = new DataInputStream(new BufferedInputStream(socket2.getInputStream()));
			dataOut2 = new DataOutputStream(new BufferedOutputStream(socket2.getOutputStream()));
			dataOut2.writeUTF("P2");
			dataOut2.flush();
			dataOut2.writeUTF("Connection accepted! You are Player 2.");
			dataOut2.flush();

			boolean loopRunning = true;
			String line1 = "";
			String line2 = "";
			while (loopRunning) {
				try {
					/*
					 * System.out.println("Waiting for Player 1 Input..."); line1 =
					 * dataIn1.readUTF(); System.out.println("Player 1: " + line1);
					 * dataOut1.writeUTF("I heard you, player 1!"); dataOut1.flush();
					 * dataOut2.writeUTF("Your turn"); dataOut2.flush();
					 * System.out.println("Waiting for Player 2 Input..."); line2 =
					 * dataIn2.readUTF(); System.out.println("Player 2: " + line2);
					 * dataOut2.writeUTF("I heard you, player 2!"); dataOut2.flush();
					 * dataOut1.writeUTF("Your turn"); dataOut1.flush(); if (line1.equals("Exit") ||
					 * line2.equals("Exit")) { loopRunning = false; }
					 */

					ConnectFour game = null;
					// PrintStream shieldedOut1 = new PrintStream(new
					// ShieldedCloseOutputStream(dataOut1));
					dataOut2.writeUTF("Waiting for Player 1 to make a selection in the menue.");
					dataOut2.flush();
					switch (menuSelection(dataOut1, dataIn1)) {
					case 1:
						game = new ConnectFour();
						break;
					case 2:
						dataOut1.writeUTF("BoardWith:");
						dataOut1.flush();
						int width = validateNumScanner(2, dataOut1, dataIn1);
						dataOut1.writeUTF("BoardHeight:");
						dataOut1.flush();
						int height = validateNumScanner(2, dataOut1, dataIn1);
						dataOut1.writeUTF("Pieces to win:");
						dataOut1.flush();
						int pieces = validateNumScanner(2, width > height ? width : height, false, dataOut1, dataIn1);
						dataOut1.writeUTF("Game started with " + width + "x" + height + ", and you need " + pieces + " pieces to win. Good luck have fun! :)");
						dataOut2.writeUTF("Game started with " + width + "x" + height + ", and you need " + pieces + " pieces to win. Good luck have fun! :)");
						game = new ConnectFour(width, height, pieces);
						break;
					default:
						loopRunning = false;
						break;
					}
					if (loopRunning) {
						boolean tmpExit = false;
						while (!tmpExit) {
							tmpExit = GameLoop(game, dataOut1, dataIn1, dataOut2, dataIn2);
						}
						dataOut1.writeUTF("Final Board: ");
						dataOut2.writeUTF("Final Board: ");
						printBoard(game.getCurrentBoard(), game.getLastMove(), dataOut1);
						printBoard(game.getCurrentBoard(), game.getLastMove(), dataOut2);
						if (game.getWinner() != ConnectFour.BoardState.Empty) {
							if(game.isRedTurn()) {
								dataOut1.writeUTF("You won! Congratulations!");
								dataOut2.writeUTF("You lose... Better luck next time!");
							}
							else {
								dataOut2.writeUTF("You won! Congratulations!");
								dataOut1.writeUTF("You lose... Better luck next time!");
							}
						}
						else {
							dataOut2.writeUTF("You won! Congratulations!");
							dataOut2.flush();
							dataOut1.writeUTF("You lose... Better luck next time!");
							dataOut1.flush();
						}
					}

				} catch (IOException e) {
					System.out.println(e);
				}

				System.out.println("Closing connections...");

			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {

			try {
				if (socket1 != null)
					socket1.close();
			} catch (IOException e) {
			}
			try {
				if (socket2 != null)
					socket2.close();
			} catch (IOException e) {
			}
			try {
				if (dataIn1 != null)
					dataIn1.close();
			} catch (IOException e) {
			}
			try {
				if (dataOut1 != null)
					dataOut1.close();
			} catch (IOException e) {
			}
			try {
				if (dataIn2 != null)
					dataIn2.close();
			} catch (IOException e) {
			}
			try {
				if (dataOut2 != null)
					dataOut2.close();
			} catch (IOException e) {
			}
		}
	}

	private int validateNumScanner(DataOutputStream out, DataInputStream in) throws IOException {
		return validateNumScanner(Integer.MIN_VALUE, Integer.MAX_VALUE, false, out, in);
	}

	private int validateNumScanner(int lowerBounds, DataOutputStream out, DataInputStream in) throws IOException {
		return validateNumScanner(lowerBounds, Integer.MAX_VALUE, false, out, in);
	}

	private int validateNumScanner(int lowerBounds, int upperBounds, boolean controlValueAllowed, DataOutputStream out,
			DataInputStream in) throws IOException {
		out.writeUTF("Your turn");
		out.flush();
		int selection = 0;
		while (selection == 0) {
			try {
				selection = Integer.parseInt(in.readUTF());
				if (selection < lowerBounds || selection > upperBounds) {
					if (!(controlValueAllowed && selection == -1)) {
						selection = 0;
						throw new IllegalArgumentException("Selection out of Bounds!");
					}
				}
			} catch (IllegalArgumentException e) {
				if (controlValueAllowed)
					out.writeUTF("Out of Bounds! Please select a number between " + lowerBounds + " and " + upperBounds
							+ ", or -1 to exit.");
				else
					out.writeUTF("Out of Bounds! Please select a number between " + lowerBounds + " and " + upperBounds
							+ ".");
				out.flush();
			} catch (Exception e) {
				out.writeUTF("This is not a valid selection...");
				out.flush();
				if(!socket1.isConnected() || !socket2.isConnected())
					continue;
			}
		}
		out.writeUTF("Correct Input");
		out.flush();
		return selection;
	}

	private int menuSelection(DataOutputStream out, DataInputStream in) throws IOException {
		out.writeUTF("Main Menu:");
		out.writeUTF("1: play a standard game");
		out.writeUTF("2: play a custom game");
		out.writeUTF("3: exit...");
		out.writeUTF("Please select one...");
		out.writeUTF(" ");
		out.flush();
		return validateNumScanner(1, 3, false, out, in);
	}

	private boolean GameLoop(ConnectFour game, DataOutputStream out1, DataInputStream in1, DataOutputStream out2,
			DataInputStream in2) throws IOException {
		printBoard(game.getCurrentBoard(), game.getLastMove(), out1);
		printBoard(game.getCurrentBoard(), game.getLastMove(), out2);
		int turnPos;
		if (game.isRedTurn()) {
			out1.writeUTF("Your turn now:");
			out1.flush();
			out2.writeUTF("Waiting for the other player to make their turn...");
			out2.flush();
			turnPos = validateNumScanner(1, game.getCurrentBoard().length, true, out1, in1);

		} else {
			out2.writeUTF("Your turn now:");
			out2.flush();
			out1.writeUTF("Waiting for the other player to make their turn...");
			out1.flush();
			turnPos = validateNumScanner(1, game.getCurrentBoard().length, true, out2, in2);

		}

		boolean isGameOver = false;
		if (turnPos != -1)
			try {
				isGameOver = game.nextTurn(turnPos);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		return isGameOver;
	}

	private void printBoard(ConnectFour.BoardState[][] board, int markPos, DataOutputStream out) throws IOException {
		int currentX = 0;
		int currentY = 0;
		StringBuilder sB = new StringBuilder();
		sB.append(charTopLeftCorner);// corner top left
		for (int i = 1; i < board.length; i++) {
			sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charTopCross);// top line
		}
		sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charTopRightCorner); // corner top right
		out.writeUTF(sB.toString());
		out.flush();
		sB = new StringBuilder();

		for (int i = 0; i < board[0].length - 1; i++) {
			for (int j = 0; j < board.length; j++) {
				sB.append(charVerticalWall);// vertical pillar
				printState(board[currentX++][currentY], sB);
			}
			sB.append(charVerticalWall);// vertical pillar
			out.writeUTF(sB.toString());
			sB = new StringBuilder();

			currentY++;
			currentX = 0;
			sB.append(charLeftCross);// left side pillar
			for (int j = 1; j < board.length; j++) {
				sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charMiddleCross);// middle cross
			}
			sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charRightCross);// right side pillar
			out.writeUTF(sB.toString());
			out.flush();
			sB = new StringBuilder();

		}

		for (int j = 0; j < board.length; j++) {
			sB.append(charVerticalWall);// vertical pillar
			printState(board[currentX++][currentY], sB);
		}
		sB.append(charVerticalWall);// vertical pillar
		out.writeUTF(sB.toString());
		out.flush();
		sB = new StringBuilder();

		sB.append(charBottomLeftCorner);// corner bottom left
		for (int i = 1; i < board.length; i++) {
			sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charBottomCross);// bottom line
		}
		sB.append("" + charHorizontalWall + charHorizontalWall + charHorizontalWall + charBottomRightCorner); // corner bottom right
		out.writeUTF(sB.toString());
		out.flush();
		sB = new StringBuilder();

		for (int i = 1; i <= board.length; i++) {
			if (i < 10)
				sB.append("  " + i + " ");
			else if (i < 100)
				sB.append(" " + i + " ");
			else
				sB.append("" + i + " ");
		}
		out.writeUTF(sB.toString());
		out.flush();
		sB = new StringBuilder();

		if (markPos != -1) {
			for (int i = 1; i < markPos; i++) {
				sB.append("    ");
			}
			sB.append("  " + charMoveIndicator);
		}
		out.writeUTF(sB.toString());
		out.flush();
	}

	private void printState(ConnectFour.BoardState x, StringBuilder sB) {
		sB.append(' ');
		switch (x) {
		case Red:
			sB.append('X');
			break;
		case Blue:
			sB.append('O');
			break;
		case Empty:
			sB.append(' ');
			break;
		}
		sB.append(' ');
	}
	
	private void initializeChars(boolean combatibilityModeEnabled) {
		if(!combatibilityModeEnabled) {
			 charTopLeftCorner = '\u250C';
			 charHorizontalWall = '\u2500';
			 charTopCross = '\u252C';
			 charTopRightCorner = '\u2510';
			 charVerticalWall = '\u2502';
			 charLeftCross = '\u251C';
			 charRightCross = '\u2524';
			 charMiddleCross = '\u253C';
			 charBottomLeftCorner = '\u2514';
			 charBottomCross = '\u2534';
			 charBottomRightCorner = '\u2518';
			 charMoveIndicator = '^';
		}
		else {
			 charTopLeftCorner = '*';
			 charHorizontalWall = '*';
			 charTopCross = '*';
			 charTopRightCorner = '*';
			 charVerticalWall = '*';
			 charLeftCross = '*';
			 charRightCross = '*';
			 charMiddleCross = '*';
			 charBottomLeftCorner = '*';
			 charBottomCross = '*';
			 charBottomRightCorner = '*';
			 charMoveIndicator = '^';
		}
	}
	
	public static void main(String[] args) {
		CFServer x;
		if(args.length == 0)
			x = new CFServer(54242, false);
		else if(args.length == 1)
			x = new CFServer(Integer.parseInt(args[0]), false);
		else
			x = new CFServer(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]));
	}
}
