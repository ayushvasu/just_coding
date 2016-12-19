class quick_sort
{
	public void qsort(int arr[])
	{
		int l=arr.length;
		for(int i=1;i<l;i++)
		{
			int key=arr[i];
			int j=i-1;
			while(j>=0 && arr[j]>key)
			{
				arr[j+1]=arr[j];
				j--;
			}
			arr[j+1]=key;
		}
		
	}
	public void printarr(int arr[])
	{
		int l=arr.length;
		for(int i=0;i<l;i++)
			System.out.print(arr[i]+" ");
		System.out.println();
	}
	public static void main(String args[])
	{
		quick_sort qs=new quick_sort();
		  int arr[] = {12, 11, 13, 5, 6, 7};
		  
	        System.out.println("Given Array");
	        qs.printarr(arr);
	
	        qs.qsort(arr);
	 
	        System.out.println("\nSorted array");
	        qs.printarr(arr);
	}
}