package ProgrammingAssignment2;

import java.util.*;

// Paris (P2 - Part B)
// Daniel Gebara, #300401006

public class ParisMetro {

    // Generic edge representation 
    private static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
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
                parent[x] = find(parent[x]);
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

    // Helper to build an unordered key for a hub pair 
    private static String hubPairKey(int a, int b) {
        return (a < b) ? (a + "#" + b) : (b + "#" + a);
    }

    public static void main(String[] args) {
        new ParisMetro().run();
    }

    private void run() {
        Scanner in = new Scanner(System.in);

        if (!in.hasNextInt()) {
            return;
        }

        int n = in.nextInt(); // number of vertices
        int m = in.nextInt(); // number of edges

        int[] stationNumber = new int[n];
        String[] stationName = new String[n];
        Map<Integer, Integer> indexByStationNumber = new HashMap<>();

        // Read station metadata
        for (int i = 0; i < n; i++) {
            int num = in.nextInt();
            String name = in.nextLine().trim();
            stationNumber[i] = num;
            stationName[i] = name;
            indexByStationNumber.put(num, i);
        }

        // Consume "$"
        if (in.hasNext()) {
            String marker = in.next();
            // expecting "$"
        }

        List<Edge> allEdges = new ArrayList<>();

        // Read edges
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int w = in.nextInt();

            Integer ia = indexByStationNumber.get(a);
            Integer ib = indexByStationNumber.get(b);
            if (ia != null && ib != null) {
                allEdges.add(new Edge(ia, ib, w));
            }
        }

        in.close();

        // Build hub components using DSU over -1 walking edges
        DSU hubUF = new DSU(n);
        for (Edge e : allEdges) {
            if (e.w == -1) {
                hubUF.union(e.u, e.v);
            }
        }

        // Count vertices in each hub component
        Map<Integer, Integer> componentSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = hubUF.find(i);
            componentSize.put(root, componentSize.getOrDefault(root, 0) + 1);
        }

        int[] hubId = new int[n];
        Arrays.fill(hubId, -1);

        List<List<Integer>> hubMembers = new ArrayList<>();
        int totalHubVertices = 0;

        // Assign hub IDs to components with size > 1
        for (int i = 0; i < n; i++) {
            int root = hubUF.find(i);
            int size = componentSize.get(root);
            if (size > 1) {
                if (hubId[root] == -1) {
                    hubId[root] = hubMembers.size();
                    hubMembers.add(new ArrayList<>());
                }
                int hid = hubId[root];
                hubId[i] = hid;
                hubMembers.get(hid).add(i);
                totalHubVertices++;
            }
        }

        int hubCount = hubMembers.size();

        // Representative station name for each hub = smallest station number in that hub
        String[] hubNames = new String[hubCount];
        for (int h = 0; h < hubCount; h++) {
            List<Integer> members = hubMembers.get(h);
            members.sort(Comparator.comparingInt(i -> stationNumber[i]));
            hubNames[h] = members.isEmpty() ? "" : stationName[members.get(0)];
        }

        // Build adjacency list with positive edges only
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (Edge e : allEdges) {
            if (e.w > 0) {
                adj.get(e.u).add(new Edge(e.u, e.v, e.w));
                adj.get(e.v).add(new Edge(e.v, e.u, e.w));
            }
        }

        boolean[] isHubVertex = new boolean[n];
        for (int i = 0; i < n; i++) {
            isHubVertex[i] = (hubId[i] != -1);
        }

        // Identify connected components of non-hub vertices (using only positive edges)
        int[] component = new int[n];
        Arrays.fill(component, -1);
        int componentCount = 0;

        for (int i = 0; i < n; i++) {
            if (isHubVertex[i] || component[i] != -1) continue;

            Queue<Integer> q = new ArrayDeque<>();
            q.add(i);
            component[i] = componentCount;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    int v = e.v;
                    if (isHubVertex[v]) continue;
                    if (component[v] == -1) {
                        component[v] = componentCount;
                        q.add(v);
                    }
                }
            }
            componentCount++;
        }

        List<List<Integer>> compNodes = new ArrayList<>();
        for (int c = 0; c < componentCount; c++) {
            compNodes.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            if (component[i] != -1) {
                compNodes.get(component[i]).add(i);
            }
        }

        // Compute best segments between hub pairs
        final int INF = 1_000_000_000;

        // Map from unordered hub pair -> best travel time
        Map<String, Integer> bestSegment = new HashMap<>();

        // Direct hub-hub positive edges
        for (Edge e : allEdges) {
            if (e.w > 0 && isHubVertex[e.u] && isHubVertex[e.v]) {
                int ha = hubId[e.u];
                int hb = hubId[e.v];
                if (ha == hb) continue;

                String key = hubPairKey(ha, hb);
                int prev = bestSegment.getOrDefault(key, INF);
                if (e.w < prev) {
                    bestSegment.put(key, e.w);
                }
            }
        }

        // Paths via non-hub components
        class Boundary {
            int hub;
            int node;       // vertex inside component
            int edgeCost;   // cost of the edge from hub to this node

            Boundary(int hub, int node, int edgeCost) {
                this.hub = hub;
                this.node = node;
                this.edgeCost = edgeCost;
            }
        }

        for (int c = 0; c < componentCount; c++) {
            List<Integer> nodes = compNodes.get(c);
            if (nodes.isEmpty()) continue;

            List<Boundary> boundaries = new ArrayList<>();

            // Find all component nodes that connect directly to hub vertices
            for (int u : nodes) {
                for (Edge e : adj.get(u)) {
                    int v = e.v;
                    if (isHubVertex[v]) {
                        int h = hubId[v];
                        boundaries.add(new Boundary(h, u, e.w));
                    }
                }
            }

            if (boundaries.size() < 2) continue; // need at least two hubs to connect

            // For each boundary start, run Dijkstra inside this component
            for (int i = 0; i < boundaries.size(); i++) {
                Boundary start = boundaries.get(i);

                Map<Integer, Integer> dist = new HashMap<>();
                for (int u : nodes) {
                    dist.put(u, INF);
                }

                dist.put(start.node, 0);
                PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
                pq.add(new int[]{0, start.node});

                while (!pq.isEmpty()) {
                    int[] cur = pq.poll();
                    int d = cur[0];
                    int u = cur[1];

                    if (d != dist.get(u)) continue;

                    for (Edge e : adj.get(u)) {
                        int v = e.v;
                        if (component[v] != c) continue; // stay within the same component
                        int nd = d + e.w;
                        if (nd < dist.get(v)) {
                            dist.put(v, nd);
                            pq.add(new int[]{nd, v});
                        }
                    }
                }

                // Combine this start boundary with each other boundary to form hub-hub segments
                for (int j = i + 1; j < boundaries.size(); j++) {
                    Boundary end = boundaries.get(j);
                    if (start.hub == end.hub) continue;

                    int inner = dist.getOrDefault(end.node, INF);
                    if (inner >= INF) continue;

                    int total = start.edgeCost + inner + end.edgeCost;
                    String key = hubPairKey(start.hub, end.hub);
                    int prev = bestSegment.getOrDefault(key, INF);
                    if (total < prev) {
                        bestSegment.put(key, total);
                    }
                }
            }
        }

        int segmentCount = bestSegment.size();

        // Build compressed graph edges (hub graph)
        List<Edge> compressedEdges = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : bestSegment.entrySet()) {
            String key = entry.getKey();
            int w = entry.getValue();
            String[] parts = key.split("#");
            int ha = Integer.parseInt(parts[0]);
            int hb = Integer.parseInt(parts[1]);
            compressedEdges.add(new Edge(ha, hb, w));
        }

        // Kruskal's MST on the compressed hub graph
        compressedEdges.sort(Comparator.comparingInt(e -> e.w));
        DSU mstUF = new DSU(hubCount);

        long totalCost = 0L;
        int used = 0;
        List<Edge> chosen = new ArrayList<>();

        for (Edge e : compressedEdges) {
            if (mstUF.union(e.u, e.v)) {
                totalCost += e.w;
                used++;
                chosen.add(e);
                if (used == hubCount - 1) break;
            }
        }

        // Output 
        System.out.println("Paris Metro Graph has " + n + " vertices and " + m + " edges.\n");

        System.out.print("Hub Stations = [ ");
        for (int h = 0; h < hubCount; h++) {
            System.out.print(hubNames[h]);
            if (h + 1 < hubCount) {
                System.out.print(",  ");
            }
        }
        System.out.println(" ]\n");

        System.out.println("Number of Hub Stations = " + hubCount
                + " (total Hub Vertices = " + totalHubVertices + ")\n");
        System.out.println("Number of Possible Segments = " + segmentCount + "\n");

        if (hubCount == 0) {
            // No hubs → nothing to connect 
            System.out.println("Total Cost = Impossible");
            return;
        }

        if (used != hubCount - 1) {
            System.out.println("Total Cost = Impossible");
            return;
        }

        System.out.println("Total Cost = $" + totalCost);
        System.out.println("Segments to Buy:");
        for (int i = 0; i < chosen.size(); i++) {
            Edge e = chosen.get(i);
            System.out.println((i + 1) + "( " + hubNames[e.u] + " - " + hubNames[e.v] + " ) - $" + e.w);
        }
    }
}


