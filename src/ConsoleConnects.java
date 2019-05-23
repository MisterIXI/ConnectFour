import java.util.*;

public class ConsoleConnects {
    public static void main(String[] args) {
        /*ConnectFour game = new ConnectFour();
        try{
            game.nextTurn(3);
            game.nextTurn(6);
            game.nextTurn(3);
            game.nextTurn(4);
            game.nextTurn(4);
        }
        catch (Exception e){
        }
        printBoard(game.getCurrentBoard()); */
        boolean exitFlag = false;
        while(!exitFlag){
            ConnectFour game = null;
            switch (menuSelection()){
                case 1:
                    game = new ConnectFour();
                    break;
                case 2:
                    System.out.println("BoardWith:");
                    int width = validateNumScanner(2);
                    System.out.println("BoardHeight:");
                    int height = validateNumScanner(2);
                    System.out.println("Pieces to win:");
                    int pieces = validateNumScanner(2, width > height ? width : height, false);
                    game = new ConnectFour(width, height, pieces);
                    break;
                default:
                    exitFlag = true;
                    break;
            }
            if(!exitFlag){
                boolean tmpExit = false;
                while(!tmpExit){
                    tmpExit = GameLoop(game);
                }
                System.out.println("Final Board: ");
                printBoard(game.getCurrentBoard());
                if(game.getWinner() != ConnectFour.BoardState.Empty)
                    System.out.printf("Player %s won!%nCongratulations!%n",game.isRedTurn() ? "1 (X)" : "2 (O)");
                else
                    System.out.println("It's a tie! Everybody wins... :)");
                System.out.println();
            }
        }
    }

    private static int validateNumScanner(){
        return validateNumScanner(Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }
    private static int validateNumScanner(int lowerBounds) {
        return validateNumScanner(lowerBounds, Integer.MAX_VALUE, false);
    }
    private static int validateNumScanner(int lowerBounds, int upperBounds, boolean controlValueAllowed){
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        while(selection == 0){
            try{
                selection = scanner.nextInt();
                if(selection < lowerBounds || selection > upperBounds){
                    if(!(controlValueAllowed && selection == -1)){
                        selection = 0;
                        throw new IllegalArgumentException("Selection out of Bounds!");
                    }
                }
            }
            catch (IllegalArgumentException e){
                if(controlValueAllowed)
                    System.out.printf("Out of Bounds! Please select a number between %d and %d, or -1 to exit.",lowerBounds,upperBounds);
                else
                    System.out.printf("Out of Bounds! Please select a number between %d and %d.%n",lowerBounds,upperBounds);
            }
            catch (Exception e){
                System.out.println("This is not a valid selection...");
            }
            finally {
                scanner.nextLine(); //without this, the newline char is not "consumed" and it gets stuck in Hell
                //see: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
            }
        }
        return selection;
    }

    private static int menuSelection(){
        System.out.println("Main Menu:");
        System.out.println("1: play a standard game");
        System.out.println("2: play a custom game");
        System.out.println("3: exit...");
        System.out.println("Please select one...");
        System.out.println();
        return validateNumScanner(1,3,false);
    }

    private static boolean GameLoop(ConnectFour game){
        printBoard(game.getCurrentBoard());
        if(game.isRedTurn()) {
            System.out.println("Turn: Player 1 (X)");
        }
        else {
            System.out.println("Turn: Player 2 (O)");
        }

        int turnPos = validateNumScanner(1,game.getCurrentBoard().length, true);

        if(turnPos == -1){

        }
        boolean isGameOver = false;
        try{
            isGameOver =  game.nextTurn(turnPos);
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return isGameOver;
    }

    private static void printBoard(ConnectFour.BoardState[][] board){
        int currentX = 0;
        int currentY = 0;

        System.out.print("\u250C");//corner rop left
        for (int i = 1; i < board.length; i++) {
            System.out.print("\u2500\u2500\u2500\u252C");//top line
        }
        System.out.println("\u2500\u2500\u2500\u2510"); //corner top right

        for (int i = 0; i < board[0].length - 1; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print("\u2502");//vertical pillar
                printState(board[currentX++][currentY]);
            }
            System.out.println("\u2502");//vertical pillar
            currentY++;
            currentX = 0;
            System.out.print("\u251C");//left side pillar
            for (int j = 1; j < board.length; j++) {
                System.out.print("\u2500\u2500\u2500\u253C");//middle cross
            }
            System.out.println("\u2500\u2500\u2500\u2524");//right side pillar
        }

        for (int j = 0; j < board.length; j++) {
            System.out.print("\u2502");// vertical pillar
            printState(board[currentX++][currentY]);
        }
        System.out.println("\u2502");// vertical pillar
        System.out.print("\u2514");//corner bottom left
        for (int i = 1; i < board.length; i++) {
            System.out.print("\u2500\u2500\u2500\u2534");// bottom line
        }
        System.out.println("\u2500\u2500\u2500\u2518"); //corner bottom right
        for (int i = 1; i <= board.length; i++) {
            if(i < 10)
                System.out.print("  " + i + " ");
            else if(i < 100)
                System.out.print(" "  + i + " ");
            else
                System.out.print(""   + i + " ");
        }
        System.out.println();
    }

    private static void printState(ConnectFour.BoardState x){
        System.out.print(' ');
        switch(x){
            case Red:
                System.out.print('X');
                break;
            case Blue:
                System.out.print('O');
                break;
            case Empty:
                System.out.print(' ');
                break;
        }
        System.out.print(' ');
    }
}
