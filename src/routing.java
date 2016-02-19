import java.io.*;
import java.math.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class routing {
    public static Graph readGraphFromFile(String path)
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        
        try {
            fis = new FileInputStream(path);//Read the file

            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);//Read the buffer as a string

            String inp = new String(dis.readLine());
            String delim = " ";
            String worksets[] = inp.split(delim);
            int vertices = Integer.parseInt(worksets[0]);
            int edges = Integer.parseInt(worksets[1]);
            Graph g = new Graph(vertices);

            for (int i = 0; i < edges; i++) {

                inp = dis.readLine();

                if(inp.equals("")) {
                    i--;
                    continue;
                }

                worksets = inp.split(delim);
                int v1 = Integer.parseInt(worksets[0]);
                int v2 = Integer.parseInt(worksets[1]);
                int w = Integer.parseInt(worksets[2]);

                g.addToGraph(v1, v2, w);

            }
            fis.close();
            bis.close();
            dis.close();
            return g;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LinkedList<char[]> readIPFromFile(String path, int numberNodes){
        LinkedList<char[]> linkedList = new LinkedList<char[]>();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        //Read the graph
        try {
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            for (int i = 0; i < numberNodes; i++) {

                String inp = dis.readLine();
                linkedList.add(convertToBinary(inp)); //Add to the list after converting the input to binary
                dis.readLine();
            }
            fis.close();
            bis.close();
            dis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkedList;
    }

    private static char[] convertToBinary(String data_in) //convert input to binary
    {
        byte[] bytes = new byte[0];
        try {
            bytes = InetAddress.getByName(data_in).getAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String data_out = new BigInteger(1, bytes).toString(2);
        return data_out.toCharArray();


    }

    public static String displayCharArray(char[] midNode) //If midNode is not marked, add it to the string
    {
        String str="";
        for(int i=0;i<32 && midNode[i]!='*';i++){
            str = str+midNode[i];
        }
        return str; //display string
    }

    public static void main(String args[])
    {

       //Read the input from the command line
    		String graphPath=null;
    	//"G:/input_graphsmall_part2.txt";
        String ipFile=null;
        //"G:/input_ipsmall_part2.txt";
        int sourceNodeNumber=-1;
        //=1;
        int destinationNodeNumber=-1;
        //=6;
        try {
        		graphPath = args[0];
        		ipFile = args[1];
            sourceNodeNumber = Integer.parseInt(args[2]);
            destinationNodeNumber = Integer.parseInt(args[3]);
        } catch (Exception e){
            System.out.println("Command line arguments formatting error.");
            System.out.print("Expected format : ");
            System.out.println("java routing path_to_first_file path_to_second_file source_number destination_number");
        }

        //Read graph
        Graph graph = readGraphFromFile(graphPath);

        
        LinkedList<char[]> IPAddr = readIPFromFile(ipFile, graph.Vnum);//Read the IP Addresses from the file
        //Store it in a linked list



        //path is the shortest path that we get using the dijkstra's algorithm
        LinkedList<Integer> path = graph.dijkstra(sourceNodeNumber, destinationNodeNumber);

        // In a hash table, store the path and the next hop
        HashMap<Integer,HashMap<Integer,Integer>> nodePaths = new HashMap<Integer, HashMap<Integer, Integer>>();
        for (int i=0; i<path.size()-1;i++)
        {
            HashMap<Integer,Integer> destinationPath = new HashMap<Integer, Integer>();

            // call dijsktra and get shortest paths
            HashMap<Integer,LinkedList<Integer>> fullPath = graph.dijkstra(path.get(i));

            
            for(int j=0;j<graph.Vnum;j++)
            {
                if(j!=path.get(i))
                    destinationPath.put(j, fullPath.get(j).get(1));
            }
           nodePaths.put(path.get(i),destinationPath);
        }

        //Generate binary tires for nodes minus the destination node
        Tries tries[] = new Tries[path.size()-1];
        for(int i=0;i<tries.length;i++)
        {
            tries[i] = new Tries();
            int currentNode = path.get(i);
            for(int j=0;j<graph.Vnum;j++)
            {
                if(currentNode!=j){
                    char[] destnIpAddr = IPAddr.get(j);
                    tries[i].insert(destnIpAddr,nodePaths.get(currentNode).get(j));
                }
            }

            tries[i].minimize();

        }

        //Get and print the prefix IP
        for(int i=0;i<tries.length;i++)
        {
            char midNode[] =tries[i].search(IPAddr.get(destinationNodeNumber));
            System.out.print(displayCharArray(midNode)+" ");
        }

    }
}
