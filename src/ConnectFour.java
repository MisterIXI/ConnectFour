
public class ConnectFour {
    enum BoardState{
        Empty,
        Red,
        Blue
    }

    private boolean isRedTurn;
    private BoardState[][] playBoard;
    private boolean isGameRunning;
    private BoardState winner;
    private int turnCount;
    private int piecesNeeded;

    public ConnectFour(){
        this(7,6);
    }

    public ConnectFour(int boardWidth, int boardLength){
        this(boardWidth, boardLength, 4);
    }

    public ConnectFour(int boardWidth, int boardLength, int winLength){
        playBoard = new BoardState[boardWidth][boardLength];
        for (int i = 0; i < playBoard.length; i++) {
            for (int j = 0; j < playBoard[0].length; j++) {
                playBoard[i][j] = BoardState.Empty;
            }
        }
        isRedTurn = true;
        isGameRunning = true;
        winner = null;
        turnCount = 0;
        piecesNeeded = winLength;
    }

    /*
    Returns true when game is over, otherwise false.
    When this is called after the game is over a IllegalCallerException is thrown.
    When this is called with an invalid index, an IllegalAccessException is thrown.
    When this is called on a full column,an IllegalArgumentException is thrown.
     */
    public boolean nextTurn(int posX) throws IllegalAccessException, IllegalArgumentException, IllegalCallerException{
        posX--;
        if(!isGameRunning)
            throw new IllegalCallerException("Game is already over! Can't excecute another turn.");
        if(posX >= playBoard.length)
            throw new IllegalAccessException("Given position is out of bounds!");
        if(playBoard[posX][0] != BoardState.Empty)
            throw new IllegalArgumentException("Column is already full! Illegal operation.");

        int posY = 0;
        while(playBoard[posX].length > posY + 1 && playBoard[posX][posY + 1] == BoardState.Empty){
            posY++;
        }

        if(isRedTurn)
            playBoard[posX][posY] = BoardState.Red;
        else
            playBoard[posX][posY] = BoardState.Blue;

        boolean isGameOver = checkForWin(posX, posY); //determine if one player won
        turnCount++;
        if(isGameOver){ //determine which player won
            if(isRedTurn)
                winner = BoardState.Red;
            else
                winner = BoardState.Blue;
        }
        else{
            if(turnCount == playBoard.length * playBoard[0].length){ //in case the board is full declare tie
                isGameOver = true;
                winner = BoardState.Empty;
            }
        }

        if(isGameOver)
            isGameRunning = false;
        else
            isRedTurn = !isRedTurn;


        return isGameOver;
    }

    private boolean checkForWin(int X, int Y){
        boolean result = false;
        BoardState player = playBoard[X][Y];
        if(player == BoardState.Empty)
            throw new IllegalArgumentException("Can't check for empty field...");
        for (int dirX = -1; dirX < 1 && !result; dirX++) {
            for (int dirY = -1; dirY < 2 && !result; dirY++) {
                if(dirX != 0 || dirY == -1){
                    int tempX = X;
                    int tempY = Y;
                    while(tempX + dirX >= 0 && tempX + dirX < playBoard.length &&
                            tempY + dirY >= 0 && tempY + dirY < playBoard[0].length &&
                            playBoard[tempX + dirX][tempY + dirY] == player){//run to "start" of chain
                        tempX += dirX;
                        tempY += dirY;
                    }
                    int count = 0;
                    while(tempX >= 0 && tempX < playBoard.length &&
                            tempY >= 0 && tempY < playBoard[0].length &&
                            count < piecesNeeded &&
                            playBoard[tempX][tempY] == player) {//run to "end" of chain with max 4 steps
                        tempX -= dirX;
                        tempY -= dirY;
                        count++;
                    }
                    if(count == piecesNeeded)
                        result = true;
                }
            }
        }
        return result;
    }


    public boolean isRedTurn(){
        return isRedTurn;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public BoardState[][] getCurrentBoard(){
        return playBoard;
    }

    public BoardState getWinner(){
        return winner;
    }
}
