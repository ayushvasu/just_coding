import java.io.*;
class fibo_botup{
	public int fib(int n)
	{
		int f[]=new int[n+1];
		f[0]=0;
		f[1]=1;
		for(int i=2;i<=n;i++)
			f[i]=f[i-1]+f[i-2];
		return f[n];
	}
	public static void main(String args[]) throws Exception
	{
		fibo_botup fb=new fibo_botup();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter fibo");
		int n=Integer.parseInt(br.readLine());
		int x=fb.fib(n);
		System.out.println(x);
	}
}