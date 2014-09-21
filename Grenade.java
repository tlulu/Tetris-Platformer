import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
* uses projectile motion to fire a grenade
* destroys a area of Squares
*/
class Grenade extends Weapons{
	private double time=0;			//time used for calculating distance
	private double vely=0,velx=0;  //speed of grenade
	private final int radius=12;
	private final int initialVel=10;

	public Grenade(double mx,double my,double px,double py,double left, double right, double top,double bottom, int size){
		super(mx,my,px,py,left,right,top,bottom,size);
		damage="shape";
		
	}
	public void move(int px, int py){ //add collision!!!
		time+=0.05;    //time moves 
		velx=Math.cos(angle)*time*initialVel;   //x-component velocity
		vely=(Math.sin(angle)*initialVel)+(-9.8*time*time);	//y-component velocity
		x-=velx;		//updating
		y-=vely;

	}
	public void draw(Graphics g){
		g.setColor(Color.orange);
		g.fillOval((int)x,(int)y,radius,radius);
	}
	public String getWeapon(){
		return "grenade";
	}
	public Square[][] aoeDmg(Square[][] s, int x, int y, int l, boolean increase,int xBound, int yBound){
		if(l>=2){
			increase = false;
		}
		if(!increase&&l<0){
			return s;
		}
		for(int i=x-l;i<=x+l;i++){
			if(i>=0&&i<xBound&&y>=0&&y<yBound)
				s[i][y].makeSolid(false);
				MyPanel.score+=10;
		}
		if(increase){
			return aoeDmg(s,x,y+1,l+1,increase,xBound,yBound);
		}else{
			return aoeDmg(s,x,y+1,l-1,increase,xBound,yBound);
		}
	}
	
	//Clears a circle of blocks using recursion
	public Square[][] damage(Square[][] s, int x, int y, int xBound, int yBound){
		s = aoeDmg(s,x,y-2,0,true,xBound,yBound);
		return s;
	}
}