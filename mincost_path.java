class mincost_path{
	public int min(int x,int y,int z)
	{
		if(x<y)
			return ((x<z)?x:z);
		else
			return ((y<z)?y:z);
	}
	public int mincost(int a[][],int m,int n)
	{
		int t[][]=new int[m+1][n+1];
		t[0][0]=a[0][0];
		for(int i=1;i<=m;i++)
			t[i][0]=t[i-1][0]+a[i][0];
		for(int i=1;i<=n;i++)
			t[0][i]=t[0][i-1]+a[0][i];
		for(int i=1;i<=m;i++)
		{
			for(int j=1;j<=n;j++)
			{
				t[i][j]=a[i][j]+min(t[i-1][j-1],t[i-1][j],t[i][j-1]);
			}
		}
		return t[m][n];
	}
	public static void main(String args[])
	{
		mincost_path mp=new mincost_path();
		int a[][]= {{1, 2, 3},
                {4, 8, 2},
                {1, 5, 3}};
		System.out.println(mp.mincost(a, 2, 2));
	}
}