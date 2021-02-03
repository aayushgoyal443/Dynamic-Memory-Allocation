import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.
        
    public BSTree(){  
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }    

    public BSTree(int address, int size, int key){
        super(address, size, key); 
    }

    public BSTree Insert(int address, int size, int key) 
    { 
        BSTree newnode = new BSTree(address, size, key);
        BSTree curr = this.getRoot();
        int comp=0;
        if (curr ==null){
            this.right = newnode;
            newnode.parent = this;
            return newnode;
        }
        while (true){
            comp  = curr.compareNode(newnode);
            if (comp ==0) return null;
            else if (comp==-1){
                if (curr.right != null) curr = curr.right;
                else{
                    curr.right = newnode;
                    newnode.parent = curr;
                    return newnode;
                }
            }
            else{
                if (curr.left != null) curr = curr.left;
                else{
                    curr.left = newnode;
                    newnode.parent = curr;
                    return newnode;
                }
            }
        }
    }

    public boolean Delete(Dictionary e)
    { 
        BSTree curr = this.getRoot();
        int comp;
        int found = 0;
        while(curr !=null){
            comp = curr.compareDict(e);
            if (comp ==0){
                found = 1;   // now I am gonna delete this node
                break;
            }
            else if(comp ==-1){
                curr = curr.right;
            }
            else{
                curr = curr.left;
            }
        }
        if (found==0) return false;
        // Now i got reference to the node which must be deleted

        // The case when the node to be dleted is a leaf node
        if (curr.left==null && curr.right ==null){
            if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                curr.parent.left = null;
                curr.parent =null;
            }
            else{
                curr.parent.right = null;
                curr.parent =null;
            }
        }
        // When it has only one child
        else if (curr.left==null){
            if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                curr.parent.left = curr.right;
                curr.right.parent = curr.parent;
                curr.parent =null;
                curr.right =null;
            }
            else{
                curr.parent.right = curr.right;
                curr.right.parent = curr.parent;
                curr.right =null;
                curr.parent =null;
            }
        }
        else if (curr.right ==null){
            if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                curr.parent.left = curr.left;
                curr.left.parent = curr.parent;
                curr.parent =null;
                curr.left =null;
            }
            else{
                curr.parent.right = curr.left;
                curr.left.parent = curr.parent;
                curr.left =null;
                curr.parent =null;
            }
        }
        // When it has both child
        else{
            BSTree temp = curr;
            curr = curr.getNext();
            temp.address  =curr.address;
            temp.key = curr.key;
            temp.size = curr.size;
            
            // deletingg the successor node
            if (curr.right ==null){
                if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                    curr.parent.left = null;
                    curr.parent =null;
                }
                else{
                    curr.parent.right = null;
                    curr.parent =null;
                }
            }
            else {
                if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                    curr.parent.left = curr.right;
                    curr.right.parent = curr.parent;
                    curr.parent =null;
                    curr.right =null;
                }
                else{
                    curr.parent.right = curr.right;
                    curr.right.parent = curr.parent;
                    curr.right =null;
                    curr.parent =null;
                }
            }
        }

        return true;
    }
    private static BSTree elem = null;    
    public BSTree Find(int key, boolean exact)
    { 
        elem = null;
        if (exact ==true){
            BSTree curr = this.getRoot();
            while(curr!=null){
                if (curr.key == key){
                    elem = curr;
                    curr = curr.left;
                }
                else if (curr.key>key){
                    curr = curr.left;
                }
                else{
                    curr = curr.right;
                }
            }
        }
        else{
            BSTree curr = this.getRoot();
            while(curr!=null){
                if (curr.key >= key){
                    elem = curr;
                    curr = curr.left;
                }
                else if (curr.key<key){
                    curr = curr.right;
                }
            }
        }
        if (elem ==null) return null;
        BSTree duplicate = new BSTree(elem.address, elem.size, elem.key);        
        return duplicate;
    }

    public BSTree getFirst(){ 
        BSTree curr = this.getRoot();
        if (this.getRoot()==null) return null;
        while(curr.left!=null){
            curr = curr.left;
        }
        return curr;
    }

    public BSTree getNext()
    { 
        // We will not go left since it has already been analysed
        BSTree curr = this;
        if (curr.right == null){
            // Now I am ready to go up till I find a node just successor of "this" node
            while (curr.parent!=null){
                if (curr.compareNode(this)==1) return curr;
                else curr = curr.parent;
            }
            return null;
        }
        else{
            // Must finish my business in the Right first
            curr = curr.right;
            while(curr.left !=null){
                curr  = curr.left;
            }
            return curr;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean sanity(){
        if (nothasloop(this,null)==false){
            System.out.println("It has a loop");
            return false;
        }
        s.clear();
        if (check_BST(this.getRoot())==false){
            System.out.println("The BST property is violated");
            return false;
        } 
        if (checkpointers(this.getRoot())==false){
            System.out.println("the pointers are not correctly defined");
            return false;
        } 
        return true;
    }

    private static HashSet <BSTree> s= new HashSet<BSTree>();    

    private boolean nothasloop(BSTree n, BSTree prev){
        if (n==null) return true;
        if (s.contains(n)) return false;
        else{
            s.add(n);
            boolean l=true,r=true,p=true;
            if (n.left!=null && n.left!=prev) l = nothasloop(n.left, n);
            if (n.right!=null && n.right!=prev) r = nothasloop(n.right, n);
            if (n.parent!=null && n.parent!=prev) p = nothasloop(n.parent, n);
            return l && r && p;
        }
    }

    private boolean check_BST(BSTree n){
        if (n== null) return true;
        else if (n.left !=null && n.compareNode(n.left)!=1){
            return false;
        }
        else if (n.right!=null && n.compareNode(n.right)!=-1){
            return false;
        }
        else return check_BST(n.left) && check_BST(n.right);
    }

    private boolean checkpointers(BSTree n){
        // boolean l = true,r= true, p =true;
        if (n== null) return true;
        if (n.left!= null && n.left.parent!=n){
            return false;
        }
        if (n.right!=null && n.right.parent!=n){
            return false;
        }
        return checkpointers(n.left) && checkpointers(n.right);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private BSTree getRoot(){
        // returns the actual root of the tree, not the sentinel one.
        BSTree curr  =  this ;
        if (curr.parent ==null){
            return curr.right;
        }
        while (curr.parent.parent != null){
            curr = curr.parent;
        }
        return curr;
    }

    private int compareNode(BSTree d){
        BSTree b = this;
        if (b.key != d.key){
            if (b.key>d.key) return 1;
            else return -1;
        }
        else {
            if (b.address> d.address) return 1;
            else if (b.address == d.address) return 0;
            else return -1;
        }
    }

    private int compareDict(Dictionary d){
        BSTree b = this;
        if (b.key != d.key){
            if (b.key>d.key) return 1;
            else return -1;
        }
        else {
            if (b.address> d.address) return 1;
            else if (b.address == d.address) return 0;
            else return -1;
        }
    }
    
}


 


