class rat_maze
{
	final int n=4;
	public boolean issafe(int a[][],int x,int y)
	{
		if(x>=0 && x<n && y>=0 && y<n && a[x][y]==1)
			return true;
		else
			return false;
	}
	public void printarr(int sol[][])
	{
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
				System.out.print(sol[i][j]+" ");
			System.out.println();
		}
	
	}
	
	public boolean findsol(int a[][],int x,int y,int sol[][])
	{
		if(x==n-1 && y==n-1)
		{
			sol[x][y]=1;
			return true;
		}
		if(issafe(a,x,y)==true)
		{
			sol[x][y]=1;
			if(findsol(a,x+1,y,sol))
				return true;
			if(findsol(a,x,y+1,sol))
				return true;
			sol[x][y]=0;
			return false;
		}
		return false;
		
	}
	public void solutil(int a[][])
	{
		int sol[][]={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		if(findsol(a,0,0,sol)==false)
			System.out.println("No way out man!");
		else
			printarr(sol);
			
	}
	public static void main(String args[])
	{
		rat_maze rm=new rat_maze();
		int maze[][] = {{1, 0, 0, 0},{1, 1, 0, 1},{0, 1, 0, 0},{1, 1, 1, 1} };
		rm.solutil(maze);
	}
}