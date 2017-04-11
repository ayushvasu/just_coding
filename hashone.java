import java.io.*;
class hashone{
	public static void main(String args[])throws IOException
	{
		BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
		int t=Integer.parseInt(bf.readLine());
		String str="abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int arr[]=new int[t];
		for(int i=0;i<t;i++)
		{
			String in=bf.readLine();
			String insp[]=in.split("\\s+");
			int l=insp.length;
			int c=0;
			for(int j=0;j<l;j++)
			{
				int len=insp[j].length();
				for(int k=0;k<len;k++)
				{
					c=c+(k+str.indexOf(insp[j].charAt(k)));
				}
			}
			arr[i]=l*c;
		}
		for(int i=0;i<t;i++)
			System.out.println(arr[i]);
	}
}