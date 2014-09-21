import java.awt.*;

/**
 * Creates an image of the current shape that tell you where the shape will land
*/
class GhostShape extends Shape{
	public GhostShape(){
		shape = new Square[4][4]; //4*4 2-D array containing the shape
		hitGround = false; //Whether the shape fell on top of another Square object or the bottom boundary
	}
	public void imitate(Block b, Map m){
		Square[][] s = b.getShape();
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				shape[i][j] = new Square(s[i][j].getX(),s[i][j].getY(),Color.gray,s[i][j].isReal(), false,true);
			}
		}
		completeFall(m);
		hitGround = false;
	}
}