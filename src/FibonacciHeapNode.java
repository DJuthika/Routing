//This class is to define a node in a fibonacci heap. 
public class FibonacciHeapNode 
{
	
	int degree; //Number of child nodes
	boolean childCut; //Childcut value shows whether a not a child has been removed from this node.
	int key;
	int index;
	
	//A node in the Fibonacci heap has a parent and child nodes.
	FibonacciHeapNode parent;
	FibonacciHeapNode child;
	
	
	//left and right show how the nodes in a level are connected in a doubly linked list.	
	FibonacciHeapNode right;
	FibonacciHeapNode left;
	//Returning the value of the node
	int keyValue()
	{
		return key;

	}
}
