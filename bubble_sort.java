class bubble_sort{
	public void bsort(int arr[])
	{
		int tmp;
		int n=arr.length;
		for(int i=0;i<n-1;i++)
		{
			for(int j=0;j<n-i-1;j++)
			{
				if(arr[j]>arr[j+1])
				{
					tmp=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=tmp;
				}
			}
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
		bubble_sort bs=new bubble_sort();
		  int arr[] = {16, 51, 13, 1, 99, 7};
		  
	        System.out.println("Given Array");
	        bs.printarr(arr);
	
	        bs.bsort(arr);
	 
	        System.out.println("\nSorted array");
	        bs.printarr(arr);
	}
}