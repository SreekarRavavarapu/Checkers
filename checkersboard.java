public class checkersboard{
    private String color;
    private pieces piece;
    private int x;
    private int y;

    public checkersboard(){

    }
    public checkersboard(String c,int x, int y){
        color = c;
        this.x = x;
        this.y = y;
    }
    public String getColor(){
        return color;
    }
    public pieces getPiece(){
        return piece;
    }
    public int getXCor(){
        return this.x;
    }
    public int getYCor(){
        return this.y;
    }
    public void setColor(String c){
        this.color = c;
    }
    public void setPiece(pieces p){
        this.piece = p;
    }

    public boolean isWhite(){

        if (this.color.equals("white")){
            return true;
        }
        return false;
    }
}
