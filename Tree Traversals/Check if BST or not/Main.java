/*
A binary Search Tree is a node-based binary tree data structure that has the following properties:  

The left subtree of a node contains only nodes with keys lesser than the node’s key.
The right subtree of a node contains only nodes with keys greater than the node’s key.
The left and right subtree each must also be a binary search tree.
There must be no duplicate nodes.
*/

public class Main 
{
    
	public static void main(String[] args) {
		Node root= new Node(5);
		root.left=new Node(2);
		root.left.left=new Node(1);
		root.left.right=new Node(3);
		root.right=new Node(7);
		root.right.right=new Node(8);
		root.right.left=new Node(6);
		
	
		
		System.out.print(isBST(root,Integer.MIN_VALUE,Integer.MAX_VALUE)); //Integer.MIN_Value and MAX_VALUE returns integers smallest and alrgest value in java
		
		//Sample BST
		/*
		        5
		    2      7
		 1   3   6    8
		 
		*/

		
	}

//check whether binary search tree or no
	public static boolean isBST(Node n,int min,int max)
	{
	    if(n==null)
	    return true;
	    
	    if(n.value < min||n.value > max) 
	    return false;
	    
	    return isBST(n.left,min,n.value)&&isBST(n.right,n.value+1,max);
	}
	
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
