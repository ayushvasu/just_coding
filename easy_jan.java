//This program is to test a simple function according to the question given in hackerearth
import java.util.*;
import java.text.*;
import java.io.*;
class easy_jan{
	public static void main(String args[]) throws IOException
	{
		//DecimalFormat dff=new DecimalFormat("#.####");
				NumberFormat nff = new DecimalFormat("#0.000");
				BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
				int t=Integer.parseInt(bf.readLine());
				double tt[]=new double[t];
				String str="123456789";
				for(int j=0;j<t;j++)
				{
					String[] s1 = bf.readLine().split(" ");
					int b1=Integer.parseInt(s1[0]);
					int b2=Integer.parseInt(s1[1]);
					String arr1[]=new String[b1];
					String arr2[]=new String[b2];
					for(int i=0;i<b1;i++)
						arr1[i]=bf.readLine();
					for(int i=0;i<b2;i++)
						arr2[i]=bf.readLine();
			
			int c,cc,even=0,odd=0;
			for(int i=0;i<b2;i++)
			{
				for(int k=0;k<b1;k++)
				{
					int x=0;
					for(int m=0;m<9;m++)
					{
						if(arr2[i].indexOf(str.charAt(m))!=-1 && arr1[k].indexOf(str.charAt(m))!=-1)
							x=x*10+str.charAt(m);
					}
					
				//System.out.print(x+" ");
				if(x%2==0)
					even++;
				else
					odd++;
				}
				
				//System.out.println();
			}
			tt[j]=(double)even/(odd+even);
		}
		for(int i=0;i<t;i++)
			System.out.println(nff.format(tt[i]));
	}
}