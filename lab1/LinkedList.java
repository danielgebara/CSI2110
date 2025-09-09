package lab1;

/** 
 * Builds a singly linked list of size 5 and prints it to the console.
 * 
 * @author Jochen Lang
 */

class LinkedList {
    private DNode head;
    private DNode tail;

    LinkedList( int sz ) {
	    if ( sz <= 0 ) {
	        head = null;
            tail = null;
            return;
	    }
	    // start with list of size 1
	    head = new DNode( "0", null, null ); 
	    tail = head; // temp node for loop
        // add further nodes
	    for ( int i=1; i<sz; ++i ) {
		    // create node and attach it to the list
		    DNode node2Add = new DNode( String.valueOf(i), null, tail );
		    tail.setNext(node2Add);   // add first node
		    tail=node2Add;
	    }
	}

    
    /**
     * Print all the elements of the list assuming that they are Strings
     */
    public void print() {
	/* Print the list */
	DNode current = head; // point to the first node
	while (current != null) {
	    System.out.print((String)current.getElement() + " ");	
	    current = current.getNext(); // move to the next
	}
	System.out.println();	
    }

    public void deleteFirst() {
	    if ( head == null ) return; // no node
	    if (head == tail) {
            head = null;
            tail = null;
            return;
        }   
        head = head.getNext();
        head.setPrev(null);
	}

    public void deleteLast() {
	    if ( tail == null ) return; // no node
	    if ( tail == head ) { // only 1 node
            head = null;
            tail = null;
            return;
        }    
        tail = tail.getPrev();
	    tail.setNext( null );
    }

    // create and display a linked list
    public static void main(String [] args){
	/* Create the list */
	LinkList llist = new LinkList( 5 );
	/* Print the list */
	llist.print();
	/* delete first and print */
	llist.deleteFirst();
	llist.print();
	/* delete last and print 5 times */
	for ( int i=0; i< 5; ++i ) {
	    llist.deleteLast();
	    llist.print();
	}
    }
}
