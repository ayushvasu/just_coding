import java.io.*;
import java.util.*;
class xor_maxsubmatrix{
	public static void main(String[] args)throws IOException {
		BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
		String temp=bf.readLine();
		String st[]=temp.split("\\s+");
		int row=Integer.parseInt(st[0]);
		int col=Integer.parseInt(st[1]);
		int maxval=Integer.MIN_VALUE;
		int curr;
		int arr[][]=new int[row][col];
		for(int i=0;i<row;i++)
		{
			temp=bf.readLine();
			st=temp.split("\\s+");
			for(int j=0;j<col;j++)
				arr[i][j]=Integer.parseInt(st[j]);
		}
		for(int i=0;i<col;i++)
		{
			int temparr[]=new int[row];
			for(int j=i;j<col;j++)
			{
				for(int k=0;k<row;k++)
				{
					temparr[k]^=arr[k][j];
				}
				curr=rowxor(temparr);
				if(curr>maxval)
					maxval=curr;
			}
		}
		System.out.println(maxval);
	}
	public static int rowxor(int temparr[])
	{
		int result=Integer.MIN_VALUE;
		int currxor=0;
		int prevxor;
		int i=0;
		while(i<temparr.length)
		{
			prevxor=currxor;
			currxor^=temparr[i];
			if(currxor<=prevxor)
			{
				currxor=0;
			
			}
			else if(currxor>prevxor)
			{
				result=currxor;
				
			}
			
		}
		return result;
	}
}