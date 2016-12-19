
class long_inc_sum{
	public int inc_sum(int a[],int n)
	{
		int s[]=new int[n];
		for(int i=0;i<n;i++)
		{
			s[i]=a[i];
		}
		for(int i=1;i<n;i++)
		{
			for(int j=0;j<i;j++)
				if(a[i]>a[j] && s[i]<s[j]+a[i])
					s[i]=s[j]+a[i];
		}
		int max=0;
		for(int i=0;i<n;i++)
			if(max<s[i])
				max=s[i];
		return max;
	}
	public static void main(String args[])
	{
		long_inc_sum lis =new long_inc_sum();
		int a[] = {1, 101, 2, 3, 100, 4, 5};
		int n=a.length;
		int x=lis.inc_sum(a,n);
		System.out.println(x);
	}
}