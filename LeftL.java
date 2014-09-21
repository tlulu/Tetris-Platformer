import java.awt.*;

/**
* The Left L shape in the tetris game
* Shape:
	0000
	0010
	0010
	0110
*/

class LeftL extends Block{
	public LeftL(int x, int y, Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[1][2].makeSolid(true);
		shape[2][2].makeSolid(true);
		shape[3][1].makeSolid(true);
		shape[3][2].makeSolid(true);
	}
}