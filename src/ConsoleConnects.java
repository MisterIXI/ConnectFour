
public class ConsoleConnects {
    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        
        try{
            game.nextTurn(3);
            game.nextTurn(6);
            game.nextTurn(3);
            game.nextTurn(4);
            game.nextTurn(4);
        }
        catch (Exception e){

        }

        printBoard(game.getCurrentBoard());

       // while(GameLoop(game)){}
    }

    private static boolean GameLoop(ConnectFour game){
        printBoard(game.getCurrentBoard());


        return true;
    }

    private static void printBoard(ConnectFour.BoardState[][] board){
        int currentX = 0;
        int currentY = 0;

        System.out.print("\u250C");//┌
        for (int i = 1; i < board.length; i++) {
            System.out.print("\u2500\u2500\u2500\u252C");//┬
        }
        System.out.println("\u2500\u2500\u2500\u2510"); //┐

        for (int i = 0; i < board[0].length - 1; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print("\u2502");//│
                printState(board[currentX++][currentY]);
            }
            System.out.println("\u2502");//│
            currentY++;
            currentX = 0;
            System.out.print("\u251C");//├
            for (int j = 1; j < board.length; j++) {
                System.out.print("\u2500\u2500\u2500\u253C");//─┼
            }
            System.out.println("\u2500\u2500\u2500\u2524");//─┤
        }

        for (int j = 0; j < board.length; j++) {
            System.out.print("\u2502");//│
            printState(board[currentX++][currentY]);
        }
        System.out.println("\u2502");//│
        currentY++;

        System.out.print("\u2514");//└
        for (int i = 1; i < board.length; i++) {
            System.out.print("\u2500\u2500\u2500\u2534");//─┴
        }
        System.out.println("\u2500\u2500\u2500\u2518"); //─┘
        for (int i = 1; i <= board.length; i++) {
            System.out.print("  " + i + " ");
        }
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
