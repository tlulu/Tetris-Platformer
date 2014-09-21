import java.awt.*;

/**
* The T shape in the tetris game
* Shape:
	0000
	0010
	0111
	0000
*/

class T extends Block{
	public T(int x, int y, Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[1][2].makeSolid(true);
		shape[2][1].makeSolid(true);
		shape[2][2].makeSolid(true);
		shape[2][3].makeSolid(true);
	}
}