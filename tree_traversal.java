/*This program is written for inorder,preorder,postorder and level-order traversal
for binary tree */

class node{
	int key;
	node left,right;
	node(int k)
	{
		key=k;
		left=right=null;
	}
}
class tree_traversal{
	node root;
	tree_traversal()
	{
		root=null;
	}
	
	public void inorder(node root)
	{
		if(root==null)
			return;
		inorder(root.left);
		System.out.print(root.key+" ");
		inorder(root.right);
	}
	public void preorder(node root)
	{
		if(root==null)
			return;
		System.out.print(root.key+" ");
		preorder(root.left);
		preorder(root.right);
	}
	public void postorder(node root)
	{
		if(root==null)
			return;
		postorder(root.left);
		postorder(root.right);
		System.out.print(root.key+" ");
	}
	public int height(node root)
	{
		if(root==null)
			return 0;
		int lh=height(root.left);
		int rh=height(root.right);
		if(lh>rh)
			return (lh+1);
		else
			return (rh+1);
	}
	public void levorder()
	{
		int h=height(root);
		for(int i=1;i<=h;i++)
			printlev(root,i);
		
	}
	public void printlev(node root,int level)
	{
		if(root==null)
			return;
		if(level==1)
			System.out.print(root.key+" ");
		else if(level>1)
		{
			printlev(root.left,level-1);
			printlev(root.right,level-1);
		}
	}

	public static void main(String args[])
	{
		tree_traversal tt=new tree_traversal();
		tt.root=new node(1);
		tt.root.left=new node(2);
		tt.root.right=new node(3);
		tt.root.left.left=new node(4);
		tt.root.left.right=new node(5);
		System.out.println("Inorder Traversal");
		tt.inorder(tt.root);
		System.out.println();
		System.out.println("Preorder Traversal");
		tt.preorder(tt.root);
		System.out.println();
		System.out.println("Postorder Traversal");
		tt.postorder(tt.root);
		System.out.println();
		System.out.println("Levelorder Traversal");
		tt.levorder();
	}
}