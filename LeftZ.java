import java.awt.*;

/**
* The Left Z shape in the tetris game
* Shape:
	0000
	0110
	0011
	0000
*/

class LeftZ extends Block{
	public LeftZ(int x, int y, Color c){
		setUp(x,y,c);

		//Setting up the shape
		shape[1][1].makeSolid(true);
		shape[1][2].makeSolid(true);
		shape[2][2].makeSolid(true);
		shape[2][3].makeSolid(true);
	}
}