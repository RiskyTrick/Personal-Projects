import java.util.*;
public class Main 
{
    
	public static void main(String[] args) {
		Node root= new Node(1);
		root.left= new Node(2);
		root.left.left=new Node(9);
		root.right=new Node(3);
		root.right.right=new Node(4);
		root.right.right.right=new Node(5);
		
		List<Integer> list=new ArrayList<>();
		
		LeftView(root.left,1,list); //(i get straight order of the list while traversing so i need to reverse the order hence a list)
		
		Collections.reverse(list);//reversing the list
		
		for(Integer i:list) // enhanced forloop to print the list
		{
		    System.out.print("["+i+"]"+" ");
		}
		
		rightView(root,1);
		
		/*
		       1
		     2   3
		   9      4
         null       5
		*/

		
	}

	static int max=0;
	
	
	//pre order recursive func for left view
			public static void LeftView(Node n,int level,List<Integer> list)
	{
	      if(n==null)
	       return;
	       
	    if(max < level)
	    {
	      //System.out.print("["+n.value+"]"+" ");
	      list.add(n.value);
	      max=level;
	    }
	    
	       LeftView(n.left,level+1,list);

	    
	}
	
	static int max2=0; //  initiliasing max2 to 0 after left view
	
	//postorder recursive func  for right view
	
		public static void rightView(Node n,int level)
	{
	      if(n==null)
	       return;
	       
	    if(max2 < level)
	    {
	      System.out.print("["+n.value+"]"+" ");
	      max2=level;
	    }
	    
	       rightView(n.right,level+1);
	    
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