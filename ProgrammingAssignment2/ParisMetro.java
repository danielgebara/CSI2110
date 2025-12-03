package ProgrammingAssignment2;

import java.util.*;

// Paris (P2 - Part B)
// Daniel Gebara, #300401006

public class ParisMetro {

    private static class Edge {
        int from;
        int to;
        int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
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
            } else if (rank[rb] < rank[ra]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    private static String pairKey(int a, int b) {
        return (a < b) ? a + "#" + b : b + "#" + a;
    }

    public static void main(String[] args) {
        ParisMetro app = new ParisMetro();
        app.run();
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) {
            return;
        }

        int n = in.nextInt(); // number of vertices
        int m = in.nextInt(); // number of edges

        int[] stationNum = new int[n];
        String[] stationName = new String[n];
        Map<Integer, Integer> indexByNumber = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int vnum = in.nextInt();
            String vname = in.nextLine().trim();
            stationNum[i] = vnum;
            stationName[i] = vname;
            indexByNumber.put(vnum, i);
        }

        // Consume "$"
        if (in.hasNext()) {
            in.next();
        }

        List<Edge> allEdges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int w = in.nextInt();
            Integer ia = indexByNumber.get(a);
            Integer ib = indexByNumber.get(b);
            if (ia != null && ib != null) {
                allEdges.add(new Edge(ia, ib, w));
            }
        }

        // Identify hub groups using -1 edges
        DSU hubUF = new DSU(n);
        for (Edge e : allEdges) {
            if (e.weight == -1) {
                hubUF.union(e.from, e.to);
            }
        }

        // Count size of each DSU component
        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = hubUF.find(i);
            compSize.put(r, compSize.getOrDefault(r, 0) + 1);
        }

        // Assign hub IDs to components with size > 1
        int[] hubId = new int[n];
        Arrays.fill(hubId, -1);

        List<List<Integer>> hubMembers = new ArrayList<>();
        int hubVertexCount = 0;

        for (int i = 0; i < n; i++) {
            int root = hubUF.find(i);
            int size = compSize.get(root);
            if (size > 1) {
                if (hubId[root] == -1) {
                    hubId[root] = hubMembers.size();
                    hubMembers.add(new ArrayList<>());
                }
                int hid = hubId[root];
                hubId[i] = hid;
                hubMembers.get(hid).add(i);
                hubVertexCount++;
            }
        }

        int hubCount = hubMembers.size();

        String[] hubName = new String[hubCount];
        for (int h = 0; h < hubCount; h++) {
            List<Integer> members = hubMembers.get(h);
            members.sort(Comparator.comparingInt(i -> stationNum[i]));
            hubName[h] = members.isEmpty() ? "" : stationName[members.get(0)];
        }

        // Build adjacency of all vertices with positive-weight edges
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (Edge e : allEdges) {
            if (e.weight > 0) {
                adj.get(e.from).add(new Edge(e.from, e.to, e.weight));
                adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
            }
        }

        boolean[] isHubVertex = new boolean[n];
        for (int i = 0; i < n; i++) {
            isHubVertex[i] = (hubId[i] != -1);
        }

        // Find connected components of NON-hub vertices (using positive edges only)
        int[] comp = new int[n];
        Arrays.fill(comp, -1);
        int compCount = 0;

        for (int i = 0; i < n; i++) {
            if (isHubVertex[i] || comp[i] != -1) continue;

            Queue<Integer> q = new ArrayDeque<>();
            q.add(i);
            comp[i] = compCount;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    int v = e.to;
                    if (isHubVertex[v]) continue;        // do not traverse into hubs
                    if (comp[v] == -1) {
                        comp[v] = compCount;
                        q.add(v);
                    }
                }
            }
            compCount++;
        }

        List<List<Integer>> compNodes = new ArrayList<>();
        for (int c = 0; c < compCount; c++) {
            compNodes.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            if (comp[i] != -1) {
                compNodes.get(comp[i]).add(i);
            }
        }

        // Map of best cost per unordered hub pair
        Map<String, Integer> bestSegment = new HashMap<>();

        // Direct hub-hub positive edges
        for (Edge e : allEdges) {
            if (e.weight > 0 && isHubVertex[e.from] && isHubVertex[e.to]) {
                int ha = hubId[e.from];
                int hb = hubId[e.to];
                if (ha == hb) continue;
                String key = pairKey(ha, hb);
                int prev = bestSegment.getOrDefault(key, Integer.MAX_VALUE);
                if (e.weight < prev) {
                    bestSegment.put(key, e.weight);
                }
            }
        }

        // For each non-hub component, compute best connection between hubs via that component
        final int INF = 1_000_000_000;

        for (int c = 0; c < compCount; c++) {
            List<Integer> nodes = compNodes.get(c);
            if (nodes.isEmpty()) continue;

            // Boundary entries: (hubId, componentNode, entryEdgeWeight)
            class Boundary {
                int hub;
                int node;
                int entryWeight;

                Boundary(int hub, int node, int entryWeight) {
                    this.hub = hub;
                    this.node = node;
                    this.entryWeight = entryWeight;
                }
            }

            List<Boundary> boundaries = new ArrayList<>();

            // Identify boundary edges: comp node -> hub vertex via positive edge
            for (int u : nodes) {
                for (Edge e : adj.get(u)) {
                    int v = e.to;
                    if (isHubVertex[v]) {
                        int h = hubId[v];
                        boundaries.add(new Boundary(h, u, e.weight));
                    }
                }
            }

            if (boundaries.size() < 2) continue;

            // Dijkstra inside this component for each boundary
            for (int i = 0; i < boundaries.size(); i++) {
                Boundary start = boundaries.get(i);

                // Distance only over nodes in this component
                Map<Integer, Integer> dist = new HashMap<>();
                for (int u : nodes) dist.put(u, INF);
                dist.put(start.node, 0);

                PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
                pq.add(new int[]{0, start.node});

                while (!pq.isEmpty()) {
                    int[] cur = pq.poll();
                    int d = cur[0];
                    int u = cur[1];
                    if (d != dist.get(u)) continue;

                    for (Edge e : adj.get(u)) {
                        int v = e.to;
                        if (comp[v] != c) continue; // stay inside this component
                        int nd = d + e.weight;
                        if (nd < dist.get(v)) {
                            dist.put(v, nd);
                            pq.add(new int[]{nd, v});
                        }
                    }
                }

                // Combine entry/exit via this component to connect two hubs
                for (int j = i + 1; j < boundaries.size(); j++) {
                    Boundary end = boundaries.get(j);
                    if (start.hub == end.hub) continue;

                    int inner = dist.getOrDefault(end.node, INF);
                    if (inner >= INF) continue;

                    int total = start.entryWeight + inner + end.entryWeight;
                    String key = pairKey(start.hub, end.hub);
                    int prev = bestSegment.getOrDefault(key, Integer.MAX_VALUE);
                    if (total < prev) {
                        bestSegment.put(key, total);
                    }
                }
            }
        }

        // Build compressed hub graph edges
        List<Edge> compressedEdges = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : bestSegment.entrySet()) {
            String[] parts = entry.getKey().split("#");
            int ha = Integer.parseInt(parts[0]);
            int hb = Integer.parseInt(parts[1]);
            int w = entry.getValue();
            compressedEdges.add(new Edge(ha, hb, w));
        }
        int segmentCount = bestSegment.size();

        // Kruskal MST on hubs
        compressedEdges.sort(Comparator.comparingInt(e -> e.weight));
        DSU mstUF = new DSU(hubCount);

        long totalCost = 0;
        int chosen = 0;
        List<Edge> chosenEdges = new ArrayList<>();

        for (Edge e : compressedEdges) {
            if (mstUF.union(e.from, e.to)) {
                totalCost += e.weight;
                chosen++;
                chosenEdges.add(e);
                if (chosen == hubCount - 1) break;
            }
        }

        System.out.println("Paris Metro Graph has " + n + " vertices and " + m + " edges.\n");

        System.out.print("Hub Stations = [ ");
        for (int h = 0; h < hubCount; h++) {
            System.out.print(hubName[h]);
            if (h + 1 < hubCount) {
                System.out.print(",  ");
            }
        }
        System.out.println(" ]\n");

        System.out.println("Number of Hub Stations = " + hubCount
                + " (total Hub Vertices = " + hubVertexCount + ")\n");
        System.out.println("Number of Possible Segments = " + segmentCount + "\n");

        if (chosen != hubCount - 1) {
            System.out.println("Total Cost = Impossible");
            return;
        }

        System.out.println("Total Cost = $" + totalCost);
        System.out.println("Segments to Buy:");
        for (int i = 0; i < chosenEdges.size(); i++) {
            Edge e = chosenEdges.get(i);
            System.out.println((i + 1) + "( " + hubName[e.from] + " - " + hubName[e.to] + " ) - $" + e.weight);
        }
    }
}

