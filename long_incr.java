import java.io.*;
class long_incr
{
	
	public int max_seq(int arr[],int n)
	{
		int lis[]=new int [n];
		for(int i=0;i<n;i++)
			lis[i]=1;
		
		for(int i=1;i<n;i++)
		{
			for(int j=0;j<i;j++){
				if(arr[i]>arr[j]&& lis[i]<lis[j]+1)
					lis[i]=lis[j]+1;
			}
		}
		int max=0;
		for(int i=0;i<n;i++)
			if(max<lis[i])
				max=lis[i];
		return max;
	}
	public static void main(String args[]) throws Exception
	{
		long_incr li= new long_incr();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("length of array");
		int n=Integer.parseInt(br.readLine());
		int arr[]=new int[n];
		for(int i=0;i<n;i++)
			arr[i]=Integer.parseInt(br.readLine());
		int x=li.max_seq(arr,n);
		System.out.println(x);
	}
}