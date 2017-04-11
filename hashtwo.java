import java.io.*;
import java.util.*;
class Nodetwo{
	int index;
	int key;
	String value;
	Nodetwo(int index,int key,String value)
	{
		this.index=index;
		this.key=key;
		this.value=value;
	}
}
class hashtwo{
	private static LinkedList<Nodetwo> adj[];
	static int arr[];
	public static void main(String args[])throws IOException
	{
		BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
		int t=Integer.parseInt(bf.readLine());
		String check="litejho";
		adj = new LinkedList[t];
		arr=new int[t];
		
		for(int i=0;i<t;i++)
		{
			
			String line=bf.readLine();
			String st[]=line.split(" ");
			int n=Integer.parseInt(st[0]);
			arr[i]=Integer.parseInt(st[1]);

			String tempstr=bf.readLine();
			String str=tempstr.toLowerCase();
			String name[]=str.split("\\s+");
			String name1[]=tempstr.split("\\s+");
			int len=name.length;
			adj[i] = new LinkedList<Nodetwo>();
			//int minlen=Integer.MAX_VALUE;
			for(int j=0;j<len;j++)
			{
				int count=0;
				int cc[]=new int[26];
				Arrays.fill(cc,0);
				int wlen=name[j].length();
				for(int m=0;m<wlen;m++)
					cc[name[j].charAt(m)-97]++;
				for(int m=0;m<26;m++)
					if(cc[m]!=0 && check.indexOf((char)(m+97))!=-1)
						count++;
				adj[i].add(new Nodetwo(j,count,name1[j]));
			}
			Collections.sort(adj[i],new Comparator<Nodetwo>(){
				@Override
				public int compare(Nodetwo i1, Nodetwo i2){
					if(i1.key==i2.key){
						return i1.index-i2.index;
					}
					return i1.key-i2.key;
				}	
			});
			
			
		}
		for(int i=0;i<t;i++)
		{
			int c=0;
			while(c<arr[i])
			{
				//System.out.print(c+" "+arr[i]+" ");
				System.out.print(adj[i].get(c).value+" ");
				c++;
			}
			System.out.println();
		}
	}
}