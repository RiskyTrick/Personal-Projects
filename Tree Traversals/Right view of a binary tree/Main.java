public class Main 
{
    
	public static void main(String[] args) {
		Node root= new Node(1);
		root.left= new Node(2);
		root.left.left=new Node(9);
		root.right=new Node(3);
		root.right.right=new Node(4);
		root.right.right.right=new Node(5);
		
		rightView(root,1);
		
		/*
		       1
		     2   3
		   9      4
         null       5
		*/

		
	}

	static int max=0;
	//preorder recursive func
	
		public static void rightView(Node n,int level)
	{
	      if(n==null)
	       return;
	       
	    if(max < level)
	    {
	      System.out.print("["+n.value+"]"+" ");
	      max=level;
	    }
	    
	       rightView(n.right,level+1);
	    
	}

// 	public static Node CreateTree(){
// 	    Node a= new Node("a");
// 	    Node b= new Node("b");
// 	    Node c= new Node("c");
// 	    Node d= new Node("d");
// 	    Node e= new Node("e");
// 	    Node f= new Node("f");
	    
// 	    //Tree connections
// 	    a.left=b;
// 	    a.right=c;
// 	    b.left=d;
// 	    d.right=e;
// 	    c.right=f;
	    
// 	    // returning root of the tree
// 	    return a;
// 	}
}



// the tree structure (Entity)
 class Node{
    int value;
    Node left;
    Node right;
    
    public Node(){
    
    }
    
    public Node(int value)
    {
        this.value=value;
        this.left=this.right=null;
        
    }
}
