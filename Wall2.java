public class Wall2 extends Wall{
    
    @Override
    public boolean isWall2() {
        return true;
    }
    
    @Override
    public void handleEdges(){}
    
    public void discolor() {
        setImage("images/wall2D.png");
    }
}