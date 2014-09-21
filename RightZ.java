import java.awt.*;

/**
* The Right Z shape in the tetris game
* Shape:
	0000
	0011
	0110
	0000
*/

class RightZ extends Block{
	public RightZ(int x, int y, Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[1][2].makeSolid(true);
		shape[1][3].makeSolid(true);
		shape[2][1].makeSolid(true);
		shape[2][2].makeSolid(true);
	}
}