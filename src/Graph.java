import java.util.*;

public class Graph 
{
	
	public class DefGraph 
	{
		int ind;
		int wt;
		DefGraph(int i, int w) 
		{
			ind = i;
			wt = w;

		}
		public void display()
		{
				System.out.print(ind  + "(" + wt + ")");
		}
		
		public int getWt()
		{
			return wt;
		}

		public int getInd()
		{
			return ind;
			
		}
	}



	ArrayList<DefGraph>[] adjacency;
	int Vnum;
	
	Graph(int v) {

		adjacency = (ArrayList<DefGraph>[]) new ArrayList[v];
		for (int i = 0; i < v; i++)
			adjacency[i] = new ArrayList<DefGraph>();
		Vnum = v;
	}

	

	// displayGraph the nodes and edges of the graph

		public void displayGraph() {

		for (int i = 0; i < Vnum; i++) 
		{

			System.out.print("vertex[" + i + "] = ");

			Iterator<DefGraph> iter = adjacency[i].iterator();

			while (iter.hasNext()) 
			{

				iter.next().display();

			}

			System.out.println("");

		}
	}
	
	public void addToGraph(int v1, int v2, int w) 
	{

		DefGraph NodeA = new DefGraph(v1, w);
		DefGraph NodeB = new DefGraph(v2, w);

		adjacency[v1].add(NodeB);
		adjacency[v2].add(NodeA);

	}


	public LinkedList<Integer> dijkstra(int sourceNode, int desNode) 
	{
		
		Fibonacci fibo = new Fibonacci();
		FibonacciHeapNode fiboNode[] = new FibonacciHeapNode[Vnum];
		int s2d = 0;
		
		int p[] = new int[Vnum];

		boolean sptSet[] = new boolean[Vnum];

		for (int i = 0; i < Vnum; i++) {
			
			fiboNode[i] = new FibonacciHeapNode();
			
			if(i != sourceNode)
			fibo.insert(fiboNode[i], Integer.MAX_VALUE, i);
			
			sptSet[i] = false;
		}

		fibo.insert(fiboNode[sourceNode], 0 , sourceNode);

		for (int count = 0; count < Vnum-1; count++)
		{
			FibonacciHeapNode temp = fibo.removeMin();
			int syn = temp.index;
			
			sptSet[syn] = true;

			Iterator<DefGraph> iter = adjacency[syn].iterator();

			while (iter.hasNext()) {

				DefGraph gn = iter.next();
				int v = gn.getInd();
				if (!sptSet[v] && (fiboNode[syn].keyValue() + gn.getWt()) < fiboNode[v].keyValue()) {
					fibo.decreaseKey(fiboNode[v], fiboNode[syn].keyValue() + gn.getWt());
					//dist[v] = dist[syn] + gn.getWt();
					if(v == desNode) s2d = fiboNode[syn].keyValue() + gn.getWt();
					p[v] = syn;
				}
			}

		}

		int x = desNode;
		LinkedList ll = new LinkedList();

		while (x != sourceNode) {
			ll.add(x);
			x = p[x];
		}
		ll.add(sourceNode);

		Collections.reverse(ll);


		System.out.println(s2d);

		return ll;
	}
//this is the Dijkstra's function that is used to get the shortest path. It takes the source node as a parameter
	//The result of Dijkstra is stored in a Hashmap.
	//Tha Hashmap stores the node and the shortest paths.
    public HashMap<Integer,LinkedList<Integer>> dijkstra(int sourceNode) {

        Fibonacci fibo = new Fibonacci();
        FibonacciHeapNode fiboNode[] = new FibonacciHeapNode[Vnum];
        int s2d[] = new int[Vnum];

        //int dist[] = new int[Vnum];
        int p[] = new int[Vnum];

        boolean sptSet[] = new boolean[Vnum];

        for (int i = 0; i < Vnum; i++) {

            s2d[i]=0;
            fiboNode[i] = new FibonacciHeapNode();

            if(i != sourceNode)
                fibo.insert(fiboNode[i], Integer.MAX_VALUE, i);

            //dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        fibo.insert(fiboNode[sourceNode], 0 , sourceNode);

        for (int count = 0; count < Vnum-1; count++) {

            //int syn = minDistance(dist, sptSet);

            FibonacciHeapNode temp = fibo.removeMin();
            int syn = temp.index;

            sptSet[syn] = true;

            Iterator<DefGraph> iter = adjacency[syn].iterator();

            while (iter.hasNext()) {

                DefGraph gn = iter.next();
                int v = gn.getInd();
                if (!sptSet[v] && (fiboNode[syn].keyValue() + gn.getWt()) < fiboNode[v].keyValue()) {
                    fibo.decreaseKey(fiboNode[v], fiboNode[syn].keyValue() + gn.getWt());
                    s2d[v] = fiboNode[syn].keyValue() + gn.getWt();
                    p[v] = syn;
                }
            }

        }

        HashMap<Integer,LinkedList<Integer>> shortestPaths = new HashMap<Integer, LinkedList<Integer>>();
//        int x = desNode;
        for(int i=0;i<Vnum;i++) {
            LinkedList<Integer> ll = new LinkedList<Integer>();
            int x = i;
            while (x != sourceNode) {
                ll.add(x);
                x = p[x];
            }
            ll.add(sourceNode);

            Collections.reverse(ll);
            shortestPaths.put(i,ll);
        }

        return shortestPaths;
    }
}
