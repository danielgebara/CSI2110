package ProgrammingAssignment2;

import java.util.*;

// Main (P2 - Part A)
// Daniel Gebara, #300401006

public class Main {

    private static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private static long primMST(List<List<Edge>> graph, int n) {
        if (n == 0) return 0;

        boolean[] inTree = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        long totalCost = 0;
        int visited = 0;

        // Start from vertex 0
        inTree[0] = true;
        visited++;
        for (Edge e : graph.get(0)) {
            pq.add(new int[]{e.cost, e.to});
        }

        while (!pq.isEmpty() && visited < n) {
            int[] cur = pq.poll();
            int w = cur[0];
            int v = cur[1];

            if (inTree[v]) continue;

            inTree[v] = true;
            visited++;
            totalCost += w;

            for (Edge e : graph.get(v)) {
                if (!inTree[e.to]) {
                    pq.add(new int[]{e.cost, e.to});
                }
            }
        }

        // If we didn't reach all vertices, MST is impossible
        return (visited == n) ? totalCost : -1;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextInt()) {
            int vertexCount = in.nextInt();
            int edgeCount = in.nextInt();
            if (vertexCount == 0 && edgeCount == 0) break;

            Map<String, Integer> nameToIndex = new HashMap<>();
            for (int i = 0; i < vertexCount; i++) {
                String city = in.next();
                nameToIndex.put(city, i);
            }

            List<List<Edge>> graph = new ArrayList<>();
            for (int i = 0; i < vertexCount; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < edgeCount; i++) {
                String a = in.next();
                String b = in.next();
                int cost = in.nextInt();

                Integer ia = nameToIndex.get(a);
                Integer ib = nameToIndex.get(b);
                if (ia != null && ib != null) {
                    graph.get(ia).add(new Edge(ib, cost));
                    graph.get(ib).add(new Edge(ia, cost));
                }
            }

            String home = in.next();
            if (!nameToIndex.containsKey(home)) {
                System.out.println("Impossible");
                continue;
            }

            long answer = primMST(graph, vertexCount);
            if (answer < 0) {
                System.out.println("Impossible");
            } else {
                System.out.println(answer);
            }
        }

        in.close();
    }
}

