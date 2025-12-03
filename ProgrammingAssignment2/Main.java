package ProgrammingAssignment2;

import java.util.*;

// Main (P2 - Part A)
// Daniel Gebara, #300401006

public class Main {

    // Edge for the subway graph 
    private static class Edge implements Comparable<Edge> {
        int u;  // endpoint 1 (index)
        int v;  // endpoint 2 (index)
        int w;  // cost

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.w, other.w);
        }
    }

    // Disjoint Set Union (Union-Find) with path compression + union by rank 
    private static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    // Returns MST cost using Kruskal; -1 if graph is disconnected. 
    private static long kruskalMST(int n, List<Edge> edges) {
        Collections.sort(edges);
        DSU dsu = new DSU(n);

        long totalCost = 0L;
        int usedEdges = 0;

        for (Edge e : edges) {
            if (dsu.union(e.u, e.v)) {
                totalCost += e.w;
                usedEdges++;
                if (usedEdges == n - 1) {
                    break;
                }
            }
        }

        return (usedEdges == n - 1) ? totalCost : -1L;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextInt()) {
            int numStations = in.nextInt();
            int numConnections = in.nextInt();

            if (numStations == 0 && numConnections == 0) {
                break; // end of input
            }

            // Map station name -> index [0..numStations-1]
            Map<String, Integer> idByName = new HashMap<>();
            for (int i = 0; i < numStations; i++) {
                String name = in.next();
                idByName.put(name, i);
            }

            List<Edge> edges = new ArrayList<>();

            for (int i = 0; i < numConnections; i++) {
                String a = in.next();
                String b = in.next();
                int cost = in.nextInt();

                Integer ia = idByName.get(a);
                Integer ib = idByName.get(b);
                if (ia != null && ib != null) {
                    edges.add(new Edge(ia, ib, cost));
                }
            }

            // starting station (not needed for MST, but must be read)
            String start = in.next();
            if (!idByName.containsKey(start)) {
                System.out.println("Impossible");
                continue;
            }

            long result = kruskalMST(numStations, edges);
            if (result < 0) {
                System.out.println("Impossible");
            } else {
                System.out.println(result);
            }
        }

        in.close();
    }
}


