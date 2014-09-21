import java.awt.*;

/**
* The Right L shape in the tetris game
* Shape:
	0000
	0100
	0100
	0110
*/

class RightL extends Block{
	public RightL(int x, int y, Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[1][1].makeSolid(true);
		shape[2][1].makeSolid(true);
		shape[3][1].makeSolid(true);
		shape[3][2].makeSolid(true);
	}
}