/* IMPORTANT: Multiple classes and nested static classes are supported */

/*
 * uncomment this if you want to read input.
//imports for BufferedReader
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import for Scanner and other utility classes
import java.util.*;
*/
import java.io.*;
import java.util.*;
class Node{
	int index;
	int value;
	Node(int index,int value)
	{
		this.index=index;
		this.value=value;
	}
}
class Graph {
      private int V;  
      private LinkedList<Node> adj[]; 
 
    
    Graph(int v)
    {
        V = v;
        adj = new LinkedList[v+1];
        for (int i=1; i<=v; ++i)
            adj[i] = new LinkedList<Node>();
    }
 
    void addEdge(int v,int w,int ww)
    {
        adj[v].add(new Node(w,ww));
    }
 
    void test(int k,int val[])
    {
    	
        for(int i=1;i<=V;i++)
        {
            Iterator<Node> xx = adj[i].listIterator();
            int c=adj[i].size();
            
           if(c<k)
        	   System.out.println(-1);
           else
           {
            Collections.sort(adj[i],new Comparator<Node>(){
				@Override
				public int compare(Node i1, Node i2){
					if(i1.value==i2.value){
						return i2.index-i1.index;
					}
					return i2.value-i1.value;
				}	
			});
            System.out.println(adj[i].get(k-1).index);
            /*for(int j=0;j<c;j++)
            {
            	System.out.println(adj[i].get(j).index+" "+adj[i].get(j).value);
            }*/
           }
           
        	   
            
        }
    }
    public static void main(String args[] ) throws Exception {
        
        Scanner sc=new Scanner(System.in);
    
        int n,m,k;
   
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
             Graph g=new Graph(n);
        int val[]=new int[n+1];
        int x[]=new int[m+1];
        int y[]=new int[m+1];
        for(int i=1;i<=n;i++)
            val[i]=sc.nextInt();
        for(int i=1;i<=m;i++)
        {
            x[i]=sc.nextInt();
            y[i]=sc.nextInt();
           
            g.addEdge(x[i],y[i],val[y[i]]);
            g.addEdge(y[i],x[i],val[x[i]]);
           
        }
        //System.out.println("before printing");
        g.test(k,val);
        //System.out.println("After ans");
        
            
    }
}
