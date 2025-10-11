package ProgrammingAssignment1;

//Part1

public class Partition<E> {
    private Node<E>[] clusters;
    private int[] sizes;
    private int numberOfClusters;

    public Partition(int capacity) {
        clusters = new Node[capacity];
        sizes = new int[capacity];
        numberOfClusters = 0;
    }

    // Create a new single cluster
    public int makeCluster(E element) {
        Node<E> newNode = new Node<>(element);
        clusters[numberOfClusters] = newNode;
        sizes[numberOfClusters] = 1;
        numberOfClusters++;
        return numberOfClusters - 1;
    }

    // Union of two clusters
    public void union(int p, int q) {
        if (p == q) return;
        if (sizes[p] < sizes[q]) {
            Node<E> temp = clusters[p];
            clusters[p] = clusters[q];
            clusters[q] = temp;

            int tempSize = sizes[p];
            sizes[p] = sizes[q];
            sizes[q] = tempSize;
        }
        Node<E> temp = clusters[p];
        while (temp != null) {
            temp.setNext(clusters[q]);
            temp = temp.getNext();
        }
        sizes[p] += sizes[q];
        clusters[q] = null;
    }

    // Find the leader of the cluster with element p
    public int find(int p) {
        return p;
    }

    // Get the element at position p
    public E element(int p) {
        return clusters[p].getElement();
    }

    // Get the number of clusters
    public int numberOfClusters() {
        return numberOfClusters;
    }

    // Get the size of the cluster containing p
    public int clusterSize(int p) {
        return sizes[p];
    }

    // Get all positions in the same cluster as position p
    public Node<E>[] clusterPositions(int p) {
        Node<E>[] result = new Node[sizes[p]];
        int index = 0;
        Node<E> temp = clusters[p];
        while (temp != null) {
            result[index++] = temp;
            temp = temp.getNext();
        }
        return result;
    }

    // Get a list of cluster sizes in decreasing order
    public int[] clusterSizes() {
        int[] sortedSizes = sizes.clone();
        java.util.Arrays.sort(sortedSizes);
        return sortedSizes;
    }
}

