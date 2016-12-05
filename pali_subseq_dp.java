class pali_subseq_dp{
	public int max(int x,int y)
	{
		return (x>y)?x:y;
	}
	public int lps(String s)
	{
		int l=s.length();
		int t[][]=new int[l][l];
		for(int i=0;i<l;i++)
			t[i][i]=1;
		for(int i=0;i<l-1;i++)
		{
			if(s.charAt(i)==s.charAt(i+1))
				t[i][i+1]=2;
			else
				t[i][i+1]=1;
		}
		for(int i=3;i<=l;i++)
		{
			for(int j=0;j<l-i+1;j++)
			{
				int k=j+i-1;
				if(s.charAt(j)==s.charAt(k))
					t[j][k]=t[j+1][k-1]+2;
				else
					t[j][k]=max(t[j][k-1],t[j+1][k]);
			}
		}
		return t[0][l-1];
	}
	public static void main(String args[])
	{
		String seq = "GEEKSFORGEEKS";
		pali_subseq_dp psd =new pali_subseq_dp();
		System.out.println("The length of longest palin is: "+psd.lps(seq));
	}
}