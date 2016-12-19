class merge_sort{
	public void merge(int arr[],int l,int r,int m)
	{
		int n1= m-l+1;
		int n2=r-m;
		int temp1[]=new int[n1];
		int temp2[]=new int[n2];
		for(int i=0;i<n1;i++)
			temp1[i]=arr[l+i];
		for(int i=0;i<n2;i++)
			temp2[i]=arr[m+1+i];
		int i=0,j=0;
		int k=l;
		while(i<n1 && j<n2)
		{
			if(temp1[i]<=temp2[j])
			{
				arr[k]=temp1[i];
				i++;
			}
			else
			{
				arr[k]=temp2[j];
				j++;
			}
			k++;
		}
		while(i<n1)
		{
			arr[k]=temp1[i];
			i++;
			k++;
			
		}
		while(j<n2)
		{
			arr[k]=temp2[j];
			j++;
			k++;
		}
	}
	public void sort(int arr[],int l,int r)
	{
		int m=(l+r)/2;
		if(l<r)
		{
		sort(arr,l,m);
		sort(arr,m+1,r);
		merge(arr,l,r,m);
		}
	}
	static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
 
    // Driver method
    public static void main(String args[])
    {
        int arr[] = {12, 11, 13, 5, 6, 7};
 
        System.out.println("Given Array");
        printArray(arr);
 
        merge_sort ob = new merge_sort();
        ob.sort(arr, 0, arr.length-1);
 
        System.out.println("\nSorted array");
        printArray(arr);
    }
}