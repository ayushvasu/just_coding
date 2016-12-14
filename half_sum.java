/*This programs determines the maximum substring length such that 
the sun of the 1st and 2nd half of the substring is same.
This is only for even substring length...*/
class half_sum
{
	public int cal_len(int arr[],int n)
	{
		int maxlen=0;
		int sum[][]=new int[n][n];
		for(int i=0;i<n;i++)
			sum[i][i]=arr[i];
		for(int i=2;i<=n;i++)
		{
			for(int j=0;j<n-i+1;j++)
			{
				int k=j+i-1;
				int l=i/2;
				sum[j][k]=sum[j][k-l]+sum[k-l+1][k];
				if(i%2==0 && sum[j][k-l]==sum[k-l+1][k] && i>maxlen)
					maxlen=i;
			}
		}
		return maxlen;
	}
	public static void main(String args[])
	{
		int arr[] = {1,5,3,8,0,3};
		int n=arr.length;
		half_sum hs=new half_sum();
		int x=hs.cal_len(arr, n);
		System.out.println(x);
	}
}
