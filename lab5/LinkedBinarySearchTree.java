package lab5;


import java.util.*;

/**
 * LinkedBinarySearchTree<E>
 * 
 * Implements a simple BST with parent pointers.
 * Includes recursive traversals and two iterators:
 *  - PreorderIterator
 *  - InorderIterator
 * 
 * The traversals add elements to a provided List<E> to keep the API simple.
 * Public helpers return a List<E> for convenience.
 */
public class LinkedBinarySearchTree<E extends Comparable<E>> implements Iterable<E> {

    // ----- Node -----
    public static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        Node(E e, Node<E> p) { this.element = e; this.parent = p; }
        boolean hasLeft()  { return left != null; }
        boolean hasRight() { return right != null; }
    }

    // ----- Fields -----
    private Node<E> root;
    private int size;

    // ----- Constructors -----
    public LinkedBinarySearchTree() { this.root = null; this.size = 0; }
    public LinkedBinarySearchTree(Collection<E> items) {
        this();
        for (E e : items) insert(e);
    }

    // ----- Basic BST ops -----
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public Node<E> root() { return root; }

    public void insert(E e) {
        if (root == null) {
            root = new Node<>(e, null);
            size = 1;
            return;
        }
        Node<E> p = null;
        Node<E> x = root;
        while (x != null) {
            p = x;
            int cmp = e.compareTo(x.element);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                // duplicates: insert to the right (stable choice)
                x = x.right;
            }
        }
        Node<E> n = new Node<>(e, p);
        if (e.compareTo(p.element) < 0) p.left = n; else p.right = n;
        size++;
    }

    // ----- Traversals (recursive) -----
    // Each has a private recursive impl that fills 'out'

    public List<E> preorderRecursive() {
        List<E> out = new ArrayList<>();
        preorderRecursive(root, out);
        return out;
    }
    public List<E> inorderRecursive() {
        List<E> out = new ArrayList<>();
        inorderRecursive(root, out);
        return out;
    }
    public List<E> postorderRecursive() {
        List<E> out = new ArrayList<>();
        postorderRecursive(root, out);
        return out;
    }

    // COMPLETE per lab statement: void preorderRecursive(Node), etc.
    private void preorderRecursive(Node<E> v, List<E> out) {
        if (v == null) return;
        out.add(v.element);
        preorderRecursive(v.left, out);
        preorderRecursive(v.right, out);
    }
    private void inorderRecursive(Node<E> v, List<E> out) {
        if (v == null) return;
        inorderRecursive(v.left, out);
        out.add(v.element);
        inorderRecursive(v.right, out);
    }
    private void postorderRecursive(Node<E> v, List<E> out) {
        if (v == null) return;
        postorderRecursive(v.left, out);
        postorderRecursive(v.right, out);
        out.add(v.element);
    }

    // ----- Iterators -----
    // We expose named factory methods, but also support 'iterator()' as inorder by default.

    public Iterator<E> preorderIterator() { return new PreorderIterator(); }
    public Iterator<E> inorderIterator()  { return new InorderIterator(); }
    @Override public Iterator<E> iterator() { return inorderIterator(); }

    // Preorder iterator using pseudocode from the assignment for preorderNext(v).
    private class PreorderIterator implements Iterator<E> {
        private Node<E> next;
        PreorderIterator() {
            next = root;
        }

        @Override public boolean hasNext() { return next != null; }

        @Override public E next() {
            if (next == null) throw new NoSuchElementException();
            Node<E> current = next;
            advance();
            return current.element;
        }

        private void advance() {
            next = preorderNext(next);
        }

        private Node<E> preorderNext(Node<E> v) {
            if (v == null) return null;
            if (v.left != null)  return v.left;
            if (v.right != null) return v.right;
            // climb up until we can go to an unexplored right child
            Node<E> parent = v.parent;
            Node<E> child = v;
            while (parent != null && (parent.right == child || parent.right == null)) {
                child = parent;
                parent = parent.parent;
            }
            if (parent == null) return null;
            return parent.right;
        }
    }

    // Inorder iterator using pseudocode from the assignment for inorderNext(v).
    private class InorderIterator implements Iterator<E> {
        private Node<E> next;
        InorderIterator() {
            next = leftmost(root);
        }

        @Override public boolean hasNext() { return next != null; }

        @Override public E next() {
            if (next == null) throw new NoSuchElementException();
            Node<E> current = next;
            advance();
            return current.element;
        }

        private void advance() {
            next = inorderNext(next);
        }

        private Node<E> leftmost(Node<E> v) {
            if (v == null) return null;
            while (v.left != null) v = v.left;
            return v;
        }

        private Node<E> inorderNext(Node<E> v) {
            if (v == null) return null;
            if (v.right != null) {
                Node<E> n = v.right;
                while (n.left != null) n = n.left;
                return n;
            } else {
                Node<E> n = v;
                while (true) {
                    if (n.parent == null) return null;
                    if (n.parent.left == n) return n.parent;
                    n = n.parent;
                }
            }
        }
    }

    // ----- Pretty utilities for testing -----
    public String toStringLevelOrder() {
        if (root == null) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Queue<Node<E>> q = new ArrayDeque<>();
        q.add(root);
        boolean first = true;
        while (!q.isEmpty()) {
            Node<E> n = q.remove();
            if (!first) sb.append(", ");
            first = false;
            sb.append(n.element);
            if (n.left != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        sb.append("]");
        return sb.toString();
    }
}