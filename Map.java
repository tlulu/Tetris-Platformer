import java.awt.*;
import java.util.ArrayList;
/**
* The map is made up of Square objects in a 2-D array
* Controls all actions performed in the map
* Changing [this][] will move the block up or down
* Changing [][this] will move the block left or right
*/
class Map{

	private int maX, maY, miX, miY; //Boundaries of the map: Max x, Max y, Min x, Min y
	private Square[][] map; //Map stored in 2-D array
	private boolean lose;
	
	/**
	 * Constructor
	 * top, bottom, left, right must be a multiple of Square.SIZE
	 * @param top    Top of the playing field; normally 0
	 * @param bottom Bottom of the playing field
	 * @param left   Left of the playing field; normally 0
	 * @param right  Right of the playing field
	*/
	public Map(int top, int bottom, int left, int right){
		maX = right;
		miX = left;
		maY = bottom;
		miY = top;
		lose = false;
		
		//Setting up the map
		map = new Square[(bottom-top)/Square.SIZE][(right-left)/Square.SIZE];
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j] = new Square(j*Square.SIZE,i*Square.SIZE,Color.black,false,true,false);
			}
		}
	}
	
	/**
	 * Draws the map on the screen
	 * @param g Graphics
	*/
	public void drawMap(Graphics g){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j].drawBackGroundSquare(g);
				map[i][j].drawSpecialSquare(g);
			}
		}
	}
	
	
	/**
	 * Adding the shape to the map array
	 * Check whether each square in the shape is visible or not
	 * If it is, find the Square object in map with the same x and y coordinate as the Square object in shape
	 * Change the status of that Square to visible (solid = true)
	 * Change the color of the Square
	 * @param b The tetris shape
	*/
	public void addShape(Block b){
		Square[][] shape = b.getShape();
		Color c = shape[0][0].getColor();
		int x, y;
		for(int i=0;i<shape.length;i++){
			for(int j=0;j<shape[0].length;j++){
				if(shape[i][j].isReal()){
					x = shape[i][j].getX()/Square.SIZE;
					y = shape[i][j].getY()/Square.SIZE;
					if(y>=0 && y<THA.HEIGHT/Square.SIZE && x>=0 && x<THA.WIDTH/Square.SIZE){
						map[y][x].makeSolid(true);
						map[y][x].changeColor(c);
					}else if(y<THA.HEIGHT/Square.SIZE){
						lose = true;
					}
				}
			}
		}
	}
	
	/**
	 * Return the 2-D Square array map
	 * @return Square[][] map
	*/
	public Square[][] getMap(){
		return map;
	}
	
	/**
	 * Changes the Square[][] array into another Square[][] array
	 * Is able to change the map into a completely different map
	 * @param s Square[][] array that is to be changed into
	*/
	public void change(Square[][] s){
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				map[i][j].makeSolid(s[i][j].isReal());
				map[i][j].makeBackground(s[i][j].isMapBackground());
				map[i][j].changeSquare(s[i][j].isSquare());
				map[i][j].changeCoord(s[i][j].getX(),s[i][j].getY());
			}
		}
	}
	
	/**
	 * Debug purpose
	 * Print the map on CMD
	 * 1 represents visible Square
	 * 0 represents invisible Square
	*/
	public void printMap(){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j].isReal()){
					System.out.print("1 ");
				}else{
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Generate random visible Square objects at the bottom of the map
	 * Never goes past 1/3 height of the map
	*/
	public void randomGenerateMap(){
		int rand;
		int height = map.length/3*2;
		for(int i=map.length-1;i>=height;i--){
			for(int j=0;j<map[0].length;j++){
				rand = (int)(Math.random()*3);
				if(rand==0){
					map[i][j].makeSolid(true);
					map[i][j].changeColor(Color.green);
				}
			}
		}
	}
	
	/**
	 * Will move down everything above a certain index down 1 index
	 * Used in conjunction with clearLines
	 * @param int  line The index of the cleared line
	*/
	public void fallSpace(int line){
		for(int i=line-1;i>=0;i--){
			for(int j=0;j<THA.WIDTH/Square.SIZE;j++){
				if(map[i][j].isReal()){
					map[i][j].makeSolid(false);
					map[i+1][j].makeSolid(true);
					map[i+1][j].changeColor(map[i][j].getColor());
				}
			}
		}
	}
	
	/**
	 * Will return all lines that need to be cleared
	 * Check if each index is completely fill with Squares
	 * Used in conjunction with clearLines
	 * @return ArrayList<Integer>  the index all the lines that need to be clears
	*/
	public ArrayList<Integer> checkClearLines(){
		ArrayList<Integer> lines = new ArrayList<Integer>();
		boolean take = false;
		for(int i=0;i<THA.HEIGHT/Square.SIZE;i++){
			take = false;
			for(int j=0;j<THA.WIDTH/Square.SIZE;j++){
				if(map[i][j].isReal()){
					take = true;
				}else{
					take = false;
					break;
				}
			}
			if(take==true){
				lines.add(i);
				MyPanel.score+=100;
			}
		}
		return lines;
	}
	
	public void clearMap(){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j].makeSolid(false);
			}
		}
	}
	/**
	 * Take a index, and clear all Squares horizontal to that index
	*/
	public void clearLines(){
		ArrayList<Integer> line = checkClearLines();
		for(int i=0;i<line.size();i++){
			for(int j=0;j<THA.WIDTH/Square.SIZE;j++){
				map[line.get(i)][j].makeSolid(false);
			}
			fallSpace(line.get(i));
		}
	}
	
	/**
	 * Originally used in AI, flood fill algorthim finding all isolated shapes. May be useful for something else
	 * returns the number of holes that the map have
	 * a square is considered a hole if it's not visible and it's surronded by 4 visible squares
	 * @return int holes  total number of holes
	*/
/*  	public int connectedShape(ArrayList<Square> s,int total){
		//System.out.println(total);
		if(s.size()==0){
			return total;
		}
		int size = s.size();
		//System.out.println("Size "+size);
		for(int i =0;i<size;i++){
			int x = s.get(i).getX()/Square.SIZE;
			int y = s.get(i).getY()/Square.SIZE;
			//System.out.println(y+" "+x);
			map[y][x].makeMarked(true);
			total++;
			if(x+1<THA.WIDTH/Square.SIZE && map[y][x+1].isReal() && !map[y][x+1].isMarked()){
				s.add(map[y][x+1]);
				map[y][x+1].makeMarked(true);
				//total++;
			}
			if(x-1>=0 && map[y][x-1].isReal() && !map[y][x-1].isMarked()){
				s.add(map[y][x-1]);
				map[y][x-1].makeMarked(true);
				//total++;
			}
			if(y+1<THA.HEIGHT/Square.SIZE && map[y+1][x].isReal() && !map[y+1][x].isMarked()){
				s.add(map[y+1][x]);
				map[y+1][x].makeMarked(true);
				//total++;
			}
			if(y-1>=0 && map[y-1][x].isReal() && !map[y-1][x].isMarked()){
				s.add(map[y-1][x]);
				map[y-1][x].makeMarked(true);
				//total++;
			}
			//if(x+1<THA.WIDTH/Square.SIZE && y+1<THA.HEIGHT/Square.SIZE && map[y+1][x+1].isReal() && !map[y+1][x+1].isMarked()){
			//	s.add(map[y+1][x+1]);
			//	map[y+1][x+1].makeMarked(true);
			//	//total++;
			//}
			//if(x+1<THA.WIDTH/Square.SIZE && y-1>=0 && map[y-1][x+1].isReal() && !map[y-1][x+1].isMarked()){
			//	s.add(map[y-1][x+1]);
			//	map[y-1][x+1].makeMarked(true);
			//	//total++;
			//}
			//if(x-1>=0 && y-1>=0 && map[y-1][x-1].isReal() && !map[y-1][x-1].isMarked()){
			//	s.add(map[y-1][x-1]);
			//	map[y-1][x-1].makeMarked(true);
			//	//total++;
			//}
			//if(x-1>=0 && y+1<THA.HEIGHT/Square.SIZE && map[y+1][x-1].isReal() && !map[y+1][x-1].isMarked()){
			//	s.add(map[y+1][x-1]);
			//	map[y+1][x-1].makeMarked(true);
			//	//total++;
			//}
		}
		for(int i=0;i<size;i++){
			s.remove(0);
		}
		return connectedShape(s,total);
	}
	public int getConnectedBlocks(){
		ArrayList<Square> find = new ArrayList<Square>();
		int total = 0;
		int holes = 0;
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j].isReal() && !map[i][j].isMarked()){
						find.add(map[i][j]);
						holes=connectedShape(find,0);
						total++;
				}
			}
		}
		unMark();
		return total;
	}  */
	
	/**
	 * Determine the maximum height of the map from a starting index to ending index
	 * Used in AI
	 * @return int  Maximum height
	*/
	public int maxHeight(int s, int e){
		int maxHeight = 0;
		int temp = 0;
		for(int i=s;i<=e;i++){
			for(int j=0;j<map.length;j++){
				if(map[j][i].isReal()){
					temp = THA.HEIGHT/Square.SIZE-j;
					break;
				}
			}
			if(temp>maxHeight){
				maxHeight = temp;
			}
		}
		return maxHeight;
	}
	
	/**
	 * Get the total number of sides in that map. E.g: 1 visible square has 4 sides, 2 adjecant visible squares have 6 sides
	 * Used in AI
	 * @return int  Number of sides
	*/
	public int getNumSides(){
		int side = 0;
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j].isReal()){
					if(i+1<THA.HEIGHT/Square.SIZE && !map[i+1][j].isReal()){
						side++;
					}
					if(i-1>=0 && !map[i-1][j].isReal()){
						side++;
					}
					if(j+1<THA.WIDTH/Square.SIZE && !map[i][j+1].isReal()){
						side++;
					}
					if(j-1>=0 && !map[i][j-1].isReal()){
						side++;
					}
				}
			}
		}
		return side;
	}
	
	/**
	 * Get the number of Squares filled in the Block Square[][] after dropping
	 * Used in AI
	 * @return int  The number of gaps
	*/
	public int gapFilled(Block b){
		Square[][] s = b.getShape();
		int gap = s.length*s[0].length;
		int totalGap = s.length*s[0].length;
		int x, y;
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				x = s[i][j].getX()/Square.SIZE;
				y = s[i][j].getY()/Square.SIZE;
				if(x>=0 && x<THA.WIDTH/Square.SIZE && y>=0 && y<THA.HEIGHT/Square.SIZE){
					if(map[y][x].isReal()){
						gap--;
					}
				}else{
					gap--;
				}
			}
		}
		return gap;
	}
	
	/**
	 * Unmark all Square objects in map.
	 * Marking a Square object is useful in searches such as flood fill
	*/
	public void unMark(){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j].makeMarked(false);
			}
		}
	}
	public boolean returnLose(){
		return lose;
	}
	public void changeLose(boolean l){
		lose = l;
	}
}