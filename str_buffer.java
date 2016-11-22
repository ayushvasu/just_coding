import java.io.*;
class str_buffer{
	public static void main(String args[])throws Exception
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		StringBuffer a=new StringBuffer();
		a.append(br.readLine());
		System.out.println(a);
		int l=a.length()-1;
		char ch;
		int i=0;
		while(i<l)
		{
			ch=a.charAt(i);
			a.setCharAt(i,a.charAt(l));
			a.setCharAt(l, ch);
			i++;
			l--;
		}
		System.out.println(a);
	}
}