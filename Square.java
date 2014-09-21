import java.awt.*;

/**
* The building block of the game
* Used to make tetris shapes and maps
* Each individual square will have its own x, y coordinate, and color
* The status of the square will be indicated by variable solid, mapBackground, and square
* The x, y coordinate, color, and status can be changed and obtained freely
*/
class Square{

	static final int SIZE = 20; //Size of each square
	protected int x; //x-coord of square
	protected int y; //y-coord of square
	protected Color c; //color of square
	
	//Status of the Square object
	protected boolean solid; //Whether the square is visible or not
	protected boolean mapBackground; //Whether the square is part of background or not
	protected boolean square; //Whether the square is a tetris shape or not
	protected boolean marked; //Used to help with flood fill in Map class
	
	public Square(int x, int y, Color c, boolean solid, boolean mapBackground,boolean square){
		this.x = x;
		this.y = y;
		this.c = c;
		this.solid = solid;
		this.mapBackground = mapBackground;
		this.square = square;
		marked = false;
	}
	
	/**
	 * Draws a regular square with the given size and color (not used)
	 * Only draws when the status of the Square is (solid and square) or (solid and map background) 
	 * @param g Graphics
	*/
	public void drawSquare(Graphics g){
		if((isReal()&&isSquare()||(isReal()&&isMapBackground()))){
			g.setColor(c);
			g.fillRect(x,y,SIZE,SIZE);
		}
	}
	
	/**
	 * Draws a special square with white outlines
	 * Only draws when the status of the Square is (solid and square) or (solid and map background)
	 * @param g Graphics
	*/
	public void drawSpecialSquare(Graphics g){
		if((isReal()&&isSquare()||(isReal()&&isMapBackground()))){
			g.setColor(Color.white);
			g.drawRect(x,y,SIZE,SIZE);
			g.setColor(c);
			g.fillRect(x+1, y+1, SIZE-1, SIZE-1);
		}
	}
	
	/**
	 * Draws a special map square
	 * Color is either black or dark grey depending on the x and y coordinate of the Square
	 * Only draws when the status of the Square is (solid and map background)
	 * @param g Graphics
	*/
	public void drawBackGroundSquare(Graphics g){
		if(isMapBackground()&&!isReal()){
			g.setColor(Color.black);
			if((x+y)%(SIZE*2)==0){
				g.setColor(new Color(20,20,20));
			}else{
				g.setColor(new Color(15,15,15));	
			}
			g.fillRect(x,y,SIZE,SIZE);
		}
	}
	
	/**
	 * @return  int x The x coordinate of the Square object
	*/
	public int getX(){
		return x;
	}
	
	/**
	 * @return  int y The y coordinate of the Square object
	*/
	public int getY(){
		return y;
	}
	
	/**
	 * @return  Color c The color of the Square object
	*/
	public Color getColor(){
		return c;
	}
	
	/**
	 * Returns if the Square object is visible or not
	 * @return  boolean solid
	*/
	public boolean isReal(){
		return solid;
	}
	
	/**
	 * Returns if the Square object belongs to the map
	 * @return  boolean mapBackground
	*/
	public boolean isMapBackground(){
		return mapBackground;
	}
	
	/**
	 * Returns whether the shaped is marked or not
	 * Used in Map class to help with flood fill checking
	 * @return boolean marked
	*/
	public boolean isMarked(){
		return marked;
	}
	
	/**
	 * Returns if the Square object belongs to the shape
	 * @return  boolean square
	*/
	public boolean isSquare(){
		return square;
	}
	
	/**
	 * Changes both the x and y coordinate of the Square object
	 * @param x The new x coordiante
	 * @param y The new y coordiante
	*/
	public void changeCoord(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Changes the x coordinate of the Square object
	 * @param x The new x coordiante
	*/
	public void changeX(int x){
		this.x = x;
	}
	
	/**
	 * Changes the y coordinate of the Square object
	 * @param y The new y coordiante
	*/
	public void changeY(int y){
		this.y = y;
	}
	
	/**
	 * Changes the color of the Square object
	 * @param color The new color
	*/
	public void changeColor(Color c){
		this.c = c;
	}
	
	/**
	 * Make the Square object either visible or not visible
	 * @param solid The new status
	*/
	public void makeSolid(boolean solid){
		this.solid = solid;
	}
	
	/**
	 * Make the Square object either part of the back ground or not
	 * @param background The new status
	*/
	public void makeBackground(boolean background){
		mapBackground = background;
	}
	
	/**
	 * Make the Square object either part of the shape or not
	 * @param square The new status
	*/
	public void changeSquare(boolean square){
		this.square = square;
	}
	
	/**
	 * Changes the boolean marked
	 * Used in Map class to help with flood fill checking
	 * @param boolean mark
	*/
	public void makeMarked(boolean mark){
		marked = mark;
	}
}