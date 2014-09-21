/**
* Collision will need to be changed to be made more efficient
*/
class Collision{
	private int maX, maY, miX, miY; //Boundaries of the collision: Max x, Max y, Min x, Min y
	
	public Collision(int top, int bottom, int left, int right){
		maX = right;
		miX = left;
		maY = bottom;
		miY = top;
	}
	
	public int[] checkBound(int x, int y, int size, int[] col){
	/*
		Check if any shape is touching or leaving the boundary
		Stores the x or y coordinate of the side(s) touched (a shape can touch more than 1 side at the same time)
		The stored value will help collision go smoother
		
		-1 				= false
		everything else = true
		Index:
			 0: Collision with left side
			 1: Collision with right side
			 2: Collision with top
			 3: Collision with bottom
			 4: No collision
	*/
		
		if(x<=miX){
			col[0] = miX; //collide with left
			col[4] = -1;
		}
		if(x+size>=maX){
			col[1] = maX-size; //collide with right
			col[4] = -1;
		}
		if(y<=miY){
			col[2] = miY; //collide with top
			col[4] = -1;
		}
		if(y+size>=maY){
			col[3] = maY-size; //collide with bottom
			col[4] = -1;
		}

		return col;
		
	}
	public int[] boundCollide(Square[][] s){
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		int x, y;
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s.length;j++){
				if(s[i][j].isReal()){	
					x = s[i][j].getX();
					y = s[i][j].getY();
					col = checkBound(x,y,Square.SIZE,col);
				}
			}
		}
		return col;
	}
	public int[] boundCollide(Weapons b){
		//Check for bullets
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		double x = b.getX();
		double y = b.getY();
		col = checkBound((int)x, (int)y,b.getSize(),col);
		return col;
	}
	public int[] boundCollide(Character c){
		//Check for character
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		double x = c.getX();
		double y = c.getY();
		col = checkBound((int)x, (int)y, Character.SIZE,col);
		return col;
	}
	public int[] checkObj(int x1, int y1, int x2, int y2, int s1, int s2,int[] col){
	/*
		-1 				= false
		everything else = true
		Checks the x and y coordinates of both 2 objects and see if they overlap
		Takes into consideration the size of the objects as well
	*/
		
		if(x1<x2+s2 && x1+s1>x2 && y1<y2+s2 && y1+s1>y2){
			col[0] = x2+s2; //Collide with left
			col[1] = x2-s1; //Collide with right
			col[2] = y2+s1+s2; //Collide with top
			col[3] = y2-s1; //Collide with bottom
			col[4] = -1; //Have collision
		}
		return col;
	}

	public int[] objCollide(Square[][] s1, Square[][] s2){
	/*
		Check if any square between the shapes are overlapping
	*/
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		int x1,y1,x2,y2;
		int size1 = Square.SIZE;
		int size2 = Square.SIZE;
		for(int i=0;i<s1.length;i++){
			for(int j=0;j<s1[0].length;j++){
				for(int k=0;k<s2.length;k++){
					for(int l=0;l<s2[0].length;l++){
						if(s1[i][j].isReal() && s2[k][l].isReal()){
							x1 = s1[i][j].getX();
							y1 = s1[i][j].getY();
							x2 = s2[k][l].getX();
							y2 = s2[k][l].getY();
							col = checkObj(x1,y1,x2,y2,size1,size2,col);
						}
					}
				}
			}
		}
		return col;
	}

	public int[] objCollide(Character c, Square[][] s){
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		int x1,y1,x2,y2;
		int size1 = Character.SIZE;
		int size2 = Square.SIZE;
		x1 = (int)c.getX();
		y1 = (int)c.getY();
		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				if(s[i][j].isReal()){
					x2 = s[i][j].getX();
					y2 = s[i][j].getY();
					col = checkObj(x1,y1,x2,y2,size1,size2,col);
				}
			}
		}
		return col;
	}
	
	public int[] objCollide(Weapons b, Square[][] s){
		int[] col = new int[5];
		for(int i=0;i<5;i++){
			col[i]=-1;
		}
		col[4] = 1; //no collision
		int x1,y1,x2,y2;
		int size1 = b.getSize();
		int size2 = Square.SIZE;
		x1 = (int)b.getX();
		y1 = (int)b.getY();
		for (int i=0;i<s.length;i++){
			for(int j=0;j<s[0].length;j++){
				if (s[i][j].isReal()){
					x2 = s[i][j].getX();
					y2 = s[i][j].getY();
					col = checkObj(x1,y1,x2,y2,size1,size2,col);
					//if (Math.abs(s[i][j].getX()+13-x)<20 && Math.abs(s[i][j].getY()+13-y)<20){
					//	col[0]=i;col[1]=j;
					//	return col;
					//}
				}
			}
		}
		return col;
	}
	
}