import java.io.*;
import java.util.*;
class point{
	int x;
	int y;
	point(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}
class orientation{
	
	public int orient(point p1,point p2,point p3)
	{
		int res=((p3.x-p2.x)*(p2.y-p1.y))-((p2.x-p1.x)*(p3.y-p2.y));
		return res;
	}
	public static void main(String args[])throws IOException
	{
		point p1=new point(0,0);
		point p2=new point(4,4);
		point p3=new point(1,2);
		orientation or=new orientation();
		int x=or.orient(p1, p2, p3);
		if(x==0)
			System.out.println("Colinear");
		else if(x>0)
			System.out.println("clock");
		else if(x<0)
			System.out.println("anti-clock");
	}
}