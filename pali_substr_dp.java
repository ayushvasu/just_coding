class pali_substr_dp{
	public int lps(String s)
	{
		int l=s.length();
		boolean t[][]=new boolean[l][l];
		int start=0;
		int len=0;
		for(int i=0;i<l;i++)
			t[i][i]=true;
		for(int i=0;i<l-1;i++)
		{
			if(s.charAt(i)==s.charAt(i+1))
			{
				t[i][i+1]=true;
				start=i;
				len=2;
			}
			else
				t[i][i+1]=false;
		}
		for(int i=3;i<=l;i++)
		{
			for(int j=0;j<l-i+1;j++)
			{
				int k=j+i-1;
				if(s.charAt(j)==s.charAt(k) && t[j+1][k-1]==true)
				{
					t[j][k]=true;
					if(i>len)
					{
						start=j;
						len=i;
					}
				}
			}
		}
		System.out.println("lonest substring is: "+s.substring(start,start+len));
		return len;		
	}
	public static void main(String args[])
	{
		pali_substr_dp psd=new pali_substr_dp();
		String s="forgeeksskeegfor";
		int len=psd.lps(s);
		System.out.println("Max length is "+len);
	}
}