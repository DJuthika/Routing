
public class Tries {

    private class TrieNode
    //Defining the Trie
    {
        char dest[];
        int next;
        boolean branchCheck;
        TrieNode zeroChild, oneChild, parent;
        int index;


        public TrieNode(int index, TrieNode parent)
        {
            branchCheck = true;
            this.index = index;
            this.parent = parent;
        }
        
        TrieNode(char[] dest, int next, TrieNode parent)
        {
            this.dest = dest;
            this.next = next;
            this.parent = parent;
            branchCheck = false; //Check if it is a branch node
        }
    }

    TrieNode trieRoot;

    Tries()
    {
        trieRoot = null;
    }

    //The function insert inserts a new IP of the destination and the next hop. If the root isn't initialized,
    //create new root.
    void insert(char dest[], int nextHop)
    { 
    	
        if(trieRoot==null)
        {
            trieRoot = new TrieNode(dest,nextHop,null);
            return;
        }

        //Keep checking if you've reached the branch node. If you haven't, move to the left or the right
        //depending on the bit on the IP.
        TrieNode temp = trieRoot;
        while(temp.branchCheck)
        {
            if(dest[temp.index]==0)
                temp = temp.zeroChild;
            else
                temp = temp.oneChild;
        }
        int i;
        for(i=0;i<32;i++)  //Finding the first position where there is a difference.
        {
            if(dest[i]!=temp.dest[i])
                break;
        }
       
        branchAt(dest,nextHop,temp.parent,i);//Creating a branch node where there is a difference.

    }

  
    private void branchAt(char[] dest, int nexthop, TrieNode branchNode, int pos) {

        //Traverse up the trie. When you find a node where index is less than pos, make a node and add it to its appropriate
    	//position
        while(branchNode!=null && branchNode.index > pos){
            branchNode = branchNode.parent;
        }
        //camelBranch and camelLeaf are the new branch nodes and new leaf nodes respectively
        TrieNode camelBranch = new TrieNode(pos,branchNode);
        TrieNode camelLeaf = new TrieNode(dest,nexthop,camelBranch);
        //if you reach the root,
        if (branchNode==null)
        {
        	//dinoRoot is the old root.
            TrieNode dinoRoot = trieRoot;
            dinoRoot.parent = camelBranch;
            if(dest[pos]=='0')
            {
                camelBranch.zeroChild = camelLeaf;
                camelBranch.oneChild = dinoRoot;
            } else 
            {
                camelBranch.zeroChild = dinoRoot;
                camelBranch.oneChild = camelLeaf;
            }
            trieRoot = camelBranch;
        }
        else 
        {	//insert at the appropriate position
            TrieNode dinoNode; //dinoNode is the old node
            if(dest[branchNode.index]=='0'){
                dinoNode = branchNode.zeroChild;
                branchNode.zeroChild = camelBranch;
            } else {
                dinoNode = branchNode.oneChild;
                branchNode.oneChild = camelBranch;
            }

            //set the old node as the new branch's parent.
            dinoNode.parent = camelBranch;

            if(dest[pos]=='0')
            {
                camelBranch.zeroChild = camelLeaf;
                camelBranch.oneChild = dinoNode;
            } else 
            {
                camelBranch.oneChild = camelLeaf;
                camelBranch.zeroChild = dinoNode;
            }
        }

    }
    //This function compresses the trie
    public void minimize() 
    {
        minimize(trieRoot);
    }

    private void minimize(TrieNode node)
    {
        if(node==null)
            return;

        if(node.branchCheck)
            if(node.zeroChild.branchCheck)
                minimize(node.zeroChild);
            else if(node.index<31)
                    node.zeroChild.dest[node.index+1]='*';
            if(node.oneChild.branchCheck)
                minimize(node.oneChild);
            else if(node.index<31)
                    node.oneChild.dest[node.index+1]='*';

        if(node.branchCheck
                && !node.zeroChild.branchCheck && !node.oneChild.branchCheck
                && node.zeroChild.next==node.oneChild.next){
            node.dest = node.zeroChild.dest;
            node.branchCheck = false;
            node.next = node.zeroChild.next;
            node.zeroChild = null;
            node.oneChild = null;
            node.dest[node.parent.index+1]='*';
        }

    }
    //d is the destination array
    public char[] search(char[] d) 
    {
        TrieNode temp = trieRoot;
        if(temp==null) //check if the value of the node is null. If yes, return.
            return null;
      //When it is a branch node, check the index on the IP to move to the left or the right.
        while(temp.branchCheck)
        {  
            if(d[temp.index]=='0'){
                temp=temp.zeroChild;
            } else {
                temp=temp.oneChild;
            }
        }


        return temp.dest;
    }

    public void print(){
        print(trieRoot,0);
    }

    private void print(TrieNode node, int level){
        System.out.println(" " + level);
        if(node.branchCheck){
            System.out.println("Index = "+node.index);
            System.out.println("Left -");
            print(node.zeroChild, level + 1);
            System.out.println("Right -");
            print(node.oneChild,level+1);
        } else {
            System.out.println(Arrange.showArray(node.dest)+" "+node.next);
        }
    }
}