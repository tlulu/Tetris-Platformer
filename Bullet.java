import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
* Controls all actions take for the bullet
* Add more weapons soon
*/
class Bullet extends Weapons{
	private Collision c = new Collision(0,THA.HEIGHT,0,THA.WIDTH); //Check collision
	
	public Bullet(double mx,double my,double px,double py,double left, double right, double top,double bottom, int size){
		super(mx,my,px,py,left,right,top,bottom,size);
		
	}
	public void draw(Graphics g){
		g.setColor(Color.blue); //Color of bullet
		g.fillOval( (int) x ,(int)y, size, size );
	}
	public void move(int px, int py){	
		x-=5*Math.cos(angle);   //changing x-coordinate
		y-=5*Math.sin(angle);	//changing y-coordinate
	}
	public String getWeapon(){
		return "gun";
	}
	public Square[][] damage(Square[][] s,int x,int y, int xBound, int yBound){
		if(x>=0&&x<xBound&&y>=0&&y<yBound)
			s[x][y].makeSolid(false);
			MyPanel.score+=10;
		return s;
	}
}