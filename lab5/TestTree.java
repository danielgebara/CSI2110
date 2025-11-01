package lab5;


import java.util.*;

public class TestTree {
    public static void main(String[] args) {
        // Build a tree from a set of integers so ordering is deterministic.
        Integer[] data = { 40, 20, 10, 30, 60, 50, 70, 65, 80 };
        LinkedBinarySearchTree<Integer> bst = new LinkedBinarySearchTree<>(Arrays.asList(data));

        // --- Recursive traversals ---
        List<Integer> pre  = bst.preorderRecursive();
        List<Integer> in   = bst.inorderRecursive();
        List<Integer> post = bst.postorderRecursive();

        System.out.println("BST (level-order): " + bst.toStringLevelOrder());
        System.out.println("Preorder  (recursive): " + pre);
        System.out.println("Inorder   (recursive): " + in);
        System.out.println("Postorder (recursive): " + post);

        // --- Iterators ---
        System.out.print("Preorder  (iterator): [");
        Iterator<Integer> itPre = bst.preorderIterator();
        boolean first = true;
        while (itPre.hasNext()) {
            if (!first) System.out.print(", ");
            first = false;
            System.out.print(itPre.next());
        }
        System.out.println("]");

        System.out.print("Inorder   (iterator): [");
        Iterator<Integer> itIn = bst.inorderIterator();
        first = true;
        while (itIn.hasNext()) {
            if (!first) System.out.print(", ");
            first = false;
            System.out.print(itIn.next());
        }
        System.out.println("]");

        // Simple assertions to verify correctness
        String preExpect  = "[40, 20, 10, 30, 60, 50, 70, 65, 80]";
        String inExpect   = "[10, 20, 30, 40, 50, 60, 65, 70, 80]";
        String postExpect = "[10, 30, 20, 50, 65, 80, 70, 60, 40]";

        check("Preorder recursive", pre.toString(),  preExpect);
        check("Inorder  recursive", in.toString(),   inExpect);
        check("Postorder recursive", post.toString(), postExpect);

        // Iterator sequences should match the recursive ones for the same order
        check("Preorder iterator", listFromIterator(bst.preorderIterator()).toString(), preExpect);
        check("Inorder  iterator", listFromIterator(bst.inorderIterator()).toString(),  inExpect);
    }

    private static <T> List<T> listFromIterator(Iterator<T> it) {
        List<T> out = new ArrayList<>();
        while (it.hasNext()) out.add(it.next());
        return out;
    }

    private static void check(String name, String got, String expected) {
        if (!Objects.equals(got, expected)) {
            System.out.println("✗ " + name + " mismatch.\n  got:      " + got + "\n  expected: " + expected);
        } else {
            System.out.println("✓ " + name + " OK");
        }
    }
}
