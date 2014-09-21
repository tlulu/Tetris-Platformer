import java.awt.*;
import java.util.ArrayList;
/**
* Every tetris piece will have this as the parent class
* Controls all actions performed by the tetris pieces
*/
abstract class Shape{

	protected Collision collide = new Collision(0,THA.HEIGHT,0,THA.WIDTH); //Collision checking
	//protected AI shapeAI = new AI();
	protected Square[][] shape; //Forms the shape of the tetris piece
	protected boolean hitGround; //Whether the piece hit the ground or not
	
	/**
	 * Returns the shape 2-D array containing Square objects
	 * @return  Square[][] of the tetris piece
	*/
	public Square[][] getShape(){
		return shape;
	}
	
	/**
	 * Given the x and y coordinate of a Square object
	 * Returns the index of that Square object inside the Square[][] array
	 * If x and y coordinate do not exist in the Square[][] array
	 * Return index -1, -1
	 * @param x	The x coordinate of a Square object
	 * @param y The y coordinate of a Square object
	 * @return  int[] with values, indicating the index of the Square[][] array
	*/
	public int[] getIndex(int x, int y){
		int[] index = new int[2];
		index[0] = -1; index[1] = -1;
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(x==shape[i][j].getX()&&y==shape[i][j].getY()){
					index[0] = i;
					index[1] = j;
					return index;
				}
			}
		}
		return index;
	}
	
	/**
	 * Changes the Square[][] array into another Square[][] array
	 * Is able to change the shape into a completely different shape
	 * NOTE: does not change hitGround because it doesn't apply to individual Square class
	 * @param s Square[][] array that is to be changed into
	*/
	public void change(Square[][] s){
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				shape[i][j].makeSolid(s[i][j].isReal());
				shape[i][j].makeBackground(s[i][j].isMapBackground());
				shape[i][j].changeSquare(s[i][j].isSquare());
				shape[i][j].changeCoord(s[i][j].getX(),s[i][j].getY());
			}
		}
	}
	
	/**
	 * @return  boolean hitGround; indicating if the shape is touching the bottom boundary or the top of another Square object
	*/
	public boolean hitGround(){
		return hitGround;
	}
	public void changeHitGround(boolean h){
		hitGround = h;
	}
	/**
	 * Checks if all Square objects in the Square[][] array have become invisible
	 * @return  true: all Square objects are invisible; false: at least 1 Square object remains solid
	*/
	public boolean shapeGone(){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[i][j].isReal()){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Draws the Square[][] array
	 * Will only draw if the Square object at each index is solid
	 * @param g Graphics
	*/
	public void draw(Graphics g){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				shape[i][j].drawSpecialSquare(g);
			}
		}	
	}
	
	public void printShape(){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[i][j].isReal()){
					System.out.print("1 ");
				}else{
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
	}
	/**
	 * Turns the Square[][] array by 90 degrees to the left
	 * Create a brand new shape array
	 * Then reintialize each index while turning it at the same time
	 * Copy the turned new shape array to the original array
	*/
	public void turn(){
		Square[][] newShape = new Square[shape.length][shape[0].length];
		int x,y;
		Color c = shape[0][0].getColor();
		boolean solid;
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[i][j].isReal()){
					newShape[j][shape.length-1-i] = new Square(shape[j][shape.length-1-i].getX(),shape[j][shape.length-1-i].getY(),c,true,false,true);
				}else{
					newShape[j][shape.length-1-i] = new Square(shape[j][shape.length-1-i].getX(),shape[j][shape.length-1-i].getY(),c,false,false,true);
				}
			}
		}
		shape = newShape;
	}
	
	/**
	 * A more veratile version of turn that will not turn out of bound
	 * Works by tracking the shape's movement
	 * The reset the shape to the original position with turnign
	 * Then use ai command to move the shape according to the tracked positions
	*/
	public void turn(Map m, ArrayList<Integer> st){
		Square[][] newShape = new Square[shape.length][shape[0].length];
		int x,y;
		Color c = shape[0][0].getColor();
		boolean solid;
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[i][j].isReal()){
					newShape[j][shape.length-1-i] = new Square(shape[j][shape.length-1-i].getX(),shape[j][shape.length-1-i].getY(),c,true,false,true);
				}else{
					newShape[j][shape.length-1-i] = new Square(shape[j][shape.length-1-i].getX(),shape[j][shape.length-1-i].getY(),c,false,false,true);
				}
			}
		}
		shape = newShape;
		//Return to original position
		for(int i=st.size()-1;i>=0;i--){
			if(st.get(0)==1){
				moveRight(m);
			}else{
				moveLeft(m);
			}
		}
		//Goes back to the previous position
		for(int i=0;i<st.size();i++){
			aiCommand(m, st.get(i),st);
		}
	}
	/**
	 * Moves the y coordinate of every Square object in Square[][] array down by 1 block (+Square.SIZE)
	 * Checks boundary collision before moving
	 * After moving, checks for object collision between map
	 * If object collision is present, move the shape 1 block up
	 * If either boundary or object collision is present, change hitGround to true
	 * @param m Map of the tetris game
	*/
	public void fall(Map m){
		if(collide.boundCollide(shape)[3]==-1){ //check if collide with boundary, if not, fall
			for(int i=0;i<shape.length;i++){
				for(int j=0;j<shape[0].length;j++){
					shape[i][j].changeY(shape[i][j].getY()+Square.SIZE);
				}
			}
			if(collide.objCollide(shape,m.getMap())[3]!=-1){ //check if collide with map, it collides, move up
				moveUp();
				hitGround = true;
			}
		}else{
			hitGround = true;
		}
	}
	
	/**
	 * A more versatile version of fall the considers the position of the character as well
	 * The character will fall with it if it's under the shape
	*/
	public void fall(Map m, Character c){
		if(collide.boundCollide(shape)[3]==-1){ //check if collide with boundary, if not, fall
			for(int i=0;i<shape.length;i++){
				for(int j=0;j<shape[0].length;j++){
					shape[i][j].changeY(shape[i][j].getY()+Square.SIZE);
				}
			}
			if(collide.objCollide(shape,m.getMap())[3]!=-1){ //check if collide with map, it collides, move up
				moveUp();
				hitGround = true;
			}else{
				if(!c.getGround() && collide.objCollide(c,shape)[3]!=-1){
					c.changeY(c.getY()+20);
				}
			}
		}else{
			hitGround = true;
		}
	}
	/**
	 * Moves the y coordinate of every Square object in Square[][] array up by 1 block (-Square,SIZE)
	 * Does not require collision checking as this method is not noticable in game
	*/
	public void moveUp(){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				shape[i][j].changeCoord(shape[i][j].getX(),shape[i][j].getY()-Square.SIZE);
			}
		}
	}
	
	/**
	 * Moves the x coordinate of every Square object in Square[][] array to the left by 1 block (-Square.SIZE)
	 * Checks boundary collision before moving
	 * After moving, checks for object collision between map
	 * If object collision is present, move the shape to the right by 1 block
	 * @param m Map of the tetris game
	*/
	public void moveLeft(Map m){
		if(collide.boundCollide(shape)[0]==-1){ //check if collide with boundary, if not, move left
			for(int i=0;i<shape.length;i++){
				for(int j=0;j<shape[0].length;j++){
					shape[i][j].changeX(shape[i][j].getX()-Square.SIZE);
				}
			}
		}
		if(collide.objCollide(m.getMap(),shape)[0]!=-1){ //check if collide with map, it collides, move right
			absMoveRight();
		}
	}
	//Move left without considering collision
	public void absMoveLeft(){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				shape[i][j].changeX(shape[i][j].getX()-Square.SIZE);
			}
		}
	}
	/**
	 * Moves the x coordinate of every Square object in Square[][] array to the right by 1 block (+Square.SIZE)
	 * Checks boundary collision before moving
	 * After moving, checks for object collision between map
	 * If object collision is present, move the shape to the left by 1 block
	 * @param m Map of the tetris game
	*/
	public void moveRight(Map m){
		if(collide.boundCollide(shape)[1]==-1){ //check if collide with boundary, if not, move right
			for(int i=0;i<shape.length;i++){
				for(int j=0;j<shape[0].length;j++){
					shape[i][j].changeX(shape[i][j].getX()+Square.SIZE);
				}
			}
		}
		if(collide.objCollide(m.getMap(),shape)[1]!=-1){ //check if collide with map, it collides, move left
			absMoveLeft();
		}
	}
	
	//Move right without considering collision
	public void absMoveRight(){
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				shape[i][j].changeX(shape[i][j].getX()+Square.SIZE);
			}
		}
	}
	
	//Falls until it collides with map
	//Used for checking ai
	public void completeFall(Map m){
		while(!hitGround){
			fall(m);
		}
	}
	
	//Get the index of the length of the visible parts of the shape
	public int[] getWidthIndex(){
		int[] index = new int[2];
		outerloop:
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[j][i].isReal()){
					index[0] = i;
					break outerloop;
				}
			}
		}
		outerloop:
		for(int i=shape.length-1;i>=0;i--){
			for(int j=0;j<shape[0].length;j++){
				if(shape[j][i].isReal()){
					index[1] = i;
					break outerloop;
				}
			}
		}
		return index;
	}
	//Commands for the ai
	public void aiCommand(Map m, int command, ArrayList<Integer> st){
		if(!hitGround){
			switch(command){
				case(0): turn(m,st); break;
				case(1): moveLeft(m); break;
				case(2): moveRight(m); break;
				case(3): break;
			}
		}
	}
}