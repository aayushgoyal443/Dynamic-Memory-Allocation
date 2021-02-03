// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {
      
    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).

    public int Allocate(int blockSize) {
        Dictionary temp = freeBlk.Find(blockSize, false);
        if (temp ==null || blockSize<=0) return -1;
        else if (temp.size ==blockSize){
            // Dictionary temp2  = new Dictionary(temp.address,temp.size,temp.key);
            freeBlk.Delete(temp);
            allocBlk.Insert(temp.address,temp.size,temp.address);
            // Something is wrong with the insert function
            
        }
        else{
            // we are gonna perform a split in the freeblk 
            allocBlk.Insert(temp.address,blockSize,temp.address);
            //
            freeBlk.Delete(temp);
            freeBlk.Insert(temp.address + blockSize,temp.size - blockSize,temp.size - blockSize);
        }
        return temp.address;
    } 
    
    public int Free(int startAddr) {
        Dictionary temp = allocBlk.Find(startAddr, true);
        if (temp == null) return -1;
        else{
            // Dictionary todelete = new Dictionary(temp.address,temp.size,temp.key);
            allocBlk.Delete(temp);
            freeBlk.Insert(temp.address, temp.size, temp.size);
            // 
            return 0;
        }
    }
}