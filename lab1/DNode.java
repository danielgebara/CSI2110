package lab1;

/**
 * A simple node class for a singly-linked list.  Each node has a
 * reference to a stored element and a next node.
 * This class is based on the <code>DNode</code> class by Roberto Tamassia.
 *
 * @author Jochen Lang
 */

 public class DNode {
    private Object element;
    private Node next;
    private Node prev;
    DNode() { this(null, null,null); }
      DNode(Object e, Node n, Node p) {
      element = e;
      next = n;
      prev = p;
    }
    public void setElement(Object newElem) { element = newElem; }
    public void setNext(Node newNext) { next = newNext; }
    public void setPrev(Node newPrev) { prev = newPrev; }
    public Object getElement() { return element; }
    public Node getNext() { return next; }
    public Node getPrev() { return prev; }
  }
