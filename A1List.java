// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List  next; // Next Node
    private A1List prev;  // Previous Node 

    public A1List(int address, int size, int key) { 
        super(address, size, key);
    }
    
    public A1List(){
        super(-1,-1,-1);    
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1,-1,-1); // Intiate the tail sentinel
        
        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key)
    {
        A1List newnode = new A1List(address, size, key);
        this.next.prev = newnode;
        newnode.next = this.next;
        this.next = newnode;
        newnode.prev =this;
        
        return newnode;
    }

    public boolean Delete(Dictionary d) 
    {
        A1List curr= this.getFirst();
        int flag =0;
        while(curr!=null){
            if (curr.key==d.key && curr.address == d.address && curr.size== d.size){
                flag=1;
                //Do the deleting node stuff here
                // In this i am considering there can bee multiple nodes with same size and adress, that is just implementing a simple DLL, without considering the fact that such a situation will never happen in the actual problem
                A1List temp = curr.next;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                curr.next=null;
                curr.prev =null;
                curr= temp;
            }
            else{
                curr = curr.next;
            }
        }
        if (flag==1) return true;
        else return false;
    }

    public A1List Find(int k, boolean exact)
    { 
        A1List curr = this.getFirst();
        if (exact==true){           
            while(curr!=null){
                if (curr.key == k) return curr;
                else curr = curr.next;
            }
        }
        else{
            while(curr!=null){
                if (curr.key >= k) return curr;
                else curr = curr.next;
            }
        }
        return null;
    }

    public A1List getFirst()
    {
        A1List curr = this;
        while(curr.prev!=null) curr= curr.prev;
        //Now the curr comes to the head sentinal
        //But we need to return the first most element that os the element just after the head sentinel
        if (curr.next.next!=null) return curr.next;
        else return null; //Assuming we don't have to return the tail sentinal if the DLL is emtpy
    }
    
    public A1List getNext() 
    {
        if (this.next.next==null) return null; // because this would mean that this.next is the  sentinel node 
        else return this.next; // If not sentinel then simply return the next node
    }

    public boolean sanity()
    {
        // write the invariants
        
        // Checking whether our list has any loops using Floyd's Cycle Finding Algorithm.
        A1List p1 = this.getFirst();
        A1List p2 = this.getFirst();
        while(true){
            p1 = p1.getNext();
            p2 = p2.getNext();
            if (p2 == null) break;
            p2 = p2.getNext();
            if (p2==null)  break;
            if (p1 ==p2) return false;
        }

        // Checkign whether the sentinel nodes are correct or not.
        A1List curr = this.getFirst();
        if (curr ==null){
            if (this.next ==null){
                if (this.prev.prev != null) return false;
            }
            else if (this.prev ==null){
                if (this.next.next !=null) return false;
            }
        }
        else {
            curr = curr.prev;
            // curr shouldd become the head sentinel;
            if (curr.prev !=null ) return false;
            while (curr.getNext() !=null ){
                curr = curr.next;
            }
            // Now curr becomes the node before tail sentinel
            if (curr.next.next !=null) return false;
        }

        // Checking whether the prev of the next of a node points back to that node oe not.
        curr  = this.getFirst();
        while(curr!=null){
            if (curr.next.prev != curr) return false;
            curr = curr.getNext();
        }


        return true;
    }

}


