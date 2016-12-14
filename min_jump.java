/*This program is to reach the end of the array from beginning with minimum number of jumps.
The jumps can be made according to the value of the array.If value is 3 then jump of 1,2 or 3 steps can be made not more
than that.*/
import java.util.Arrays;

class min_jump{
	public int jump(int a[],int n)
	{
		int ju[]=new int[n];
		int arr[]=new int[n];
		Arrays.fill(ju, Integer.MAX_VALUE);
		Arrays.fill(arr, -1);
		ju[0]=0;
		arr[0]=-1;
		for(int i=1;i<n;i++)
		{
			for(int j=0;j<i;j++)
			{
				if(a[j]>=i-j && ju[j]+1<ju[i])
				{
						ju[i]=ju[j]+1;
						arr[i]=j;
				}
			}
		}
		for(int i=0;i<n;i++)
		{
			System.out.print(arr[i]+" ");
		}
		System.out.println();
		return ju[n-1];
	}
	public static void main(String args[])
	{
		min_jump mj=new min_jump();
		int a[] = {1, 3, 5, 8, 9, 2, 6, 7, 6, 8, 9};
		int n=a.length;
		int x=mj.jump(a,n);
		System.out.println(x);
	}
}
