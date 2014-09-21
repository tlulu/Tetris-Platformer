import java.awt.*;

/**
* The Line shape in the tetris game
* Shape:
	0000
	0000
	1111
	0000
*/

class Line extends Block{
	public Line(int x, int y, Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[2][0].makeSolid(true);
		shape[2][1].makeSolid(true);
		shape[2][2].makeSolid(true);
		shape[2][3].makeSolid(true);
	}
}