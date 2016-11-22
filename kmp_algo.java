class kmp_algo{
	public void calclps(String pat,int m,int lps[])
	{
		int len=0;
		int i=1;
		lps[0]=0;
		while(i<m)
		{
			if(pat.charAt(len)==pat.charAt(i))
			{
				len++;
				lps[i]=len;
				i++;
			}
			else
			{
				if(len==0)
				{
					lps[i]=0;
					i++;
				}		
				else
				{
					len=lps[len-1];
				}
					
			}
		}
	}
	public void kmpsearch(String txt,String pat)
	{
		int m=pat.length();
		int n=txt.length();
		int lps[]=new int[m];
		calclps(pat,m,lps);
		int i=0,j=0;
		while(i<n)
		{
			if(txt.charAt(i)==pat.charAt(j))
			{
				i++;
				j++;
			}
			if(j==m)
			{
				System.out.println("patter forund at "+(i-j));
				j=lps[j-1];
			}
			else if(i<n && txt.charAt(i)!=pat.charAt(j))
			{
				if(j!=0)
					j=lps[j-1];
				else
					i++;
			}
		}
		
	}
	public static void main(String args[]) throws Exception
	{
		String txt = "ABABDABACDABABCABAB";
        String pat = "ABABCABAB";
        kmp_algo ka=new kmp_algo();
        ka.kmpsearch(txt,pat);
	}
}