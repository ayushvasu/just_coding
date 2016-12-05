import java.io.BufferedReader;
import java.io.InputStreamReader;
class string_permute{
	public void swap(StringBuffer str,int i,int j)
	{
		char temp;
		temp=str.charAt(i);
		str.setCharAt(i,str.charAt(j));
		str.setCharAt(j, temp);
	}
	public void permute(StringBuffer str, int r,int l)
	{
		if(r==l)
			System.out.println(str);
		else
		{
			for(int i=r;i<=l;i++)
			{
				swap(str,r,i);
				permute(str,r+1,l);
				swap(str,r,i);
			
			}
		}
	}
	public static void main(String args[])throws Exception
	{
		string_permute sp=new string_permute();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		StringBuffer a=new StringBuffer();
		a.append(br.readLine());
		int l=a.length()-1;
		int r=0;
		sp.permute(a, r, l);
	}
}