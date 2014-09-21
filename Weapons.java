import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
* Weapons contains bullets, grenades, laser and shotgun
* Hit / to change weapons
* Use mouse to shoot
*/
abstract class Weapons{
	protected Collision c = new Collision(0,THA.HEIGHT,0,THA.WIDTH); //Check collision
	protected double xmax,xmin,ymax,ymin;
	protected boolean remove;
	protected double x,y;
	protected double angle;
	protected String damage;
	protected static int weaponNum=0;
	protected int size;
	public Weapons(double mx,double my,double px,double py,double left, double right, double top,double bottom, int size){
		xmin = left;
		xmax = right;
		ymin = top;
		ymax = bottom;
		x=px;
		y=py;       //player coordinates
		angle=(Math.atan2((y-my),(x-mx)));  //angle of tangent
		weaponNum++;
		this.size = size;
	}
	abstract void draw(Graphics g);
	abstract void move(int px, int py);
	
	//Check collision with bound and object
	public int [] collide(Block b,Map m){
		//System.out.println(remove);
		int[] col1 = c.boundCollide(this);
		int[] col2 = c.objCollide(this,m.getMap());
		int[] col3 = c.objCollide(this,b.getShape());
		int[] removeSq = {-1,-1,-1}; //Coordinate of the square hit
		if(col1[4]==-1){
			remove = true;
		}
		if(col2[4]==-1){
			remove = true;
			removeSq[0] = col2[1]+size;
			removeSq[1] = col2[3]+size;
			removeSq[2] = 0; //indicate that map is being collided
			return removeSq;
		}
		if(col3[4]==-1){
			remove = true;
			removeSq[0] = col3[1]+size;
			removeSq[1] = col3[3]+size;
			removeSq[2] = 1; //indicate that shape is being collided
			return removeSq;
		}
		return removeSq;
		
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(int a){
		x=a;
	}
	public void setY(int a){
		y=a;
	}
	public boolean getRemove(){
		return remove;
	}
	public void setRemove(boolean b){
		remove=b;
	}
	public String getDamage(){
		return damage;
	}
	public double getAngle(){
		return angle;
	}
	public static int getWeaponNum(){
		return weaponNum;
	}
	public int getSize(){
		return size;
	}
	abstract String getWeapon();
	
	//How much square the weapon removes
	abstract Square[][] damage(Square[][] s,int x, int y, int xBound, int yBound);
	
}