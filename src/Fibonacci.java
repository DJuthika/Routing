import java.util.ArrayList;
import java.util.List;

public class Fibonacci {
	FibonacciHeapNode min;
	int nodeCount;
	public void insert(FibonacciHeapNode myNode, int key, int index) 
	{
		myNode.key = key;
		myNode.index = index;
		if (min != null) //Adding a myNode to the top level
		{
			myNode.left = min;
			myNode.right = min.right;
			min.right = myNode;
			myNode.right.left = myNode;
			if (key < min.key) //Check if the inserted myNode is smaller than the minimum myNode
			{ 
				min = myNode;
			}
		} 
		else 
		{
			myNode.right = myNode;
			min = myNode;
		}

		nodeCount++;
	}

	public FibonacciHeapNode removeMin() 
	{
		FibonacciHeapNode toBeRemoved = min;

		if (toBeRemoved != null) 
		{
			int z = toBeRemoved.degree; //We're storing the degree of the node to 
			//be removed because all those child nodes will be added to the top level
			FibonacciHeapNode remChild = toBeRemoved.child;
			FibonacciHeapNode childTemp;

			while (z > 0) //We keep moving nodes to the top level untill all the child nodes of min are exhausted
			{
				childTemp = remChild.right;
				remChild.left.right = remChild.right;
				remChild.right.left = remChild.left;

				remChild.left = min;
				remChild.right = min.right;
				min.right = remChild;
				remChild.right.left = remChild;

				remChild.parent = null;
				remChild = childTemp;
				z--;
			}

			toBeRemoved.left.right = toBeRemoved.right;
			toBeRemoved.right.left = toBeRemoved.left;

			if (toBeRemoved == toBeRemoved.right) 
			{
				min = null;
			} 
			else 
			{
				min = toBeRemoved.right;
				meld();
			}

			nodeCount--;
		}

		return toBeRemoved;
	}

	private static final double maxPossibleChildNodes = 1.0 / Math.log((1.0 + Math
			.sqrt(5.0)) / 2.0);
	
	protected void meld() 
	{
		int capacity = ((int) Math.floor(Math.log(nodeCount) * maxPossibleChildNodes)) + 1;
		
		List<FibonacciHeapNode> store = new ArrayList<FibonacciHeapNode>(capacity);

		for (int i = 0; i < capacity; i++) 
		{
			store.add(null); //adding all the nodes
		}	

		int numRoots = 0;
		
		FibonacciHeapNode remChild = min;
		if (remChild != null) 
		{
			numRoots++;
			remChild = remChild.right;
			while (remChild != min) 
			{
				numRoots++;
				remChild = remChild.right;
			}
		}

		while (numRoots > 0) 
		{
			int d = remChild.degree;
			FibonacciHeapNode next = remChild.right;

			for (;;) {
				FibonacciHeapNode degNode = store.get(d);
				if (degNode == null) {
					break;
				}

				if (remChild.key > degNode.key) {
					FibonacciHeapNode temp = degNode;
					degNode = remChild;
					remChild = temp;
				}

				link(degNode, remChild);
				store.set(d, null);
				d++;
			}
			store.set(d, remChild);remChild = next;
			numRoots--;
		}
		min = null;
			for (int i = 0; i < capacity; i++) 
			{
			FibonacciHeapNode degNode = store.get(i);
			if (degNode == null) {
				continue;
			}
			if (min != null) 
			{
				degNode.left.right = degNode.right;
				degNode.right.left = degNode.left;

				degNode.left = min;
				degNode.right = min.right;
				min.right = degNode;
				degNode.right.left = degNode;

				if (degNode.key < min.key) {
					min = degNode;
				}
			} else {
				min = degNode;
			}
		}
	}
	protected void removeNode(FibonacciHeapNode remChild, FibonacciHeapNode degNode) 
	{
		remChild.left.right = remChild.right;
		remChild.right.left = remChild.left;
		degNode.degree--;

		if (degNode.child == remChild) {
			degNode.child = remChild.right;
		}

		if (degNode.degree == 0) {
			degNode.child = null;
		}

		remChild.left = min;
		remChild.right = min.right;
		min.right = remChild;
		remChild.right.left = remChild;

		remChild.parent = null;

		remChild.childCut = false;
	}
	protected void link(FibonacciHeapNode degNode, FibonacciHeapNode remChild) 
	{
		degNode.left.right = degNode.right;
		degNode.right.left = degNode.left;

		degNode.parent = remChild;

		if (remChild.child == null) //If the node doesn't have a child node, add degNode as a child
		{
			remChild.child = degNode;
			degNode.right = degNode;
			degNode.left = degNode;
		}
		else //Else adjust the childNodes to add degNode as a child
		{
			degNode.left = remChild.child;
			degNode.right = remChild.child.right;
			remChild.child.right = degNode;
			degNode.right.left = degNode;
		}

		remChild.degree++;

		degNode.childCut = false; //Set it to false now since it acts as a new node without any operations in it
	}
	public void decreaseKey(FibonacciHeapNode remChild, int decVal) //To decrease the value of a node
	//to decVal
	{
		if (decVal > remChild.key) 
		{
			throw new IllegalArgumentException(
					"decreaseKey() got larger key value");
		}

		remChild.key = decVal;
		FibonacciHeapNode degNode = remChild.parent;
		if ((degNode != null) && (remChild.key < degNode.key)) //Check if the value after decrease key
			//becomes less than the value of its parent. If so, the node needs to be removed and added
			//to the top most level
		{
			removeNode(remChild, degNode);
			cascadingCut(degNode);
		}

		if (remChild.key < min.key) //since the node is being added to the top most level,
			//check if it is less than min. If yes, set this node as min.
		{
			min = remChild;
		}
	}

	private void cascadingCut(FibonacciHeapNode degNode) //To check the cascading cut. If it is false,
	//remove node and make it true.
	//Else keep going up until you find the first node where it is false.
	{
		FibonacciHeapNode toBeRemoved = degNode.parent;

		if (toBeRemoved != null) 
		{
			if (!degNode.childCut) 
			{
				degNode.childCut = true;
			} else 
			{
				removeNode(degNode, toBeRemoved);
				cascadingCut(toBeRemoved);
			}
		}
	}
}

