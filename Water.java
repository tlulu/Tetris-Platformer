import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
class Water{

	private double x,y;
	private double xmax,xmin,ymax,ymin; //boundaries of the map
	private BufferedImage w;
	private int speed; //how fast the water rises
	private int length,height;
	
	public Water(double left, double right, double top,double bottom, int speed){
		xmin = left;
		xmax = right;
		ymin = top;
		ymax = bottom;
		x=xmin;
		y=ymax;
		try {
			w = ImageIO.read(new File("water1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.speed = speed;
		length=w.getWidth();
		height=w.getHeight();
	}
	
	/**
	 * Subtract the y coordinate of water by speed to make it move up
	*/
	public void move(){
		y=y-speed;
	}
	
	/**
	 * Draws water onto map
	 * @param g Graphics
	*/
	public void draw(Graphics g){
		g.drawImage(w,0, (int)y, (int)xmax,(int)ymax,0, 0, length,height, null);
	}
	
	/**
	 * @return double y  The y coordinate of water
	*/
	public double getY(){
		return y;
	}
	
	/**
	 * @return double x  The x coordinate of water
	*/
	public double getX(){
		return x;
	}
	
	/**
	 * @return int speed  How fast is the water rising
	*/
	public int getSpeed(){
		return speed;
	}
	
	/**
	 * Changes how fast the water rises
	 * @param int speed  The new speed value
	*/
	public void changeSpeed(int speed){
		this.speed = speed;
	}
}