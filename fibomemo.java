import java.io.*;
class fibomemo{
	int lookup[];
	public void init(int n)
	{
		lookup=new int[n];
		for(int i=0;i<n;i++)
			lookup[i]=-1;
	}
	public int fib(int n)
	{
		if(lookup[n]==-1)
		{
			if(n<=1)
				lookup[n]=1;
			else
				lookup[n]=fib(n-1)+fib(n-2);
		}
		return lookup[n];
	}
	public static void main(String args[]) throws Exception
	{
		fibomemo fm=new fibomemo();
		System.out.println("Enter the fibo");
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		int n=Integer.parseInt(br.readLine());
		fm.init(n);
		int x=fm.fib(n-1);
		System.out.println(x);
	}
}