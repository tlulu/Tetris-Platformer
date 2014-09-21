import java.util.ArrayList;
/**
* A weapon that fires multiple bullets at once
* Uses recursion to fire the bullets at different angles-similar to recursive tree
* Each bullet destroys one square
*/
class ShotGun extends Bullet{
	private final double a1;
	private final double a2;
	
	public ShotGun(double mx,double my,double px,double py,double left, double right, double top,double bottom,int size,double x0, double y0,double len,double ang,ArrayList<Weapons> b){
		super(mx,my,px,py,left,right,top,bottom,size);
		a1=Math.abs((int)(Math.random()*180)-angle)+angle;
		a2=Math.abs((int)(Math.random()*180)+angle)+angle;
		multiply(x0,y0,len,ang,b);
		remove=true;
		weaponNum--;
		
	}
	/**
	* Adds bullets to the Weapons list
	* Each bullet has new x,y coordinates with different angles
	*/
	public void multiply(double x0, double y0,double len,double ang,ArrayList<Weapons> b){
		double x2,y2;
		if(len <= 2) return;
		else{
			x2 = x0 + (len*Math.cos(Math.toRadians(ang)));
			y2 = y0 - (len*Math.sin(Math.toRadians(ang)));
			b.add(new Bullet(x2,y2,x0,y0,xmin,xmax,ymin,ymax,size));
			multiply((int)x2,(int)y2,len/1.5,ang-a1,b);
			multiply((int)x2,(int)y2,len/1.5,ang+a2,b);
		} 

	}
	public String getWeapon(){
		return "shotgun";
	}
}