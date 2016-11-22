import java.io.*;
class pattern_match{
	public void pattern(String text,String pat){
		int m=text.length();
		int n=pat.length();
		int j;
		for(int i=0;i<=m-n;i++)
		{
			for(j=0;j<n;j++)
				if(text.charAt(i+j)!=pat.charAt(j))
					break;
			if(j==n)
				System.out.println("Pattern matches at index "+i);
		}
		
	}
	public static void main(String args[]) throws Exception
	{
		pattern_match pm=new pattern_match();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("enter the text");
		String text=br.readLine();
		String pat=br.readLine();
		pm.pattern(text, pat);
		
	}
}
