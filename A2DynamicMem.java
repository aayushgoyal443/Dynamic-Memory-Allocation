import jdk.nashorn.api.tree.NewTree;

// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {
      
    public A2DynamicMem() {  super(); }

    public A2DynamicMem(int size) { super(size); }

    public A2DynamicMem(int size, int dict_type) { super(size, dict_type); }

    // In A2, you need to test your implementation using BSTrees and AVLTrees. 
    // No changes should be required in the A1DynamicMem functions. 
    // They should work seamlessly with the newly supplied implementation of BSTrees and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test using BSTrees and AVLTrees. 
    //Your BST (and AVL tree) implementations should obey the property that keys in the left subtree <= root.key < keys in the right subtree. How is this total order between blocks defined? It shouldn't be a problem when using key=address since those are unique (this is an important invariant for the entire assignment123 module). When using key=size, use address to break ties i.e. if there are multiple blocks of the same size, order them by address. Now think outside the scope of the allocation problem and think of handling tiebreaking in blocks, in case key is neither of the two. 
    
    public int Allocate(int blockSize) {
        Dictionary temp = freeBlk.Find(blockSize, false);
        
        if (temp ==null || blockSize<=0) return -1;
        int out = temp.address;
        int tempsize = temp.size;
        if (temp.size ==blockSize){
            // Dictionary temp2  = new Dictionary(temp.address,temp.size,temp.key);  
            allocBlk.Insert(temp.address,temp.size,temp.address);
            freeBlk.Delete(temp);
        }
        else{
            // we are gonna perform a split in the freeblk 
            allocBlk.Insert(temp.address,blockSize,temp.address);
            freeBlk.Delete(temp);
            freeBlk.Insert(out + blockSize,tempsize - blockSize,tempsize - blockSize);
        }
        return out;
    }

    public int Free(int startAddr) {
        Dictionary temp = allocBlk.Find(startAddr, true);
        if (startAddr<0) return -1;
        if (temp == null) return -1;
        else{
            // Dictionary todelete = new Dictionary(temp.address,temp.size,temp.key);
            freeBlk.Insert(temp.address, temp.size, temp.size);
            allocBlk.Delete(temp);
            return 0;
        }
    }

    public void Defragment() {
        if (type ==1 ) return;
        Dictionary newtree;
        if (type ==2 ){
            newtree = new BSTree();
        }
        else{
            newtree = new AVLTree();
        }

        Dictionary curr = freeBlk.getFirst();
        while(curr!=null){
            newtree.Insert(curr.address, curr.size, curr.address);
            curr =curr.getNext();
        }
        Dictionary temp = newtree.getFirst();
        Dictionary temp2;
        if (temp == null) return;
        while(temp.getNext()!=null){
            temp2 = temp.getNext();
            if (temp2.address== temp.address+temp.size){
                int addr= temp.address;
                int sizecomb = temp.size+temp2.size;
                Dictionary temp3;
                if (type==2) temp3 = new BSTree(temp.address,temp.size,temp.size);
                else temp3 = new AVLTree(temp.address,temp.size,temp.size);
                Dictionary temp4;
                if (type==2) temp4 = new BSTree(temp2.address,temp2.size,temp2.size);
                else temp4 = new AVLTree(temp2.address,temp2.size,temp2.size);
                freeBlk.Delete(temp3);
                freeBlk.Delete(temp4);
                // newtree.Delete(temp);
                // newtree.Delete(temp2);
                freeBlk.Insert(addr, sizecomb, sizecomb);
                // temp = newtree.Insert(addr, sizecomb, addr);
                temp2.address=addr;
                temp2.size=sizecomb;
                temp2.key=addr;
            }            
            // else temp = temp2;
            temp = temp2;
            temp2= temp2.getNext();
        }
        return ;
    }
}