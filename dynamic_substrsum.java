class dynamic_substrsum
{
	public boolean isubset(int set[],int n,int sum)
	{
		boolean submat[][]=new boolean[n+1][sum+1];
		for(int i=0;i<=n;i++)
			submat[i][0]=true;
		for(int i=1;i<=sum;i++)
			submat[0][i]=false;
		for(int i=1;i<=n;i++)
		{
			for(int j=1;j<=sum;j++)
			{
				submat[i][j]=submat[i-1][j];
				if(j>=set[i-1])
					submat[i][j]=submat[i][j]||submat[i-1][j-set[i-1]];
			}
		}
		
	}
	public static void main(String args[]) 
	{
		dynamic_substrsum ds=new dynamic_substrsum();
		int set[] = {3, 34, 4, 12, 5, 2};
        int sum = 9;
        int n = set.length;
        if(ds.isubset(set, n, sum)==true)
        	System.out.println("Substring present");
        else
        	System.out.println("Substring not present");
	}
}