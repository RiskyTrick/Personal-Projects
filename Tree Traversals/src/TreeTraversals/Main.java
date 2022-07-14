package TreeTraversals;

import Entity.Node;

public class Main 
{
	public static void main(String[] args) {
		Node data=CreateTree();
		
		System.out.println("Inorder Traversal :");
		inorderTraversal(data);
		System.out.println();
		System.out.println("Preorder Traversal :");
		preorderTraversal(data);
		System.out.println();
		System.out.println("Postorder Traversal :");
		postorderTraversal(data);
		
		
	}
	
	//inorder recursive func
	public static void inorderTraversal(Node n)
	{
	    if(n==null)
	    return;
	    inorderTraversal(n.left);
	    System.out.print("["+n.value+"]"+" ");
	    inorderTraversal(n.right);
	    
	}
	
	//preorder recursive func
		public static void preorderTraversal(Node n)
	{
	      if(n==null)
	    return;
	      System.out.print("["+n.value+"]"+" ");
	       preorderTraversal(n.left);
	       preorderTraversal(n.right);
	    
	}
	
	//postorder recursive func
		public static void postorderTraversal(Node n)
	{
	      if(n==null)
	    return;
	     
	       postorderTraversal(n.left);
	       postorderTraversal(n.right);
	        System.out.print("["+n.value+"]"+" ");
	}
	public static Node CreateTree(){
	    Node a= new Node("a");
	    Node b= new Node("b");
	    Node c= new Node("c");
	    Node d= new Node("d");
	    Node e= new Node("e");
	    Node f= new Node("f");
	    
	    //Tree connections
	    a.left=b;
	    a.right=c;
	    b.left=d;
	    d.right=e;
	    c.right=f;
	    
	    // returning root of the tree
	    return a;
	}
}
