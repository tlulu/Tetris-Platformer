import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;

/**
 * Commands all the highscore function
 * Read and write files
 * Clear files
 * Draws files of the panel
*/
public class Highscore extends JPanel implements ActionListener{
	private  int scorelist[]=new int[100];
	private String namelist2[]=new String[100];
	private PrintWriter w;
	private ImageIcon intro;
	private JButton back = 	new JButton("Back");
	private JLabel l = new JLabel("Highscore");
	private int count = 0;
	
	public Highscore(){
		intro = new ImageIcon("introScreen.jpg");
		back.setBounds(325,600,80,40);
		l.setPreferredSize(new Dimension(250,100));
		l.setFont(new Font("Serif", Font.BOLD, 50));
		l.setForeground(Color.yellow);
		back.addActionListener(this);
		count = 0;
		
	}
	
	/**
	 * Insertion sort from highest score to the lowest
	*/
	public static void sort(int[] num,int len) {	//sorts the scores
		for (int i=0;i<len;i++){		
			int start=i;		//initial value
			int b=num[i];
			while ((start>0) && (num[start-1]<=b)){ //compares the values doesn't go under index zero
				num[start]=num[start-1];
				start=start-1;			//moves down a position
			}
			num[start]=b;
		}		
	}
	public void read() throws IOException{
		int i;
		String str="";			//used for file read function
		String numstr="";			//used for file read function
		String namelist[]=new String[100];    //namelist for highscores											
		int scorelist2[]=new int[100];			//score list for highscores
		int scorelist3[]=new int[100];
		int count=0;
		BufferedReader in = new BufferedReader(new FileReader("highscores.txt"));
		BufferedReader in2 = new BufferedReader(new FileReader("highscoreNames.txt"));
		while (str!= null && numstr!=null) {
			str=in2.readLine();
			numstr=in.readLine();		  
			if (str!= null && numstr!=null){
				i=Integer.parseInt(numstr);
				namelist[count]=str;         //enters the names
				scorelist[count]=i;        //enters the scores 
				scorelist2[count]=i;
				scorelist3[count]=i;			//another list used for sorting
				count++;
				if(count>=100){
					break;
				}
			}

		}  
		sort(scorelist,count);  //sorts the scores highest to lowest
		sort(scorelist3,count);
		for (int j=0;j<count;j++){
			for (int k=0;k<count;k++){
				if (scorelist2[j]==scorelist3[k]){
					namelist2[k]=namelist[j];       //matches the sorted scores with the corresponding names
					scorelist3[k]=-50;    		//if the same score appears, assigns a different value
					break;					  
				}
			}
		}
		in.close();
		in2.close();
		clearFiles();
	}
	public static void write(int score,String name)throws IOException{
		PrintWriter out;
		PrintWriter out2;
		out = new PrintWriter(new BufferedWriter(new FileWriter("highscores.txt",true)));
		out2 = new PrintWriter(new BufferedWriter(new FileWriter("highscoreNames.txt",true)));
		out.println(score+"");							//writes to file
		out2.println(name);								//writes to file

		out.close();
		out2.close();
	}
	public  String[] getSortedNames(){
		return namelist2;
	}
	public int[] getSortedScores(){
		return scorelist;
	}
	
	/**
	 * Get the space between name and score based on the length of name and score
	 * Used for formatting similar to System.out.format
	*/ 
	public String getSpace(int i){
		String space = "";
		for(int j=0;j<50-namelist2[i].length()-(scorelist[i]+"").length();j++){
			space+=" ";
		}
		return space;
	}
	/**
	 * Uses the box layout to draw the highscore
	*/
	public  JPanel drawHighscore(){
		JLabel[] writeHighScores = new JLabel[100];
		int yPos = 30;
		for(int i=0;i<100;i++){
			if(namelist2[i]==null){
				break;
			}
			writeHighScores[i] = new JLabel(i+1+".  "+namelist2[i]+getSpace(i)+scorelist[i]);
			writeHighScores[i].setLayout(null);
			writeHighScores[i].setFont(new Font("Serif", Font.BOLD, 16));
			writeHighScores[i].setForeground(Color.black);
			writeHighScores[i].setBounds(4, yPos, 348, 40);
			yPos+=30;
		}
		
		JPanel scores = new JPanel();
		scores.setLayout(new BoxLayout(scores, BoxLayout.PAGE_AXIS));
		scores.add(Box.createRigidArea(new Dimension(0,5)));
		scores.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"   Name                             Score",TitledBorder.LEFT,TitledBorder.TOP,new Font("Serif", Font.BOLD, 18)));
		for(int i=0;i<100;i++){
			if(writeHighScores[i]==null){
				break;
			}
			scores.add(writeHighScores[i]);
		}
		return scores;
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(intro.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		try {
			read();					//reads the file
		}catch (IOException e) {}
		JPanel scores = drawHighscore();
		scores.setBounds(93, 40, 298, 500);
		add(scores);
		count++;
		add(back);
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equalsIgnoreCase("Back")){
			MyFrame.screen = "title";
		}
	}
	public int returnCount(){
		return count;
	}
	
	//Clear files when the lengh goes over 15
	//Saves the top 5 scores
	public void clearFiles(){
		try{
			int count = 0;
			for(int i=0;i<namelist2.length;i++){
				if(namelist2[i]!=null){
					count++;
				}else{
					break;
				}
			}
			if(count>15){
				w = new PrintWriter(new BufferedWriter(new FileWriter("highscoreNames.txt")));
				w.close();
				w = new PrintWriter(new BufferedWriter(new FileWriter("highscores.txt")));
				w.close();
			
				for(int i=0;i<5;i++){
					write(scorelist[i],namelist2[i]);
				}
			}
		}catch(IOException e){
			System.out.println("Files do not exist!");
		}
	}
}
