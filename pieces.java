public class pieces{
    private int xCor;
    private int yCor;
    private boolean king;
    private String color;
    private boolean isCaptured;

    public pieces(){
        xCor = 0;
        yCor = 0;
        king = false;
        isCaptured = false;
    }
    public pieces(int x, int y, String c){
        this.xCor = x;
        this.yCor = y;
        this.color = c;
        this.isCaptured = false;
    }

    public int getX(){
        return xCor;
    }
    public int getY(){
        return yCor;
    }
    public boolean kingStatus(){
        return king;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String c){
        color = c;
    }
    public void setX(int x){
        xCor = x;
    }
    public void setY(int y){
        yCor = y;
    }
    public void makeKing(boolean k){
        king = k;
    }
    public void setCaptured(){
        this.isCaptured = true;
    }

    public boolean isCaptured(){
        return this.isCaptured;
    }

    public boolean canCapture(maincheckers board){
        checkersboard[][] tiles = board.getTiles();

        int first  = 0,  second  = 0, third =0, fourth= 0;

        if(this.color.equals("black")){
            first = maincheckers.UpR;
            second = maincheckers.UpL;
            third = maincheckers.DownR;
            fourth = maincheckers.DownL;
        }else{
            first = maincheckers.DownR;
            second = maincheckers.DownL;
            third = maincheckers.UpR;
            fourth = maincheckers.UpL;
        }
        pieces p = getCapture(board, first );

        if(p  == null){
            p = getCapture(board, second);

            if(p == null){
                if(this.king){
                    p = getCapture(board, third);

                    if(p == null){
                        p = getCapture(board, fourth);
                    }
                }
            }
        }
        if(p == null)
            return false;

        return true;
    }

    public  pieces getCapture(maincheckers board, int direction){
        int row = this.xCor;
        int col = this.yCor;
        checkersboard chec = null;
        chec = board.getTile(direction, row, col);

        if(chec == null){
            return null;
        }
        pieces p = chec.getPiece();

        if(p != null){
            if(p.color.equals(this.color)){
                return null;
            }else{
                if(!p.isCaptured()){
                    checkersboard jumpTile = board.getTile(direction, chec.getXCor(), chec.getYCor());
                    if(jumpTile != null && jumpTile.getPiece() == null){
                        return p;
                    }
                }
            }
        }
        return null;
    }

    public boolean move(checkersboard current, checkersboard newTile){
        current.setPiece(null);
        newTile.setPiece(this);

        this.xCor = newTile.getXCor();
        this.yCor = newTile.getYCor();
        checkKing();

        return true;
    }
    private void checkKing(){
        if(this.color.equals("black")){
            if(this.xCor == 0){
                this.king=true;
            }
        }else{
            if(this.xCor == maincheckers.rows - 1){
                this.king= true;
            }
        }

    }

    public boolean capture( checkersboard currTile, checkersboard newTile, checkersboard capTile){
        pieces capPiece = capTile.getPiece();
        if(capPiece == null)
            return false;

        capPiece.setCaptured();
        capTile.setPiece(null);

        currTile.setPiece(null);
        newTile.setPiece(this);

        this.xCor=newTile.getXCor();
        this.yCor=newTile.getYCor();
        checkKing();

        return true;
    }

    public void captureMove(maincheckers board, checkersboard currTile, checkersboard newTile){
        checkersboard[][] tiles = board.getTiles();

        int direction = -1;
        if(newTile.getXCor() > currTile.getXCor()){
            if(newTile.getYCor() < currTile.getYCor())
                direction = maincheckers.DownL;
            else
                direction = maincheckers.DownR;
        }else{
            if(newTile.getYCor() < currTile.getYCor())
                direction = maincheckers.UpL;
            else
                direction = maincheckers.UpR;
        }
        checkersboard chec = board.getJumpTile(direction, newTile.getXCor(), newTile.getYCor());
        move(currTile, chec);

    }
}