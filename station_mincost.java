/*This program determines the minimum cost of traveling from start to end station.
The cost of travel is given cost[N][N] = { {0, 15, 80, 90},
              {INF, 0, 40, 50},
              {INF, INF, 0, 70},
              {INF, INF, INF, 0}
             };
There are 4 stations and cost[i][j] indicates cost to reach j 
from i. The entries where j < i are meaningless. */
class station_mincost{
	public final int n=4;
	public int calc_fare(int a[][])
	{
		int min_st[]=new int[n];
		min_st[0]=0;
		for(int i=1;i<n;i++)
			min_st[i]=Integer.MAX_VALUE;
		for(int i=1;i<n;i++)
		{
			for(int j=0;j<i;j++)
			{
				if(a[j][i]+min_st[j]<min_st[i])
					min_st[i]=a[j][i]+min_st[j];
			}
		}
		for(int i=0;i<n;i++)
			System.out.print(min_st[i]+" ");
		System.out.println();
		return min_st[n-1];
	}
	public static void main(String args[])
	{
		int cost[][] = { {0, 15, 80, 90},
                {-1, 0, 40, 50},
                {-1, -1, 0, 70},
                {-1, -1, -1, 0}
              };
		station_mincost sm=new station_mincost();
		int x=sm.calc_fare(cost);
		System.out.println(x);
	}
}