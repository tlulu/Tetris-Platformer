import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
* Shoots a laser that lasts for 1 seconds, then removes itself
* destroys all shapes
* draws a rectangle to hit the shapes
*/
class Laser extends Weapons{
	private double r;
	public Laser(double mx,double my,double px,double py,double left, double right, double top,double bottom, int size){
		super(mx,0,px,py,left,right,top,bottom,size);
		r=System.currentTimeMillis();
		
	}
	public void move(int px, int py){
		x = px;
		y = py;
	}
	public void draw(Graphics g){
		g.setColor(Color.cyan);
		g.fillRect((int)(x)-5,0, size, (int)y);
			
	}
	public String getWeapon(){
		return "laser";
	}
	
	//Clears the vertical column
	//Has the same coorinates as the character
	public Square[][] damage(Square[][] s, int x, int y,int xBound, int yBound){
		if(System.currentTimeMillis()-r>1000 && r!=0){
			remove=true;
			weaponNum--;
		}	
		for(int i=x;i<x+1;i++){
			if (i<=xBound && i>=0){
				for(int j = y-1;j>=0;j--){
					s[j][i].makeSolid(false);
					MyPanel.score+=10;
				}
			}
		}
		return s;
	}
	
}