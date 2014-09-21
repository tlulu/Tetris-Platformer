import java.awt.*;

/**
* Controls all actions taken by the character
* Move method need to be more effieient
*/
class Character{

	static final int SIZE = 10; //Size of the character
	private Collision c = new Collision(0,THA.HEIGHT,0,THA.WIDTH); //Check collision
	final double GRAVITY = 0.981; //falling speed
	double oriJumpSpeed; //jump speed of the character (used to reset jumpSpeed)
	double moveSpeed; //move speed of the character (used to reset speed incase acceleration is applied)
	private double xCoord; //x-coordinate of character
	private double yCoord; //y-coordinate of character
	private boolean fall = false; //fall down
	private double acceleration; //Gradually speed up from 0 - moveSpeed(not used)
	private double speed = 0; //Initial move speed
	private double jumpSpeed; //Initial jump speed
	private boolean moveLeft = false; //Character moving left
	private boolean moveRight = false; //Character moving right
	private boolean stop = true; //Character stoping
	private boolean jump = false; //Character jumping
	private boolean sprint = false; //Character sprinting (times speed by 3)
	private int countJump = 0; //Count the number of jumps (used in double jump)
	private boolean death = false; //Whether the character meets the death conditions
	private boolean underWater = false; //Whether the character is submerged in water
	private double breath = 0; //The breath counter of the character; starts once underwater = true
	private boolean ground=false; //Whether the character is stepping on ground or jumping in air
	private double breathBar;		//Draws a rectangle to see how much time left to stay underwater
	private final double breathTime=5;  //5 seconds to stay underwater
	
	public Character(double x, double y, double speed,double acceleration, double jumpSpeed){
		xCoord = x;
		yCoord = y;
		this.acceleration = acceleration;
		this.jumpSpeed = jumpSpeed;
		oriJumpSpeed = jumpSpeed;
		moveSpeed = speed;
	}
	
	/**
	 * Draws the character on the screen
	 * @param g Graphics
	*/
	public void putCharacter(Graphics g){
		g.setColor(Color.white);
		g.fillOval((int)(xCoord), (int)(yCoord), SIZE, SIZE);
		if (underWater){
			breathBar=(System.currentTimeMillis()-breath)/100;	
			g.fillRect((int)(xCoord)-20, (int)(yCoord)-20, (int)(breathTime*10-breathBar), 5);
		}
	}
	
	/**
	 * Character moves depend on boolean moveLeft, moveRight, stop, jump, and fall
	 * Character is constantly falling to simulate gravity
	 * col1 = bound collide
	 * col2 = map collide
	 * col3 = shape collide
	 * @param m Square[][] map
	 * @param b Square[][] shape
	*/
	public void move(Map m, Block b){
		int[] col1 = new int[5];  //Bound collide
		int[] col2 = new int[5];  //Map collide
		int[] col3 = new int[5];  //Shape collide
		if(!jump)
			fall(m,b,col1,col2,col3);
		if (moveLeft){
			speed = -moveSpeed;
			xCoord+=speed;
			col1 = c.boundCollide(this);
			col2 = c.objCollide(this,m.getMap());
			col3 = c.objCollide(this,b.getShape());
			if(col1[0]!=-1){
				xCoord=col1[1];
			}
			if(col2[0]!=-1){
				xCoord = col2[0];
			}
			if(col3[0]!=-1){
				xCoord = col3[0];
			}
		}
		if (moveRight){
			speed = moveSpeed;
			xCoord+=speed;
			col1 = c.boundCollide(this);
			col2 = c.objCollide(this,m.getMap());
			col3 = c.objCollide(this,b.getShape());
			if(col1[1]!=-1){
				xCoord=col1[1];
			}
			if(col2[1]!=-1){
				xCoord = col2[1];
			}
			if(col3[1]!=-1){
				xCoord = col3[1];
			}
		}
		if (stop){
			moveLeft = false;
			moveRight = false;
			speed = 0;
			stop = false;
		}
		if (jump){
			ground=false;
			if(jumpSpeed<=0){
				fall=true;
				jump = false;
			}
			yCoord-=jumpSpeed;
			jumpSpeed-=GRAVITY;
			col1 = c.boundCollide(this);
			col2 = c.objCollide(this,m.getMap());
			col3 = c.objCollide(this,b.getShape());
			if(col1[2]!=-1){
				yCoord=col1[2];
			}
			if(col2[2]!=-1){
				yCoord = col2[2];
			}
			if(col3[2]!=-1){
				yCoord = col3[2];
			}
		}
	}
	
	/**
	 * Lets the character fall down
	 * Accelerates due to gravity
	 * Once lands on ground, reset all jump values
	 * @param m Square[][] map
	 * @param b Square[][] shape
	 * @param col1 = int[][] bound collide output
	 * @param col2 = int[][] map collide output
	 * @param col3 = int[][] shape collide output
	*/
	public void fall(Map m,Block b,int[] col1,int[] col2,int[] col3){
		yCoord+=jumpSpeed;
		jumpSpeed+=GRAVITY;
		col1 = c.boundCollide(this);
		col2 = c.objCollide(this,m.getMap());
		col3 = c.objCollide(this,b.getShape());
		if(col1[3]!=-1){
			yCoord = col1[3];
			jump = false;
			countJump = 0;
			jumpSpeed = oriJumpSpeed;
			fall = false;
			ground=true;
		}
		if(col2[3]!=-1){
			yCoord = col2[3];
			jump = false;
			countJump = 0;
			jumpSpeed = oriJumpSpeed;
			fall = false;
			ground=true;
		}
		if(col3[3]!=-1){
			yCoord = col3[3];
			jump = false;
			countJump = 0;
			jumpSpeed = oriJumpSpeed;
			fall = false;
			ground=false;
		}
	}
	
	/**
	 * Sets the moveLeft variable to true
	 * At the same time sets the moveRight variable to false avoid confilct
	*/
	public void moveLeft(){
		if(!stop){
			moveLeft = true;
			moveRight = false;
		}
	}
	
	/**
	 * Sets the moveRight variable to true
	 * At the same time sets the moveLeft variable to false avoid confilct
	*/
	public void moveRight(){
		if(!stop){
			moveRight = true;
			moveLeft = false;
		}
	}
	public void setAcceleration(double value){
		acceleration += value;
	}
	
	/**
	 * Stops all moving left and right motion
	*/
	public void stop(){
		stop = true;
	}
	
	/**
	 * Allows the character to run 3 times as fast
	 * Not used
	*/
	public void sprintOn(){
		if(!sprint){
			sprint = true;
			moveSpeed*=3;
		}
	}
	
	/**
	 * Reset the speed of the character to the original speed
	 * Not used
	*/
	public void sprintOff(){
		if(sprint){
			sprint = false;
			moveSpeed/=3;
		}
	}
	
	/**
	 * Add the jumping speed by the desired value to increase in jump height
	 * Used for testing purposes
	 * @param height The speed to increase jumpSpeed by
	*/
	public void increaseJump(double height){
		oriJumpSpeed+=height;
		jumpSpeed = oriJumpSpeed;
	}
	
	/**
	 * Subtract the jumping speed by the desired value to decrease in jump height
	 * Used for testing purposes
	 * @param height The speed to decrease jumpSpeed by
	*/
	public void decreaseJump(double height){
		oriJumpSpeed=Math.max(0,oriJumpSpeed-height);
		jumpSpeed = oriJumpSpeed;
	}
	
	/**
	 * Returns the current x coordinate of the character
	 * @return double xCoord  The current x coordinate
	*/
	public double getX(){
		return xCoord;
	}
	
	/**
	 * Returns the current y coordinate of the character
	 * @return double yCoord  The current y coordinate
	*/
	public double getY(){
		return yCoord;
	}
	
	/**
	 * Returns if the character is jumping or not
	 * Used for testing purposes
	 * @return boolean jump  Whether the character is jumping
	*/
	public boolean returnJump(){
		return jump;
	}
	
	/**
	 * Allows the character to jump
	 * Stops falling temporarily
	*/
	public void jump(){
		if(!fall){
			fall = false;
			jump = true;
		}
		doubleJump();
	}
	
	/**
	 * The be used in conjunction with jump method
	 * Allows the character to jump once more in mid air
	 * Every time jump is pressed, countJump increases by 1
	 * While countJump is <= 2, the character is allow to jumped again by resetting the jumpSpeed
	*/
	public void doubleJump(){
		countJump++;
		if(countJump<=2){
			jumpSpeed=oriJumpSpeed;
			fall = false;
			jump = true;
		}
	}
	
	/**
	 * Conditions for death:
	 * Get crushed by the tetris shape from above
	 * Stay in water for more than 5 seconds
	 * @param Square[][] b  The tetris shape
	*/
	public void checkDeath(Square[][] s){
		int[] col3 = c.objCollide(this,s);
		if (!jump  &&ground && (col3[4]!=1)){
			death=true;
		}
		if (underWater){
			if (System.currentTimeMillis()-breath>5000 && breath!=0){
				death=true;
				breath=0;
			}else{
				death=false;
			}
		}
	}
	
	/**
	 * Start countdown once character is submerged in water
	 * @param double y  the height of the water
	*/
	public void checkUnderWater(double y){	
		if (yCoord>y && underWater==false){
			underWater=true;
			breath=System.currentTimeMillis();
		}else if (yCoord<y){
			underWater=false;
			breath=0;
			
		}
	}
	
	//Player winning condition
	//Checks if the player moves above the frame/map
	public boolean checkExit(){	
		if (yCoord<0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @return boolean death  Whether the character meets the death conditions
	*/
	public boolean getDeath(){
		return death;
	}
	
	//Check if the player's touching the top of the map
	public boolean getGround(){
		return ground;
	}
	
	public void changeX(double x){
		xCoord = x;
	}
	
	public void changeY(double y){
		yCoord = y;
	}
	
	//Set the death of player
	public void setDeath(boolean death){
		this.death = death;
	}
	
}
