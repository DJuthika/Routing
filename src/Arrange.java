import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Arrange 
{
    public static Graph makeGraph(String location)
    {
    	
        
        BufferedInputStream b = null;
        DataInputStream d = null;
        FileInputStream f = null;

        try {
        	
            f = new FileInputStream(location);
            b = new BufferedInputStream(f);
            d = new DataInputStream(b);

            String input = new String(d.readLine());
            
            String values[] = input.split(" "); 
            int v = Integer.parseInt(values[0]); //v is the number of vertices
            int e = Integer.parseInt(values[1]); //e is the number of edges
            Graph g = new Graph(v); //graph has v vertices and is of the size v*v
            String delimitor = " ";
            
            for (int i = 0; i < e; i++) //Read the input file
            {

                input = d.readLine();

                if(input.equals(" ")) {
                    i--;
                    continue;
                }

                values = input.split(delimitor);
                
                int vertex1 = Integer.parseInt(values[0]);
                int vertex2 = Integer.parseInt(values[1]);
                int w = Integer.parseInt(values[2]);

                g.addToGraph(vertex1, vertex2, w);

            }
            f.close();
            b.close();
            d.close();
            
            return g;

        } catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    private static char[] intToBinary(String inputData) 
    {
        byte[] bytes = new byte[0];
        try {
            bytes = InetAddress.getByName(inputData).getAddress();
        } catch (UnknownHostException e) 
        {
            e.printStackTrace();
        }
        String outputData = new BigInteger(1, bytes).toString(2);
        return outputData.toCharArray();
    }
    
    public static LinkedList<char[]> getIP(String location, int numberNodes)
    {
        LinkedList<char[]> L = new LinkedList<char[]>();
        
        FileInputStream f = null;
        BufferedInputStream b = null;
        DataInputStream d = null;

        try {
            f = new FileInputStream(location);
            b = new BufferedInputStream(f);
            d = new DataInputStream(b);

            for (int i = 0; i < numberNodes; i++) {

                String input = d.readLine();
                L.add(intToBinary(input));
                d.readLine();
            }
            f.close();
            b.close();
            d.close();


        } catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return L;
    }

    public static String showArray(char[] intermediateNode) {
        String str="";
        for(int i=0;i<32 && intermediateNode[i]!='*';i++){
            str = str+intermediateNode[i];
        }
        return str;
    }
}
