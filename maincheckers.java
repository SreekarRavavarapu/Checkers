import java.util.Scanner;
import java.util.ArrayList;
public class maincheckers {
    final static int rows = 8;
    final static int columns = 8;
    final static int numPieces = 12;
    final static int DownR = 1;
    final static int DownL = 2;
    final static int UpR = 3;
    final static int UpL =4;
    final static int numTries = 3;
    private checkersboard[][] tiles;
    private ArrayList<pieces> P1pieces;
    private ArrayList<pieces> P2pieces;

    public static void main(String[] args){
        System.out.println("Lets play Checkers!");
        maincheckers Game = new maincheckers();
        Game.startPlay();
    }

    public maincheckers(){
        setupBoard();
    }
    private void setupBoard(){
        tiles = new checkersboard[rows][columns];
        boolean isWhite = true;
        boolean firstColW = true;
        for(int row = 0; row < rows; row++){
            isWhite = firstColW;
            for(int col = 0; col < columns; col++){
                checkersboard ct = null;
                if(isWhite){
                    ct = new checkersboard("white", row, col);
                    isWhite = false;
                }else{
                    ct = new checkersboard("black", row, col);
                    isWhite=true;
                }
                tiles[row][col] = ct;
            }
            if(firstColW){
                firstColW = false;
            }else{
                firstColW = true;
            }
        }
    }
    public boolean startPlay(){
        setupPlayer1Pieces();
        setupPlayer2Pieces();
        printBoard();
        boolean over = false;

        while(!over) {
            int numOfPieces = checkPieces("black");

            if (numOfPieces == 0) {
                System.out.println("No pieces left for player 1. Player 2 WINS");
                over = true;
            } else {
                numOfPieces = checkPieces("white");

                if (numOfPieces == 0) {
                    System.out.println("No pieces left for player 2. Player 1 WINS");
                } else {
                    System.out.println("Player 1's turn");
                    if (!move("black")){
                        System.out.println(" Moves failed so Player 2 WINS. ");
                        over=true;
                    }else {
                        printBoard();

                        System.out.println("turn:Player 2");
                        if (!move("white")) {
                            System.out.println(" Moves failed so Player 1 WINS. ");
                            over = true;
                        } else {
                            printBoard();
                        }
                    }
                }
            }
        }
        return true;
    }
    private boolean move(String color){
        boolean keepPlaying = true;
        boolean hasToCapture = checkForCaptures(color);

        int tryNo = 1;

        while(keepPlaying) {
            if(tryNo == maincheckers.numTries){
                System.out.println("Moves failed after " + maincheckers.numTries + " tries" );
                return false;
            }
            tryNo++;
            checkersboard current = null;
            checkersboard newTile = null;

            System.out.println("Enter row piece you want to move.");
            int fromRow = getPlayerMoves();

            System.out.println("Enter column piece you want to move.");
            int fromCol = getPlayerMoves();

            System.out.println("Enter the row you want to move.");
            int toRow = getPlayerMoves();

            System.out.println("Enter the column you want to move.");
            int toCol = getPlayerMoves();

            current = this.getTile(fromRow, fromCol);
            newTile = this.getTile(toRow, toCol);
            if (hasToCapture) {
                if (capture(current, newTile)) {
                    pieces curPiece = current.getPiece();
                    if (captured(current, newTile)) {
                        System.out.println("captured!!");
                        boolean hasMultipleJumps = checkForCaptures(curPiece);
                        if(hasMultipleJumps){
                            printBoard();
                            tryNo=1;
                            if(color.equals("black")) {
                                System.out.println("Player 1 has multiple jumps. Continue moves");
                            }else{
                                System.out.println("Player 2 has multiple jumps. Continue moves");
                            }
                        }else {
                            keepPlaying = false;
                        }
                    }
                } else {
                    System.out.println("You have to capture. Go again");
                }
            } else {
                if (isValidMove(current, newTile)) {
                    pieces curPiece = current.getPiece();
                    if (move(current, newTile)) {
                        keepPlaying = false;
                    }

                }else{
                    System.out.println("Not valid. Try again");
                }
            }
        }
        return true;
    }
    private void printBoard(){
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                checkersboard ct = tiles[row][col];
                pieces p = ct.getPiece();
                if(p == null || p.isCaptured()){
                    System.out.print("    |");
                }else{
                    if(p.getColor().equals("black")){
                        if(p.kingStatus())
                            System.out.print("BB"+ (row +1)  + (col + 1) + "|");
                        else
                            System.out.print("B"+ (row +1)  + (col + 1)  + " |");
                    }
                    else{
                        if(p.kingStatus())
                            System.out.print("WW"  +  (row +1)  + (col + 1) + "|");
                        else
                            System.out.print("W"+ (row +1)  + (col + 1)  + " |");
                    }
                }
            }
            System.out.println("\n----|----|----|----|----|----|----|----|");
        }
    }
    private void setupPlayer1Pieces(){
        P1pieces = new ArrayList<pieces>();
        for(int row = rows - 1; row > 4; row--){
            for(int col = 0; col < 8; col++){
                checkersboard ct = tiles[row][col];
                if(!ct.isWhite()){
                    pieces p = new pieces(row, col, "black");
                    ct.setPiece(p);
                    P1pieces.add(p);
                }
            }
        }
    }
    private void setupPlayer2Pieces(){
        P2pieces = new ArrayList<pieces>();
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 8; col++){
                checkersboard ct = tiles[row][col];
                if(!ct.isWhite()){
                    pieces p = new pieces(row, col, "white");
                    ct.setPiece(p);
                    P2pieces.add(p);
                }
            }
        }
    }
    public int getPlayerMoves() {
        Scanner input = new Scanner(System.in);
        int move = input.nextInt();
        move--;
        return move;
    }
    private boolean checkForCaptures(String color){
        ArrayList<pieces> playerPieces;
        if(color.equals("black")){
            playerPieces = this.P1pieces;
        }
        else{
            playerPieces = this.P2pieces;
        }
        for(pieces p : playerPieces){
            if(!p.isCaptured()){
                if(p.canCapture(this)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkForCaptures(pieces p){
        if(!p.isCaptured()){
            if(p.canCapture(this)){
                return true;
            }
        }
        return false;
    }
    private boolean isValidMove(checkersboard current, checkersboard newTile){
        if(current == null || newTile == null || newTile.isWhite()){
            return false;
        }
        pieces piece = current.getPiece();

        if(piece == null)
            return false;
        int curRow = current.getXCor();
        int newRow = newTile.getXCor();

        pieces newPiece = newTile.getPiece();
        if(newPiece != null) {
            return false;
        }
        if(piece.getColor().equals("black")){
            if(newRow == curRow - 1)
                return true;

            if(newRow == curRow + 1 && piece.kingStatus())
                return true;
        }else{
            if(newRow == curRow +1)
                return true;

            if(newRow == curRow -1 && piece.kingStatus())
                return true;
        }
        return false;
    }
    public checkersboard getTile(int row, int col){
        if((row >= 0 && row < rows) && (col >= 0 && col < columns)){
            return tiles[row][col];
        }
        return null;
    }
    public checkersboard[][] getTiles(){
        return this.tiles;
    }

    public checkersboard getTile(int direction, int row, int col){
        if(direction == maincheckers.DownR){
            row++;
            col++;
        }else if(direction == maincheckers.DownL){
            row++;
            col--;
        }else if(direction == maincheckers.UpR){
            row--;
            col++;
        }else if(direction == maincheckers.UpL){
            row--;
            col--;
        }
        if(row < 0 || row > maincheckers.rows -1)
            return null;

        if(col < 0 || col > maincheckers.columns -1)
            return null;

        checkersboard chec = tiles[row][col];

        return chec;
    }

    public checkersboard getJumpTile(int direction, int row, int col){
        if(direction == maincheckers.DownR){
            row+=2;
            col+=2;
        }else if(direction == maincheckers.DownL){
            row+=2;
            col-=2;
        }else if(direction == maincheckers.UpR){
            row-=2;
            col-=2;
        }else if(direction == maincheckers.UpL){
            row-=2;
            col-=2;
        }
        if(row < 0 || row > maincheckers.rows -1)
            return null;

        if(col < 0 || col > maincheckers.columns -1)
            return null;

        checkersboard chec = tiles[row][col];

        return chec;
    }

    private boolean capture(checkersboard currTile, checkersboard newTile){
        if(currTile == null || newTile == null)
            return false;
        pieces currPiece = currTile.getPiece();
        pieces newPiece = newTile.getPiece();

        if(newPiece != null){
            return false;
        }

        int newRow = newTile.getXCor();
        int curRow = currTile.getXCor();

        int newCol = newTile.getYCor();
        int curCol = currTile.getYCor();

        int capRow = curRow;
        int capCol = curCol;

        if(currPiece.getColor().equals("black")){
            if(!currPiece.kingStatus() ){
                if(newRow != curRow -2) {
                    return false;
                }
            }else {
                if (Math.abs(newRow - curRow) != 2) {
                    return false;
                }
            }
        }else{
            if(!currPiece.kingStatus() ){
                if(newRow != curRow +2) {
                    return false;
                }
            }else {
                if(Math.abs(newRow -  curRow) != 2) {
                    return false;
                }
            }
        }
        if(newRow > curRow)
            capRow++;
        else
            capRow--;

        if(newCol > curCol)
            capCol++;
        else
            capCol--;

        checkersboard capTile = getTile(capRow, capCol);
        pieces capPiece = capTile.getPiece();

        if(capPiece.getColor().equals(currPiece.getColor())){
            return false;
        }
        return true;
    }
    private boolean captured(checkersboard current, checkersboard newTile){
        if(current == null || newTile == null)
            return false;

        pieces currPiece = current.getPiece();

        int fromRow = current.getXCor();
        int toRow =  newTile.getXCor();

        int fromCol = current.getYCor();
        int toCol = newTile.getYCor();

        int capRow = fromRow;
        int capCol = fromCol;

        if(toRow > fromRow)
            capRow++;
        else
            capRow--;

        if(toCol > fromCol)
            capCol++;
        else
            capCol--;

        checkersboard capTile = getTile(capRow, capCol);

        if(capTile == null)
            return false;
        return currPiece.capture(current, newTile, capTile);
    }

    private boolean move(checkersboard current, checkersboard newTile){
        if(current == null || newTile == null)
            return false;

        pieces p = current.getPiece();

        if(p == null)
            return false;

        boolean b = p.move(current, newTile);
        return b;
    }
    private int checkPieces(String color){
        ArrayList<pieces> playerPieces = null;

        if(color.equals("black")){
            playerPieces = this.P1pieces;
        }else{
            playerPieces = this.P2pieces;
        }

        int numpieces = 0;

        for(pieces p : playerPieces){
            if(!p.isCaptured()){
                numpieces++;
            }
        }
        return numpieces;
    }
}