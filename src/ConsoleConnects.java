import java.util.*;

public class ConsoleConnects {
    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();

        /*try{
            game.nextTurn(3);
            game.nextTurn(6);
            game.nextTurn(3);
            game.nextTurn(4);
            game.nextTurn(4);
        }
        catch (Exception e){

        }

        printBoard(game.getCurrentBoard());
*/
        while(!GameLoop(game)){}
        System.out.printf("Player %s won!%nCongratulations!%n",game.isRedTurn() ? "1 (X)" : "2 (O)");
    }

    private static boolean GameLoop(ConnectFour game){
        printBoard(game.getCurrentBoard());
        if(game.isRedTurn()) {
            System.out.println("Turn: Player 1 (X)");
        }
        else {
            System.out.println("Turn: Player 2 (O)");
        }

        int turnPos = 0;
        Scanner scanner = new Scanner(System.in);
        while(turnPos < 1 || turnPos > game.getCurrentBoard().length){
            try{
                turnPos = scanner.nextInt();
                if(turnPos < 1 || turnPos > game.getCurrentBoard().length)
                    System.out.println("Index is out of Bounds!");
            }
            catch (Exception e){
                System.out.println("Wrong input!");

            } 
            finally {
                scanner.nextLine(); //without this, the newline char is not "consumed" and it gets stuck in Hell
                //see: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
            }
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
        currentY++;
        System.out.print("\u2514");//corner bottem left
        for (int i = 1; i < board.length; i++) {
            System.out.print("\u2500\u2500\u2500\u2534");// bottom line
        }
        System.out.println("\u2500\u2500\u2500\u2518"); //corner bottom right
        for (int i = 1; i <= board.length; i++) {
            System.out.print("  " + i + " ");
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
