import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

/*
 Final Summative THA: Tetris Heli Attack
 By: Tony Lu, David Dong
 Jan/21/2013
 Ms.S
*/
/*
 Version 2 minor fix:
 Game game will now exit when the player win or dies
 You can still submit scores, but it's only visible if you run the program again
*/

class MyPanel extends JPanel implements ActionListener, KeyListener,MouseListener{
	private Character character;
	private Map map; //map of the screen
	private GhostShape gs = new GhostShape();  //shape that helps guide the shapes
	private NextShapeToolBar ns = new NextShapeToolBar(6*Square.SIZE,24*Square.SIZE);
	private Ammo ammo = new Ammo();  //ammo for weapons
	private AI shapeAI;
	private ArrayList<Integer> aiCommand;
	private ArrayList<Integer> shapeTrack = new ArrayList<Integer>();
	private Timer moveTime = new Timer(10,this);
	private Timer fallTime = new Timer(100,this);
	private Timer bulletMoveTime = new Timer(10,this);
	private Timer aiMove = new Timer(50,this);
	private Timer buttonTimer = new Timer(500,this);
	private Timer bulletTime = new Timer(500,this);
	private Timer grenadeTime = new Timer(2000,this);
	private Timer laserTime = new Timer(5000,this);
	private Timer shotgunTime = new Timer(10000,this);
	private Timer waterTime=new Timer(1000,this);
	private ArrayList<Block> shapes = new ArrayList<Block>();
	private ArrayList<Weapons> weaponList=new ArrayList<Weapons>();	
	private Water water;
	static int guntrack;
	private String weapons[]={"gun","grenade","laser","shotgun"};
	private boolean bshoot=true;
	private boolean gshoot=true;
	private boolean lshoot=true;
	private boolean sgshoot=true;
	static boolean pause = true;
	static int score = 0;
	
	private double startWater;
	boolean ss=true;
	
	public MyPanel(int x, int y, boolean ai, String difficulty){
		this.setSize(x,y);
		map = new Map(0,THA.HEIGHT,0,THA.WIDTH);
		shapeAI = new AI(ai,difficulty);
		map.randomGenerateMap();
		map.clearLines();
		character = new Character(100,THA.HEIGHT/2-40,5,0.1,10);
		water = new Water(0,THA.WIDTH,0,THA.HEIGHT,5);
		addKeyListener(this);
		addMouseListener(this);
		for(int i=0;i<6;i++){
			addShape();
		}
		startWater=System.currentTimeMillis();
		buttonTimer.start();
	}
	public void drawShape(Square[][] t){
		for(int i=0;i<t.length;i++){
			for(int j=0;j<t[0].length;j++){
				if(t[i][j].isReal()){
					System.out.print("1 ");
				}else{
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.drawMap(g);
		gs.imitate(shapes.get(0),map);
		gs.draw(g);
		if(shapes.size()>0)
			shapes.get(0).draw(g);
		character.putCharacter(g);    //draws the character
		for (int i=0;i<weaponList.size();i++){		
				weaponList.get(i).draw(g);  //draws each weapon object
		}
		water.draw(g);   //draws the water image
		g.setColor(Color.black);	
		g.drawString("score: ",300,350);
	}
	/**
	 * Fires weapons when left mouse is pressed
	*/
	
	public void mousePressed(MouseEvent evt) {	
		int mx = evt.getX();  // x-coordinate where user clicked.
		int my = evt.getY(); // y-coordinate where user clicked.
		if(!pause){
			if ( !evt.isMetaDown() ) {
				if (weapons[guntrack].equals("gun") && bshoot){
					weaponList.add(new Bullet(mx,my,character.getX(),character.getY(),0,THA.WIDTH,0,THA.HEIGHT,7));
					bshoot=false;
					ammo.useBullet();
					bulletTime.restart();
				}
				if (weapons[guntrack].equals("grenade")&& gshoot){
					weaponList.add(new Grenade(mx,my,character.getX(),character.getY(),0,THA.WIDTH,0,THA.HEIGHT,12));
					gshoot=false;
					ammo.useGrenade();
					grenadeTime.restart();
				}
				if (weapons[guntrack].equals("laser") && lshoot){
					weaponList.add(new Laser(mx,my,character.getX(),character.getY(),0,THA.WIDTH,0,THA.HEIGHT,20));
					lshoot=false;
					ammo.useLaser();
					laserTime.restart();
				}
				if (weapons[guntrack].equals("shotgun") && sgshoot){
					weaponList.add(new ShotGun(mx,my,character.getX(),character.getY(),0,THA.WIDTH,0,THA.HEIGHT,7,character.getX(),character.getY(),25,90,weaponList));
					sgshoot=false;
					ammo.useShotgun();
					shotgunTime.restart();
				}
			}
		}
	}
	public void mouseEntered(MouseEvent evt) { }  
	public void mouseExited(MouseEvent evt) { }   
	public void mouseClicked(MouseEvent evt) { }  
	public void mouseReleased(MouseEvent evt) { }
	
	/**
	 * Controls the movements and actions of character and tetris pieces
	*/
	public void keyPressed(KeyEvent key) {
		String keyText = KeyEvent.getKeyText(key.getKeyCode());
		if(!pause){
			if(!shapeAI.getAI()){
				if(keyText.equalsIgnoreCase("a")){
					shapes.get(0).moveLeft(map);
					shapeTrack.add(1);
				}
				if(keyText.equalsIgnoreCase("d")){
					shapes.get(0).moveRight(map);
					shapeTrack.add(2);
				} 
				if(keyText.equalsIgnoreCase("w")){
					shapes.get(0).turn(map,shapeTrack);
				}
			}
			if(keyText.equalsIgnoreCase("left")){
				character.moveLeft();
			}
			if(keyText.equalsIgnoreCase("right")){
				character.moveRight();
			}
			if(keyText.equalsIgnoreCase("up")){
				character.jump();
			}
			if (keyText.equalsIgnoreCase("Slash")){		 
				guntrack+=1;
				if (guntrack>weapons.length-1){
					guntrack=0;
				}
				  
			}
		}
		repaint();
	}
	public void keyReleased(KeyEvent key) {
		String keyText = KeyEvent.getKeyText(key.getKeyCode());
		if(!pause){
			if(keyText.equalsIgnoreCase("left")){
				character.stop();
			}
			if(keyText.equalsIgnoreCase("right")){
				character.stop();
			}
		}
		repaint();
	}
	public void keyTyped(KeyEvent key) {}
	
	/**
	 * Checks the timers and start actions according to it
	*/
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==buttonTimer){
			//From the Btns class
			if(Btns.startB){
				unPause();
				this.requestFocus();
			}
			if(Btns.pauseB){
				pause();
				this.requestFocus();
			}
		}
		if(e.getSource()==fallTime){
				shapes.get(0).fall(map,character);
				repaint();
				if(shapes.get(0).hitGround()){
					map.addShape(shapes.get(0));
					map.clearLines();
					shapes.remove(0);
					addShape();
				}
		}
		if(e.getSource()==aiMove){
			if(aiCommand.size()>0){
				shapes.get(0).aiCommand(map,aiCommand.get(0),shapeTrack);
				if(aiCommand.get(0)!=0){
					shapeTrack.add(aiCommand.get(0));
				}
				aiCommand.remove(0);
				repaint();
			}
		}
		if(e.getSource()==moveTime){
			character.checkUnderWater(water.getY());
			character.checkDeath(shapes.get(0).getShape());		
			character.move(map,shapes.get(0));
			if (character.checkExit()==true){
				MyFrame.screen="gameover";
				pause();
				character.setDeath(false);
				
			}
			if (character.getDeath()==true){
				pause();
				MyFrame.screen="gameover";
				character.setDeath(false);
				
				
			}
		}
		if(e.getSource()==bulletMoveTime){
			for (int i=0;i<weaponList.size();i++){	
				weaponList.get(i).move((int)character.getX(), (int)character.getY());
				if(!weaponList.get(i).getWeapon().equalsIgnoreCase("laser")){
					int[] col = weaponList.get(i).collide(shapes.get(0),map);
					if(col[2]==0){
						map.change(weaponList.get(i).damage(map.getMap(),col[1]/Square.SIZE,col[0]/Square.SIZE,THA.HEIGHT/Square.SIZE,THA.WIDTH/Square.SIZE));
					}else if(col[2]==1){
						shapes.get(0).change(weaponList.get(i).damage(shapes.get(0).getShape(),shapes.get(0).getIndex(col[0],col[1])[0],shapes.get(0).getIndex(col[0],col[1])[1],4,4));
					}
					i = removeBullet(i);
					if(shapes.get(0).shapeGone()){
						shapes.remove(0);
						addShape();
					}
				}else{
					map.change(weaponList.get(i).damage(map.getMap(),(int)(character.getX()/Square.SIZE),(int)(character.getY()/Square.SIZE),THA.HEIGHT/Square.SIZE,THA.WIDTH/Square.SIZE));
					
					
				}
				if (weaponList.get(i).getRemove()==true){
						weaponList.remove(i);
				}
			}
			
		}
		if (System.currentTimeMillis()-startWater>100*500 && startWater!=0){
			if(e.getSource()==waterTime){ 
				water.move();
			}
		}
		//delays for weapons
		if(e.getSource()==bulletTime){
			bshoot=true;
		}
		if(e.getSource()==grenadeTime){
			gshoot=true;
		}
		if(e.getSource()==laserTime){
			lshoot=true;
		}
		if(e.getSource()==shotgunTime){
			sgshoot=true;
		}
		repaint();
	}
	public int removeBullet(int i){
		if(weaponList.get(i).getRemove()){
			return i--;
		}
		return i;
	}
	//Add a random tetris shape to the arraylist
	//Add the shape to the tool bar
	//Also find the ai command when ai is enabled
	public void addShape(){
		shapeTrack = new ArrayList<Integer>();
		int n = (int)(Math.random()*7);
		switch(n){
			case(0): shapes.add(new Block(THA.WIDTH/2-40,-80,new Color(0,0,128))); break;
			case(1): shapes.add(new Line(THA.WIDTH/2-40,-80,new Color(75,0,130))); break;
			case(2): shapes.add(new LeftZ(THA.WIDTH/2-40,-80,new Color(255,215,0))); break;
			case(3): shapes.add(new RightZ(THA.WIDTH/2-40,-80,new Color(255,20,147))); break;
			case(4): shapes.add(new T(THA.WIDTH/2-40,-80,new Color(205,0,0))); break;
			case(5): shapes.add(new LeftL(THA.WIDTH/2-40,-80,new Color(255,165,0))); break;
			case(6): shapes.add(new RightL(THA.WIDTH/2-40,-80,new Color(0,128,0))); break;
			default: System.out.println("Impossible Error!!!"); break;
		}
		ns.clear();
		ns.addShape(shapes);
		if(shapeAI.getAI()){
			aiCommand = shapeAI.initAI(map,shapes.get(0));
		}
	}
	public void pause(){
		pause = true;
		moveTime.stop();
		fallTime.stop();
		bulletMoveTime.stop();
		waterTime.stop();
		bulletTime.stop();
		grenadeTime.stop();
		laserTime.stop();
		shotgunTime.stop();
		if(shapeAI.getAI()){
			aiMove.stop();
		}
	}
	public void unPause(){
		pause = false;
		moveTime.start();
		fallTime.start();
		bulletMoveTime.start();
		waterTime.start();
		bulletTime.start();
		grenadeTime.start();
		laserTime.start();
		shotgunTime.start();
		if(shapeAI.getAI()){
			aiMove.start();
		}
	}
	//returns the frame to MyFrame
	public NextShapeToolBar returnNs(){
		return ns;
	}
	
	//returns the frame to MyFrame
	public Ammo returnAmmo(){
		return ammo;
	}
}

/**
 * Tool bar for the next 4 shapes
*/
class NextShapeToolBar extends JPanel{
	private Square[][] box;
	static ArrayList<Block> s = new ArrayList<Block>();
	public NextShapeToolBar(int x, int y){
		this.setPreferredSize(new Dimension(x,y));
		box = new Square[6][24];
		for(int i=0;i<box.length;i++){
			for(int j=0;j<box[0].length;j++){
				box[i][j] = new Square(i*Square.SIZE,j*Square.SIZE,Color.black,false,true,false);
			}
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		for(int i=0;i<box.length;i++){
			for(int j=0;j<box[0].length;j++){
				box[i][j].drawBackGroundSquare(g);
				box[i][j].drawSpecialSquare(g);
			}
		}
		drawSpecialLines(g);
	}
	
	//Borders for decoration
	public void drawSpecialLines(Graphics g){
		for(int i=0;i<11;i++){
			if(i%2==0){
				g.setColor(new Color(51,51,51));
			}else{
				g.setColor(new Color(255,193,37));
			}
			g.drawRect(0+i,0+i, box.length*Square.SIZE-i*2, box[0].length*Square.SIZE-i*2);
		}
	}
	
	//Add the shapes to the box array
	public void addShape(ArrayList<Block> shapes){
		Square[][] s;
		int add;
		for(int i=1;i<shapes.size()-1;i++){
			s = shapes.get(i).getShape();
			add = s[0].length+1;
			for(int j=0;j<s.length;j++){
				for(int k=0;k<s[0].length;k++){
					if(s[k][j].isReal()){
						box[j+1][k+add*(i-1)+1].makeSolid(true);
						box[j+1][k+add*(i-1)+1].changeColor(s[j][k].getColor());
					}
				}
			}
		}
		repaint();
	}
	
	//Clear the box array
	public void clear(){
		for(int i=0;i<box.length;i++){
			for(int j=0;j<box[0].length;j++){
				box[i][j] = new Square(i*Square.SIZE,j*Square.SIZE,Color.black,false,true,false);
			}
		}
	}
}

/**
 * Has pause and start buttons
*/
class Btns extends JPanel implements ActionListener, KeyListener{
	private JButton pause = new JButton("Pause");
	private JButton start = new JButton("Start");
	static boolean startB = false;
	static boolean pauseB = true;
	public Btns(int x, int y){
		this.setSize(x,y);
		this.setLayout(new FlowLayout());
		this.add(pause);
		this.add(start);
		pause.addActionListener(this);
		start.addActionListener(this);
		pause.addKeyListener(this);
		start.addKeyListener(this);
		this.setFocusable(true);
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("start")){
			//System.out.println("start");
			startB = true;
			pauseB = false;
		}else if(command.equalsIgnoreCase("pause")){
			//System.out.println("end");
			startB = false;
			pauseB = true;
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
class TitleScreen extends JPanel implements ActionListener{
	private ImageIcon intro;
	private ImageIcon tetris;
	private JButton start = new JButton("Start");
	private JButton highScore = new JButton("Highscore");
	private JButton instructions = new JButton("Instructions");
	private JButton exit = new JButton("Exit");
	private String screen = "title";
	public TitleScreen(int x, int y){
		this.setSize(x,y);
		try{
			intro = new ImageIcon("introScreen.jpg");
			tetris = new ImageIcon("tetrisWord.png");
		}catch(Exception e){};
		start.setPreferredSize(new Dimension(80, 40));
		highScore.setPreferredSize(new Dimension(150, 40));
		instructions.setPreferredSize(new Dimension(150, 40));
		exit.setPreferredSize(new Dimension(80, 40));
		start.addActionListener(this);
		highScore.addActionListener(this);
		instructions.addActionListener(this);
		exit.addActionListener(this);
		this.add(Box.createRigidArea(new Dimension(10, this.getHeight()/2-50)));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,this.getWidth(),25));
		this.add(start);
		this.add(instructions);
		this.add(highScore);
		this.add(exit);
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(intro.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawImage(tetris.getImage(), 90, 100,this.getWidth()-90*2 , this.getHeight()/2-100, null);	
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Start")){
			MyFrame.screen = "mode";
		}else if(command.equalsIgnoreCase("Highscore")){
			MyFrame.screen = "highscore";
		}else if(command.equalsIgnoreCase("Exit")){
			System.exit(0);
		}else if(command.equalsIgnoreCase("Instructions")){
			MyFrame.screen = "instructions";
		}
	}
	public String getScreen(){
		return screen;
	}
}
class Mode extends JPanel implements ActionListener{
	private ImageIcon intro;
	private JButton m1 = new JButton("Player VS Player");
	private JButton m2 = new JButton("Player VS AI");
	private JButton back = new JButton("Back");
	private JLabel l = new JLabel("Mode",SwingConstants.CENTER);
	private String screen = "mode";
	public Mode(int x, int y){
		this.setSize(x,y);
		try{
			intro = new ImageIcon("introScreen.jpg");
		}catch(Exception e){}
		m1.setPreferredSize(new Dimension(150,40));
		m2.setPreferredSize(new Dimension(150,40));
		back.setPreferredSize(new Dimension(80,40));
		l.setPreferredSize(new Dimension(150,100));
		l.setFont(new Font("Serif", Font.BOLD, 60));
		l.setForeground(Color.yellow);
		m1.addActionListener(this);
		m2.addActionListener(this);
		back.addActionListener(this);
		this.setLayout(new FlowLayout(FlowLayout.CENTER,this.getWidth(),100));
		this.add(l);
		this.add(m1);
		this.add(m2);
		this.add(back);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(intro.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Player VS Player")){
			MyFrame.screen = "play";
			MyFrame.ai = false;
		}else if(command.equalsIgnoreCase("Player VS AI")){
			MyFrame.screen = "difficulty";
			MyFrame.ai = true;
		}else if(command.equalsIgnoreCase("Back")){
			MyFrame.screen = "title";
		}
	}
}
class Difficulty extends JPanel implements ActionListener{
	private ImageIcon intro;
	private JButton easy = new JButton("Easy");
	private JButton medium = new JButton("Medium");
	private JButton hard = new JButton("Hard");
	private JButton back = new JButton("Back");
	private JLabel l = new JLabel("Difficulty",SwingConstants.CENTER);
	private String screen = "difficulty";
	static String difficulty = "medium";
	public Difficulty(int x, int y){
		this.setSize(x,y);
		try{
			intro = new ImageIcon("introScreen.jpg");
		}catch(Exception e){}
		easy.setPreferredSize(new Dimension(100,40));
		medium.setPreferredSize(new Dimension(100,40));
		hard.setPreferredSize(new Dimension(100,40));
		back.setPreferredSize(new Dimension(80,40));
		l.setPreferredSize(new Dimension(250,100));
		l.setFont(new Font("Serif", Font.BOLD, 50));
		l.setForeground(Color.yellow);
		easy.addActionListener(this);
		medium.addActionListener(this);
		hard.addActionListener(this);
		back.addActionListener(this);
		this.setLayout(new FlowLayout(FlowLayout.CENTER,this.getWidth(),75));
		this.add(l);
		this.add(easy);
		this.add(medium);
		this.add(hard);
		this.add(back);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(intro.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Easy")){
			difficulty = "easy";
			MyFrame.screen = "play";
			MyFrame.diff="easy";
		}else if(command.equalsIgnoreCase("Medium")){
			difficulty = "medium";
			MyFrame.screen = "play";
			MyFrame.diff="medium";
		}else if(command.equalsIgnoreCase("Hard")){
			difficulty = "hard";
			MyFrame.screen = "play";
			MyFrame.diff="hard";
		}else if(command.equalsIgnoreCase("Back")){
			MyFrame.screen = "title";
		}
	}
}
class GameOver extends JPanel implements ActionListener{
	private ImageIcon gameOver;
	private JTextField name = new JTextField(7);
	private JButton back = 	new JButton("Exit");
	private JButton sub = 	new JButton("Submit");
	
	public GameOver(){
		try{
			gameOver = new ImageIcon("gameOver.png");
		}catch(Exception e){}
		back.addActionListener(this);
		name.addActionListener(this);
		sub.addActionListener(this);
		back.setBounds(325,600,80,40);
		name.setBounds(325,500,80,40);
		sub.setBounds(325,700,80,40);
		this.setLayout(new FlowLayout(FlowLayout.CENTER,this.getWidth(),75));
		this.add(name);
		this.add(back);
		this.add(sub);
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setBackground(Color.black);
		g.drawImage(gameOver.getImage(),80,100,300,300,null);
		
		
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Exit")){ //Exits the game
			System.exit(0);
			//MyFrame.screen = "title";
			//MyFrame.once=true;
			

		}else if(command.equalsIgnoreCase("Submit")){
			String user=name.getText();			//name of user
			if(user.equalsIgnoreCase("") || user==null){
				user = "anyomonous";
			}
			try {
				Highscore.write(MyPanel.score,user);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
			//MyFrame.screen = "title";
			//MyFrame.once=true;
			
		}	
	}

}

/**
 * Tool bar for the character
 * Include ammo recharge time
 * Include score
*/
class Ammo extends JPanel implements ActionListener{
	private ImageIcon bullet;
	private ImageIcon grenade;
	private ImageIcon laser;
	private ImageIcon shotgun;
	
	//Ammo delay times
	private int bulletDelay = 500;
	private int grenadeDelay = 2000;
	private int laserDelay = 5000;
	private int shotgunDelay = 10000;
	
	//Ammo recharge bar length
	private double bl = 50;
	private double gl = 50;
	private double ll = 50;
	private double sl = 50;
	
	//Score
	private int score = 0;
	private JLabel l = new JLabel("Score");
	private JLabel s = new JLabel(score+"");
	private Timer increase = new Timer(10,this);
	private int gunTrack = 0;
	public Ammo(){
		//this.setSize(134,480);
		this.setPreferredSize(new Dimension(134,480));
		try{
			bullet = new ImageIcon("bullet.png");
			grenade = new ImageIcon("grenade.jpg");
			laser = new ImageIcon("laser.jpg");
			shotgun = new ImageIcon("shotgun.jpg");
		}catch(Exception e){}
		l.setBounds(40,0,100,30);
		l.setFont(new Font("Serif", Font.BOLD, 14));
		l.setForeground(Color.yellow);
		s.setBounds(90,0,100,30);
		s.setFont(new Font("Serif", Font.BOLD, 14));
		s.setForeground(Color.yellow);
		increase.start();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setBackground(Color.black);
		add(l);
		add(s);
		drawSpecialLines(g);
		g.setColor(Color.white);
		g.fillOval(30,100,75,75);
		g.setColor(Color.blue);
		g.fillRect(getWidth()/2,getHeight()/2,(int)bl,10);
		g.fillRect(getWidth()/2,getHeight()/2+40,(int)gl,10);
		g.fillRect(getWidth()/2,getHeight()/2+80,(int)ll,10);
		g.fillRect(getWidth()/2,getHeight()/2+120,(int)sl,10);
		g.drawImage(bullet.getImage(),30,getHeight()/2-10,20,20,null);
		g.drawImage(grenade.getImage(),30,getHeight()/2+40-10,20,20,null);
		g.drawImage(laser.getImage(),30,getHeight()/2+80-10,20,20,null);
		g.drawImage(shotgun.getImage(),30,getHeight()/2+120-10,20,20,null);
		g.setColor(Color.yellow);
		if(gunTrack==0){
			g.drawRect(30,getHeight()/2-10,20,20);
		}else if(gunTrack==1){
			g.drawRect(30,getHeight()/2+40-10,20,20);
		}else if(gunTrack==2){
			g.drawRect(30,getHeight()/2+80-10,20,20);
		}else{
			g.drawRect(30,getHeight()/2+120-10,20,20);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==increase){
			if(!MyPanel.pause){
				gunTrack = MyPanel.guntrack;
				score = MyPanel.score;
				s.setText(score+"");
				if(bl<50){
					bl+=50/(bulletDelay/10.0);
				}
				if(gl<50){
					gl+=50/(grenadeDelay/10.0);
				}
				if(ll<50){
					ll+=50/(laserDelay/10.0);
				}
				if(sl<50){
					sl+=50/(shotgunDelay/10.0);
				}
			}
		}
		repaint();
	}
	
	//Border
	public void drawSpecialLines(Graphics g){
		for(int i=0;i<11;i++){
			if(i%2==0){
				g.setColor(new Color(51,51,51));
			}else{
				g.setColor(new Color(255,193,37));
			}
			g.drawRect(0+i,0+i, this.getWidth()-i*2, this.getHeight()-i*2);
		}
	}
	public void useBullet(){
		bl = 0;
	}
	public void useGrenade(){
		gl = 0;
	}
	public void useLaser(){
		ll = 0;
	}
	public void useShotgun(){
		sl = 0;
	}
}
class Instruction extends JPanel implements ActionListener{
	private ImageIcon intro;
	private JLabel ins1 = new JLabel("Arrow keys to move character");
	private JLabel ins2 = new JLabel("Left click to shoot");
	private JLabel ins3 = new JLabel("/ key to switch weapons");
	private JLabel ins4 = new JLabel("For 2 players:");
	private JLabel ins5 = new JLabel("a and d to move tetris piece");
	private JLabel ins6 = new JLabel("w to turn tetris shape");
	private JLabel ins7 = new JLabel("Character will die when crushed or drowned");
	private JLabel ins8 = new JLabel("Tetris player will die when the shapes stack above the map");
	private JLabel ins9 = new JLabel("Character will win when he's reached the top of the map");
	private JLabel[] allIns = {ins1,ins2,ins3,ins4,ins5,ins6,ins7,ins8,ins9};
	private JButton back = new JButton("Back");
	public Instruction(int x, int y){
		this.setSize(x,y);
		try{
			intro = new ImageIcon("introScreen.jpg");
		}catch(Exception e){}
		this.add(Box.createRigidArea(new Dimension(10, 15)));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,this.getWidth(),25));
		for(int i=0;i<allIns.length;i++){
			allIns[i].setFont(new Font("Serif", Font.BOLD, 18));
			allIns[i].setForeground(Color.yellow);
			this.add(allIns[i]);
		}
		back.setPreferredSize(new Dimension(80,40));
		back.addActionListener(this);
		this.add(back);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(intro.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Back")){
			MyFrame.screen = "title";
		}
	}
}


/**
 * Contains all the panels of the game
*/
class MyFrame extends JFrame {
	
	//Size of the frame
	static final int HEIGHT = 700;
	static final int WIDTH = 460;
	
	private Container c;
	static MyPanel panel;
	private Btns btns = new Btns(WIDTH,HEIGHT-THA.HEIGHT);
	private TitleScreen ts = new TitleScreen(WIDTH,HEIGHT);	
	private Mode mode = new Mode(WIDTH,HEIGHT);
	private Difficulty dif = new Difficulty(WIDTH,HEIGHT);
	private Instruction instructions = new Instruction(WIDTH,HEIGHT);
	private Highscore hs;
	private  GameOver gg;
	public static String screen = "title";
	public static String diff = "medium";
	public static boolean ai = false;
	static boolean once=true; //Draws the game panel only once

	public MyFrame(String name){
		super(name);
		c = this.getContentPane();
		this.setSize(WIDTH,HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(panel);
		gg = new GameOver();
		
		
	
	}
	public void paint(Graphics g){
		super.paint(g);
		if(screen.equalsIgnoreCase("title")){
			MyPanel.score = 0;
			hs = new Highscore();
			c.removeAll();	
			c.add(ts);
			panel = null;
			ts.revalidate();
		}else if(screen.equalsIgnoreCase("instructions")){
			c.removeAll();
			c.add(instructions);
			instructions.revalidate();
		}else if(screen.equalsIgnoreCase("mode")){
			c.removeAll();
			c.add(mode);
			mode.revalidate();
		}else if(screen.equalsIgnoreCase("difficulty")){
			c.removeAll();
			c.add(dif);
			dif.revalidate();
		}else if(screen.equalsIgnoreCase("highscore")){
			if(hs.returnCount()==0){
				c.removeAll();
				c.add(hs);
				c.validate();
			}
		}else if (screen.equalsIgnoreCase("gameover")){	
			if (!once){			
				c.removeAll();
				c.add(gg);
				once=true;
				c.validate();
			}

		}
		else{
			if(once){
				c.removeAll();	
				panel= new MyPanel(THA.WIDTH,THA.HEIGHT,ai,diff);
				c.setLayout(new BorderLayout());
				c.add(panel.returnNs(),BorderLayout.WEST);
				c.add(panel,BorderLayout.CENTER);
				c.add(btns,BorderLayout.SOUTH);
				c.add(panel.returnAmmo(),BorderLayout.EAST);
				c.validate();
				once=false;
			}

		}
		repaint();
	}
}

public class THA {
	//Size of the game panel
	static final int HEIGHT = 600;
	static final int WIDTH = 200;
	public static void main(String[] args){
		MyFrame f = new MyFrame("THA");
	}
}