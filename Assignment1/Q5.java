package Assignment1;

public class Q5 {

    public static class TreeNode {
        TreeNode left, right, parent;
        // optional payload:
        // char key;
        // TreeNode(char k) { key = k; }
    }

    // Returns the inorder successor of p, or null if none exists.
    public static TreeNode inorderNext(TreeNode p) {
        if (p == null) return null;

        // Case 1: right subtree exists -> leftmost node of right subtree
        if (p.right != null) {
            TreeNode q = p.right;
            while (q.left != null) {
                q = q.left;
            }
            return q;
        }

        // Case 2: no right subtree -> go up until we come from a left child
        TreeNode q = p;
        TreeNode parent = q.parent;
        while (parent != null && q == parent.right) {
            q = parent;
            parent = parent.parent;
        }
        return parent; // may be null
    }
}
