import java.awt.*;

/**
* The square piece in the tetris game
* All other tetris pieces will extend this class to make them the same type of objects
* All tetris pieces are made up of a 4*4 2-D array
* Shape:
	0000
	0110
	0110
	0000
*/
class Block extends Shape{
	public Block(){} //Empty constructor required for extending other classes
	public Block(int x,int y,Color c){
		setUp(x,y,c);
		
		//Setting up the shape
		shape[1][1].makeSolid(true);
		shape[1][2].makeSolid(true);
		shape[2][1].makeSolid(true);
		shape[2][2].makeSolid(true);
	}
	
	/**
	 * The extended classes will call this method instead of the constructor
	 * Fills the Square[][] array with invisible Square objects
	 * @param x Starting x coordinate of the tetris shape
	 * @param y Starting y coordinate of the tetris shape
	 * @param c Color of the tetris shape
	*/
	public void setUp(int x, int y, Color c){
		shape = new Square[4][4]; //4*4 2-D array containing the shape
		for(int k=0;k<shape.length;k++){
			for(int l=0;l<shape[0].length;l++){
				//filling the shape with none-visible square objects
				shape[k][l] = new Square(x+l*20,y+k*20,c,false,false,true);
			}
		}
		hitGround = false; //Whether the shape fell on top of another Square object or the bottom boundary
	}
}