class cut_rod{
	public int rod(int val[],int n)
	{
		int t[][]=new int[n][n+1];
		for(int i=0;i<n;i++)
			t[i][0]=0;
		for(int i=1;i<n+1;i++)
			t[0][i]=val[0]*i;
		for(int i=1;i<n;i++)
		{
			for(int j=1;j<n+1;j++)
			{
				if(i+1>j)
					t[i][j]=t[i-1][j];
				else
					t[i][j]=Math.max(t[i-1][j],t[i][j-i-1]+val[i]);
			}
		}
		p_tbl(t,n);
		return t[n-1][n];
	}
	public void p_tbl(int t[][],int n)
	{
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n+1;j++)
				System.out.print(t[i][j]+" ");
		System.out.println();
		}
	}
	public static void main(String args[])
	{
		cut_rod cd=new cut_rod();
		int arr[] ={1, 5, 8, 9, 10, 17, 17, 20};
	    int size = arr.length;
	    int x=cd.rod(arr, size);
	    System.out.println("Maximum Obtainable Value is " +x);
	}
}