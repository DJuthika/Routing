import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ssp {
    public static Graph readGraph(String path)
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        //Read the file and get the graph

        try {
            fis = new FileInputStream(path);

            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

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

//    public static LinkedList<char[]> readIPFromFile(String path, int numberNodes){
//        LinkedList<char[]> L = new LinkedList<char[]>();
//        FileInputStream fis = null;
//        BufferedInputStream bis = null;
//        DataInputStream dis = null;
//
//        // this try clause consist of code to read the vertices and edges of the
//        // graph from the input file
//        try {
//            fis = new FileInputStream(path);
//            bis = new BufferedInputStream(fis);
//            dis = new DataInputStream(bis);
//
//            for (int i = 0; i < numberNodes; i++) 
//            {
//
//                @SuppressWarnings("deprecation")
//				String inp = dis.readLine();
//                L.add(intToBinary(inp));
//                String readLine = dis.readLine();
//            }
//            fis.close();
//            bis.close();
//            dis.close();
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return L;
//    }
//
//    private static char[] intToBinary(String data_in)
//    {
//        byte[] bytes = new byte[0];
//        try 
//        {
//            bytes = InetAddress.getByName(data_in).getAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        String data_out = new BigInteger(1, bytes).toString(2);
//        return data_out.toCharArray();
//
//
//    }

//    public static String showArray(char[] midNode)
//    {
//        String str="";
//        for(int i=0;i<32 && midNode[i]!='*';i++)
//        {
//            str = str+midNode[i];
//        }
//        return str;
//    }

	public static void main(String args[]) {

	int src = Integer.parseInt(args[1]);
	int dest = Integer.parseInt(args[2]);
	String path = new String(args[0]);
		//int src = 0;
		//int dest = 999999;
	//String path = "G:/input_1000000 (1).txt";
//        String path="G:/input_graphsmall_part2.txt";
	//long startTime = System.currentTimeMillis();
	
        Graph g = readGraph(path);
		LinkedList<Integer> shortestPath = g.dijkstra(src, dest);
        Iterator<Integer> iterator = shortestPath.iterator();

        while (iterator.hasNext()) {

            System.out.print(iterator.next() + " ");

        }
        
        //long stopTime = System.currentTimeMillis();
        //long elapsedTime = stopTime - startTime;
        //System.out.println(elapsedTime);


	}
}
