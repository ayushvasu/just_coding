import java.io.*;
class str_buffer_swap{
	public void swap(StringBuffer str,int i,int j)
	{
		char temp;
		temp=str.charAt(i);
		str.setCharAt(i,str.charAt(j));
		str.setCharAt(j, temp);
	}
	public static void main(String args[])throws Exception
	{
		str_buffer_swap sbs=new str_buffer_swap();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		StringBuffer a=new StringBuffer();
		a.append(br.readLine());
		System.out.println(a);
		int l=a.length()-1;
		int i=0;
		sbs.swap(a,i,l);
		System.out.println(a);
	}
}