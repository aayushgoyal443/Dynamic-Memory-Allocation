import java.util.HashSet;

// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the BSTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    public AVLTree Insert(int address, int size, int key) 
    {
        AVLTree newnode = new AVLTree(address, size, key);
        AVLTree curr = this.getRoot();
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
                    break;
                }
            }
            else{
                if (curr.left != null) curr = curr.left;
                else{
                    curr.left = newnode;
                    newnode.parent = curr;
                    break;
                }
            }
        }
        // Newnode is inserted and now we have to update heights of all it's parents
        newnode.updateheight();
        // Done
        return newnode;
    }

    private void updateheight(){
        AVLTree curr = this;
        int l=-1, r=-1;
        while(curr.parent!=null){
            l=-1;
            r=-1;
            if (curr.left!=null) l = curr.left.height;
            if (curr.right!=null) r = curr.right.height;
            if (l>r) curr.height = l+1;
            else curr.height = r+1;
            // Now we have to check whether height property is getting violated or not
            if (l>r+1 || r>l+1){
                // We will rebalance it (rotation or quiz method) and then update the heights.
                // x,y,z can be easily obtained if z is known     
                curr = curr.rebalance();
            }
            curr =curr.parent;
        }
    }

    private AVLTree getBig(){
        // This fnction returns the child with more height
        if (this.left==null && this.right==null) return null;
        else if (this.right==null) return this.left;
        else if (this.left==null) return this.right;
        else{
            if (this.left.height>this.right.height) return this.left;
            else if (this.left.height<this.right.height) return this.right;
            else{
                if (this.parent!=null && this.parent.left==this) return this.left;
                else return this.right;
            }
        }
    }
    
    private AVLTree rebalance(){
        AVLTree Allfather = this.parent;
        AVLTree x,y,z= this;
        AVLTree[] xyz = new AVLTree[3];
        AVLTree[] Ts = new AVLTree[4];
        // Make x and y using the fact that they are the children's with bigger heights;
        y= z.getBig();
        x= y.getBig();

        // Make the sorted xyz[] and Ts[] array by considering 4 cases;
        if (y==z.left && x==y.left){
            xyz[0] =x;
            xyz[1] =y;
            xyz[2] =z;
            Ts[0] =x.left;
            Ts[1] =x.right;
            Ts[2] =y.right;
            Ts[3] =z.right;
        }
        else if (y==z.left && x==y.right){
            xyz[0] =y;
            xyz[1] =x;
            xyz[2] =z;
            Ts[0] =y.left;
            Ts[1] =x.left;
            Ts[2] =x.right;
            Ts[3] =z.right;
        }
        else if (y== z.right && x== y.left){
            xyz[0] =z;
            xyz[1] =x;
            xyz[2] =y;
            Ts[0] =z.left;
            Ts[1] =x.left;
            Ts[2] =x.right;
            Ts[3] =y.right;
        }
        else{
            xyz[0] =z;
            xyz[1] =y;
            xyz[2] =x;
            Ts[0] =z.left;
            Ts[1] =y.left;
            Ts[2] =x.left;
            Ts[3] =x.right;
        }
        
        // Do the labour of assigning pointers
        if (Allfather.left!= null  && Allfather.left.compareNode(z)==0) Allfather.left = xyz[1];
        else if (Allfather.right!= null  && Allfather.right.compareNode(z)==0) Allfather.right = xyz[1];
        xyz[1].parent = Allfather;
        xyz[1].left=(xyz[0]);
        xyz[0].parent = xyz[1];
        xyz[1].right = (xyz[2]);
        xyz[2].parent = xyz[1];
        xyz[0].right = (null);
        xyz[0].left=(null);
        xyz[2].left=(null);
        xyz[2].right = (null);

        for (int i=0;i<4;i++){
            if (Ts[i]==null) continue;
            if (i%2==0){
                if(i<=1) xyz[0].left = Ts[i];
                else xyz[2].left=Ts[i];
            }
            else{
                if(i<=1) xyz[0].right = Ts[i];
                else xyz[2].right=Ts[i];
            }
            if(i<=1) Ts[i].parent = xyz[0];
            else Ts[i].parent = xyz[2];
        }

        // Do the labour of assigning heihgts;
        int l,r;
        l=-1;
        r=-1;
        if (xyz[0].left!=null) l= xyz[0].left.height;
        if (xyz[0].right!=null) r= xyz[0].right.height;
        if (l>r) xyz[0].height=l+1;
        else xyz[0].height = r+1;
        l=-1;
        r=-1;
        if (xyz[2].left!=null) l= xyz[2].left.height;
        if (xyz[2].right!=null) r= xyz[2].right.height;
        if (l>r) xyz[2].height=l+1;
        else xyz[2].height = r+1;
        l=-1;
        r=-1;
        if (xyz[1].left!=null) l= xyz[1].left.height;
        if (xyz[1].right!=null) r= xyz[1].right.height;
        if (l>r) xyz[1].height=l+1;
        else xyz[1].height = r+1;

        return xyz[1];
    }

    public boolean Delete(Dictionary e)
    {
        AVLTree curr = this.getRoot();
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
        // I must delete that node and store a reference to the parent of required leaf in start.
        AVLTree start;

         // The case when the node to be deleted is a leaf node
        if (curr.left==null && curr.right ==null){
            if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                curr.parent.left = null;
                start = curr.parent;
                curr.parent =null;
            }
            else{
                curr.parent.right = null;
                start = curr.parent;
                curr.parent =null;
            }
        }
        // When it has only one child
        else if (curr.left==null || curr.right==null){
            start = curr;
            if (curr.left==null) curr = curr.right;
            else curr = curr.left;
            start.address = curr.address;
            start.key= curr.key;
            start.size= curr.size;
            if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                curr.parent.left = null;
                curr.parent =null;
            }
            else{
                curr.parent.right = null;
                curr.parent =null;
            }
        }
        // When it has both child
        else{
            AVLTree temp = curr;
            curr = curr.getNext();
            temp.address  =curr.address;
            temp.key = curr.key;
            temp.size = curr.size;
            
            // deletingg the successor node ie curr node
            if (curr.right ==null){
                if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                    curr.parent.left = null;
                    start = curr.parent;
                    curr.parent =null;
                }
                else{
                    curr.parent.right = null;
                    start = curr.parent;
                    curr.parent =null;
                }
            }
            else {
                start = curr;
                curr = curr.right;
                start.address = curr.address;
                start.key= curr.key;
                start.size= curr.size;
                if (curr.parent.left!= null  && curr.parent.left.compareNode(curr)==0){
                    curr.parent.left = null;
                    curr.parent =null;
                }
                else{
                    curr.parent.right = null;
                    curr.parent =null;
                }
            }
        }

        // Now I will just start the update height process on start node.
        start.updateheight();

        return true;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
        
    public AVLTree Find(int key, boolean exact)
    { 
        AVLTree elem = null;
        if (exact ==true){
            AVLTree curr = this.getRoot();
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
            AVLTree curr = this.getRoot();
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
        AVLTree duplicate = new AVLTree(elem.address, elem.size, elem.key);        
        return duplicate;
    }
    public AVLTree getFirst(){ 
        AVLTree curr = this.getRoot();
        if (this.getRoot()==null) return null;
        while(curr.left!=null){
            curr = curr.left;
        }
        return curr;
    }
    public AVLTree getNext()
    { 
        // We will not go left since it has already been analysed
        AVLTree curr = this;
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

    public boolean sanity(){
        if (nothasloop(this,null)==false){
            System.out.println("It has a loop");
            return false;
        }
        s.clear();
        if (check_height(this.getRoot())==false){
            System.out.println("The height property is violated");
            return false;
        }
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

    private static HashSet <AVLTree> s= new HashSet<AVLTree>();    
    private boolean nothasloop(AVLTree n, AVLTree prev){
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
    private boolean check_height(AVLTree n){
        if (n==null) return true;
        int l=-1;
        int r=-1;
        if (n.left!=null) l= n.left.height;
        if (n.right!=null) r = n.right.height;
        if (l>r+1 || r>l+1) return false;
        if (l>r && n.height!=l+1) return false;
        if (l<=r && n.height!=r+1) return false; 
        else return check_height(n.left) && check_height(n.right);
    }
    private boolean check_BST(AVLTree n){
        if (n== null) return true;
        else if (n.left !=null && n.compareNode(n.left)!=1){
            return false;
        }
        else if (n.right!=null && n.compareNode(n.right)!=-1){
            return false;
        }
        else return check_BST(n.left) && check_BST(n.right);
    }
    private boolean checkpointers(AVLTree n){
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

    private AVLTree getRoot(){
        // returns the actual root of the tree, not the sentinel one.
        AVLTree curr  =  this ;
        if (curr.parent ==null){
            return curr.right;
        }
        while (curr.parent.parent != null){
            curr = curr.parent;
        }
        return curr;
    }
    private int compareNode(AVLTree d){
        AVLTree b = this;
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
        AVLTree b = this;
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