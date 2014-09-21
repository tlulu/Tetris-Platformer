import java.util.ArrayList;
import java.awt.*;

/**
 * AI for tetris solving
 * Command:
 * 0 = turn
 * 1 = move left
 * 2 = move Right
 * 3 = stop
*/
class AI{

	private boolean enable;
	private String difficulty; // difficulty can either be:
							   // "easy"
							   // "medium"
							   // "hard"
	private double[] weightFactors = new double[3];; // Difficulty is determined by how often the AI clears lines
									// Index:
									// 0: side
									// 1: height
									// 2: gap
	
	public AI(boolean enable, String difficulty){
		this.enable = enable;
		this.difficulty = difficulty;
		if(difficulty.equalsIgnoreCase("easy")){
			weightFactors[0] = 0.0; weightFactors[1] = 0.0; weightFactors[2] = 0.0;
		}else if(difficulty.equalsIgnoreCase("medium")){
			weightFactors[0] = 10.0; weightFactors[1] = 5; weightFactors[2] = 1.0;
		}else if(difficulty.equalsIgnoreCase("hard")){
			weightFactors[0] = 2.0; weightFactors[1] = 5.0; weightFactors[2] = 1.0;
		}else{
			//Default: medium
			weightFactors[0] = 2.0; weightFactors[1] = 1.5; weightFactors[2] = 1.0;
		}
	}
	
	/**
	 * Once a destination is found for the Block, it configures it into commands (0,1,2,3) which will get to the destination
	 * When the height of the map reaches a certain point, the AI will go into panic mode, aka moving randomly
	 * Command:
	 * 0 = turn
	 * 1 = move left
	 * 2 = move Right
	 * 3 = stop
	 
	 * @param m  Map
	 * @param b  Block Tetris shape
	 * @return moves  ArrayList<Integer> The commands for the shape
	*/
	public ArrayList<Integer> initAI(Map m, Block b){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		try{
			ArrayList<int[]> allPaths = findPath(m,b);
			if(allPaths.size()<=0){
				return panicMode();
			}
			int oriX, moveX, turn, index;
			index = (int)(Math.random()*allPaths.size());
			oriX = allPaths.get(index)[0];
			moveX = allPaths.get(index)[1];
			turn = allPaths.get(index)[2];	
			for(int j=0;j<turn;j++){
				moves.add(0);
			}
			if(oriX>=moveX){
				for(int j = moveX;j<oriX;j++){
					moves.add(1);
				}
			}else{
				for(int j=oriX;j<moveX;j++){
					moves.add(2);
				}
			}
		}catch(Exception e){
			moves = panicMode();
		}
		return moves;
		//return panicMode();
	}
	
	/**
	 * Checks by adding every possible position of the tetris piece to the map
	 * Then calculate factors: sides, height, and gap
	 
	 * Sides: The number of sides the map have. E.g: 1 visible square has 4 sides, 2 adjecant visible squares have 6 sides
	 * Height: The height of the map from the beginning of the shape to the end of the shape
	 * Gap: The number of Squares filled in the Block Square[][] after dropping
	 
	 * Multiply each factor by their respective weight factor then add them
	 * The destination with the highest score will be returned
	 * More than 1 destination may be returned, therefore the choice will be randomly decided
	 
	 * @param m  Map
	 * @param b  Block Tetris shape
	 * @return pathInfo  ArrayList<int[]> The destination(s) of the shape
	*/
	public ArrayList<int[]> findPath(Map m, Block b){
	
		ArrayList<int[]> pathInfo = new ArrayList<int[]>();
		ArrayList<ArrayList> finalMove = new ArrayList<ArrayList>();
		
		//Make a replacement of the original Map and Block class
		//Use replacement for checking
		Map m2 = new Map(0,THA.HEIGHT,0,THA.WIDTH);
		m2.change(m.getMap());
		Block b2 = new Block(0,0,new Color(0,0,128));
		b2.change(b.getShape());
		
		int[] info; // Stores the information used to determine move
					// index:
					// 0: current x position
					// 1: best x position
					// 2: number of turns
		
		//Initialize variables						 
		int curPos = b.getShape()[0][0].getX()/Square.SIZE; //Current position of the Block, does not change
		int[] shapeIndex; //The index range containing the actual tetris piece, USED IN CHECKING HEIGHT
		double height; //The height of the map within a specific index range, used to determine move 
		double gap; //The gap not filled in the Block Square[][] once it's added to map, used to determine move
		double sides; //The number of shape sides in the map, used to determine move
		int maxSides = m.getNumSides(); //The number of shape sides in the map before adding the Block
		int maxHeight = THA.HEIGHT/Square.SIZE; //The maximum height of the map
		int maxGap = b2.getShape().length*b2.getShape()[0].length; //The maximum gaps in Block Square[][].
		double total; //total score of all factors
		double maxTotal = 0;
		
		//Determining move...
		 for(int i=0;i<4;i++){ //number of turns 0-3
			for(int j=0;j<THA.WIDTH/Square.SIZE;j++){ //number of horizontal positions on the map
				
				//Since we are using b and m as reference to reset b2 and m2, therefore it's best to not change them during checking. Therefore 3 more for loops are needed below.
				
				//Turns the shape to the desired position
				for(int l=0;l<i;l++){
					b2.turn();
				}
				
				//Moves the shape to the left most position on the map
				//*Note: it's important to turn first then move because turning on edges may cause bugs
				for(int k=0;k<THA.WIDTH/Square.SIZE;k++){
					b2.moveLeft(m2);
				}
				
				//Moves the shape to the desired x position
				for(int n = 0;n<j;n++){
					b2.moveRight(m2);
				}
				
				//Drop the shape and add it to the map
				b2.completeFall(m2);
				m2.addShape(b2);
				
				//Storing information about the moves
				shapeIndex = b2.getWidthIndex();
				info = new int[3];
				info[0] = curPos; info[1] = j-shapeIndex[0]; info[2] = i;
				
				//Begin checking:
				sides = calcSides(m2,maxSides);
				height = calcHeight(m2,b2,maxHeight,shapeIndex);
				gap = calcGap(m2,b2,maxGap);
				total = sides+height+gap;
				if(total>maxTotal){
					pathInfo = new ArrayList<int[]>();
					pathInfo.add(info);
					maxTotal = total;
				}else if(total==maxTotal && maxTotal!=0){
					pathInfo.add(info);
				}
				
				//Resetting m2 and b2
				b2.change(b.getShape());
				b2.changeHitGround(false);
				m2.change(m.getMap());
			}
		}
		return pathInfo;
	}
	
	/**
	 * Calculate the score for the sides
	 * @param m  Map
	 * @param maxSides  int the original number of sides before adding the shape
	 * @return  double score for the factor
	*/
	public double calcSides(Map m, int maxSides){
		int sides = m.getNumSides();
		double weightFactor = weightFactors[0];
		return (maxSides-sides)*weightFactor;
	}
	
	/**
	 * Calculate the score for the height
	 * @param m  Map
	 * @param b  Block tetris shape
	 * @param maxHeight  int the maximum height the map can go to
	 * @return  double score for the factor
	*/
	public double calcHeight(Map m, Block b, int maxHeight,int[] shapeIndex){
		int height = m.maxHeight(b.getShape()[0][0].getX()/Square.SIZE+shapeIndex[0],b.getShape()[0][0].getX()/Square.SIZE+shapeIndex[1]);
		double weightFactor = weightFactors[1];
		return (maxHeight-height)*weightFactor;
	}
	
	/**
	 * Calculate the score for the gap
	 * @param m  Map
	 * @param b  Block tetris shape
	 * @param maxGap  int the maximum number of gaps a shape can have
	 * @return  double score for the factor
	*/
	public double calcGap(Map m, Block b, int maxGap){
		int gap = m.gapFilled(b);
		double weightFactor = weightFactors[2];
		return (maxGap-gap)*weightFactor;
	}
	
	/**
	 * Used when the height of the map reaches a certain point
	 * Overrides all other AI choices
	 * Randomly generate commands
	 * @return moves  ArrayList<Integer> The commands for the shape
	*/
	public ArrayList<Integer> panicMode(){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		int command, moveDist;
		for(int i=0;i<50;i++){
			command = (int)(Math.random()*4);
			if(command!=0){
				moveDist = (int)(Math.random()*6)+1;
				if(command==3){
					moveDist/=2;
				}
				for(int j=0;j<moveDist;j++){
					moves.add(command);
				}
			}else{
				moves.add(command);
			}
		}
		return moves;
	}
	
	/**
	 * @return boolean  Whether the AI is enabled or not
	*/
	public boolean getAI(){
		return enable;
	}
	
	/**
	 * @return String  The difficulty of the AI
	*/
	public String getDifficulty(){
		return difficulty;
	}
}