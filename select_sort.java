class select_sort{
	public void sort_arr(int a[],int n)
	{
		for(int i=0;i<n-1;i++)
		{
			int min=a[i];
			int pos=i;
			for(int j=i+1;j<n;j++)
			{
				if(a[j]<min)
				{
					min=a[j];
					pos=j;
				}

			}
			int temp=a[i];
			a[i]=min;
			a[pos]=temp;
		}
	}
	public void print_arr(int a[],int n)
	{
		for(int i=0;i<n;i++)
			System.out.print(a[i]+" ");
		System.out.println();
	}
	public static void main(String args[])
	{
		int a[]={9,22,7,45,3,2,1};
		int n=a.length;
		select_sort ss=new select_sort();
		ss.print_arr(a, n);
		ss.sort_arr(a,n);
		ss.print_arr(a,n);
	}
}