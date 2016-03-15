import java.util.ArrayList;
import java.util.List;

public class Fibonacci {
	FibonacciHeapNode min;
	int nodeCount;
	public void insert(FibonacciHeapNode myNode, int key, int index) 
	{
		/* The insert operation adds a new node to the Fibonacci heap.
		The steps followed to insert a node to the Fibonacci heap are:
		i. check if min is null, i.e Fibonacci heap doesn't exist. If true:
			a. Add the node passed on to this function as min node and point it to itself
			   since it doesn't have any neighbors yet.
		ii. If there is a min:
			a. Add this node to the top level of the Fibonacci heap ie as a neighbor to the min node.
			b. Adjust the pointers to and from this node
			c. Check if the key of this node is less than min. If yes, set it as min.
		*/
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
		/* To remove the min node, we also need to take care of all its child pointers.
		i.We have to add the children of the min node to the top level.
		ii.After that, delete the min node and adjust pointers.
		iii.Find a new min node. This is done using meld.
		*/
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
		/*
		The meld operation is to meld two Fibonacci heaps. 
		We form parent-child relationships between two nodes if they have the same degree where degree is the number of child
		nodes a node has.
		*/
		int capacity = ((int) Math.floor(Math.log(nodeCount) * maxPossibleChildNodes)) + 1;
		
		List<FibonacciHeapNode> store = new ArrayList<FibonacciHeapNode>(capacity);

		for (int i = 0; i < capacity; i++) 
		{
			store.add(null); //adding all the nodes
		}	

		int numRoots = 0;
		
		FibonacciHeapNode remChild = min;
		//Finding out the total number of roots. 
		if (remChild != null) 
		{
			numRoots++;
			remChild = remChild.right;
			//Since we have a double linked list on all levels, the ending condition would be when root is
			//visited again, meaning all nodes have been visited on this level.
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
			//Moving along the degree arraylist.
			for (;;) {
				FibonacciHeapNode degNode = store.get(d);
				if (degNode == null) {
					break;
				}
				//If another node has a degree k, we need to make a link between that node and the current one,
				//i.e remChild and degNode. This will be done on the basis of their keys.
				if (remChild.key > degNode.key) {
					//Swap the two nodes.
					FibonacciHeapNode temp = degNode;
					degNode = remChild;
					remChild = temp;
				}
				//Link the two nodes and set the value at degree d to null as degree of both these nodes have changed.
				link(degNode, remChild);
				store.set(d, null);
				d++;
			}
			//Associate an integer, degree to a node with that degree.
			store.set(d, remChild);
			remChild = next;
			numRoots--;
		}
		min = null;
		`	//Find the new min. 
			//Check for each node on the top level. Manage pointers and check against the key on min node.
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
	/*
	A removeNode is doe to remove a particular node from the fibonacci heap given the pointer to that node.
	Remove the node, change its right-left links.
	Now add its children to the top level.
	*/
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
	/*
	Decrease key is to decrease the key of a given node by a particular value.
	If this value doesn't go below it's parent's value, there isn't a problem. 
	Otherwise, this needs to be added to the top level and assigned a the new min.
	*/
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
	/*Cascading cut is a feature of the fibonacci heap that controls how many decrese keys or remove mins can take place.
	The cascading cut is set to false when a node is added.
	If the node loses a child, the cascading cut of the node becomes true.
	That means the next time it loses a child, it'll have to move out of its current location and into the top level.
	*/
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

