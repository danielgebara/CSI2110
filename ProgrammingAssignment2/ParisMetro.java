package ProgrammingAssignment2;

// Paris (P2 - Part B)
// Daniel Gebara, #300401006

import java.util.*;

public class ParisMetro {

    static class Edge implements Comparable<Edge> {
        int u, v, w;
        Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
        public int compareTo(Edge o) { return Integer.compare(this.w, o.w); }
    }

    static class DSU {
        int[] p, r;
        DSU(int n) { p = new int[n]; r = new int[n]; for (int i = 0; i < n; i++) p[i] = i; }
        int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
        boolean unite(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (r[ra] < r[rb]) p[ra] = rb;
            else if (r[rb] < r[ra]) p[rb] = ra;
            else { p[rb] = ra; r[ra]++; }
            return true;
        }
    }

    private static String pairKey(int a, int b) { return (a < b) ? a + "#" + b : b + "#" + a; }

    public static void main(String[] args) {
        solve();
    }

    private static void solve() {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) return;

        int n = in.nextInt();
        int m = in.nextInt();

        int[] num = new int[n];
        String[] name = new String[n];
        Map<Integer, Integer> idxByNum = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int vnum = in.nextInt();
            String vname = in.nextLine().trim();
            num[i] = vnum;
            name[i] = vname;
            idxByNum.put(vnum, i);
        }

        if (in.hasNext()) in.next(); // consume "$"

        List<Edge> all = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int w = in.nextInt();
            Integer ia = idxByNum.get(a), ib = idxByNum.get(b);
            if (ia != null && ib != null) all.add(new Edge(ia, ib, w));
        }

        // Hubs via -1 edges
        DSU hubDsu = new DSU(n);
        for (Edge e : all) if (e.w == -1) hubDsu.unite(e.u, e.v);

        // Component sizes
        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = hubDsu.find(i);
            compSize.put(r, compSize.getOrDefault(r, 0) + 1);
        }

        // Assign hub ids (size > 1)
        int[] hubId = new int[n];
        Arrays.fill(hubId, -1);
        List<List<Integer>> hubMembers = new ArrayList<>();
        int hubVertexCount = 0;
        for (int i = 0; i < n; i++) {
            int r = hubDsu.find(i);
            if (compSize.get(r) > 1) {
                if (hubId[r] == -1) {
                    hubId[r] = hubMembers.size();
                    hubMembers.add(new ArrayList<>());
                }
                int hid = hubId[r];
                hubId[i] = hid;
                hubMembers.get(hid).add(i);
                hubVertexCount++;
            }
        }
        int hubCount = hubMembers.size();

        // Rep name per hub (first by station number)
        String[] hubName = new String[hubCount];
        for (int h = 0; h < hubCount; h++) {
            List<Integer> mem = hubMembers.get(h);
            mem.sort(Comparator.comparingInt(i -> num[i]));
            hubName[h] = mem.isEmpty() ? "" : name[mem.get(0)];
        }

        // Positive edges adjacency
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : all) if (e.w > 0) {
            adj.get(e.u).add(new Edge(e.u, e.v, e.w));
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }

        boolean[] isHub = new boolean[n];
        for (int i = 0; i < n; i++) isHub[i] = hubId[i] != -1;

        // non-hub components
        int[] comp = new int[n];
        Arrays.fill(comp, -1);
        int compCnt = 0;
        for (int i = 0; i < n; i++) {
            if (isHub[i] || comp[i] != -1) continue;
            Queue<Integer> q = new ArrayDeque<>();
            q.add(i); comp[i] = compCnt;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    int v = e.v;
                    if (isHub[v]) continue;
                    if (comp[v] == -1) { comp[v] = compCnt; q.add(v); }
                }
            }
            compCnt++;
        }

        List<List<Integer>> compNodes = new ArrayList<>();
        for (int c = 0; c < compCnt; c++) compNodes.add(new ArrayList<>());
        for (int i = 0; i < n; i++) if (comp[i] != -1) compNodes.get(comp[i]).add(i);

        Map<String, Integer> best = new HashMap<>(); // min per hub pair

        // direct hub-hub edges
        for (Edge e : all) {
            if (e.w > 0 && isHub[e.u] && isHub[e.v]) {
                int ha = hubId[e.u], hb = hubId[e.v];
                if (ha != hb) {
                    String k = pairKey(ha, hb);
                    int prev = best.getOrDefault(k, Integer.MAX_VALUE);
                    if (e.w < prev) best.put(k, e.w);
                }
            }
        }

        // process each non-hub component
        for (int c = 0; c < compCnt; c++) {
            List<Integer> nodes = compNodes.get(c);
            if (nodes.isEmpty()) continue;

            Map<Integer, List<Edge>> cadj = new HashMap<>();
            for (int u : nodes) cadj.put(u, new ArrayList<>());
            for (int u : nodes) {
                for (Edge e : adj.get(u)) {
                    if (comp[e.v] == c) cadj.get(u).add(new Edge(u, e.v, e.w));
                }
            }

            List<int[]> boundary = new ArrayList<>(); // {hubId, compNode, entryW}
            for (int u : nodes) {
                for (Edge e : adj.get(u)) {
                    int v = e.v;
                    if (isHub[v]) boundary.add(new int[]{hubId[v], u, e.w});
                }
            }
            if (boundary.size() < 2) continue;

            final int INF = 1_000_000_000;
            for (int i = 0; i < boundary.size(); i++) {
                int hubA = boundary.get(i)[0];
                int src = boundary.get(i)[1];
                int entryA = boundary.get(i)[2];

                Map<Integer, Integer> dist = new HashMap<>();
                for (int u : nodes) dist.put(u, INF);
                PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
                dist.put(src, 0);
                pq.add(new int[]{0, src});

                while (!pq.isEmpty()) {
                    int[] cur = pq.poll();
                    int d = cur[0], u = cur[1];
                    if (d != dist.get(u)) continue;
                    for (Edge e : cadj.get(u)) {
                        int v = e.v;
                        int nd = d + e.w;
                        if (nd < dist.get(v)) {
                            dist.put(v, nd);
                            pq.add(new int[]{nd, v});
                        }
                    }
                }

                for (int j = i + 1; j < boundary.size(); j++) {
                    int hubB = boundary.get(j)[0];
                    int tgt = boundary.get(j)[1];
                    int entryB = boundary.get(j)[2];
                    int inside = dist.getOrDefault(tgt, INF);
                    if (inside >= INF) continue;
                    int total = entryA + inside + entryB;
                    if (hubA != hubB) {
                        String k = pairKey(hubA, hubB);
                        int prev = best.getOrDefault(k, Integer.MAX_VALUE);
                        if (total < prev) best.put(k, total);
                    }
                }
            }
        }

        // compressed edges for MST; segment count = unique hub pairs
        List<Edge> compEdges = new ArrayList<>();
        for (Map.Entry<String, Integer> en : best.entrySet()) {
            String[] p = en.getKey().split("#");
            int a = Integer.parseInt(p[0]);
            int b = Integer.parseInt(p[1]);
            compEdges.add(new Edge(a, b, en.getValue()));
        }
        int segmentCount = best.size();

        // Kruskal MST
        Collections.sort(compEdges);
        DSU mst = new DSU(hubCount);
        long total = 0;
        int picked = 0;
        List<Edge> mstEdges = new ArrayList<>();
        for (Edge ce : compEdges) {
            if (mst.unite(ce.u, ce.v)) {
                total += ce.w;
                picked++;
                mstEdges.add(ce);
                if (picked == hubCount - 1) break;
            }
        }

        // Output
        System.out.println("Paris Metro Graph has " + n + " vertices and " + m + " edges.\n");

        System.out.print("Hub Stations = [ ");
        for (int h = 0; h < hubCount; h++) {
            System.out.print(hubName[h]);
            if (h + 1 < hubCount) System.out.print(",  ");
        }
        System.out.println(" ]\n");

        System.out.println("Number of Hub Stations = " + hubCount + " (total Hub Vertices = " + hubVertexCount + ")\n");
        System.out.println("Number of Possible Segments = " + segmentCount + "\n");

        if (picked != hubCount - 1) {
            System.out.println("Total Cost = Impossible");
            return;
        }

        System.out.println("Total Cost = $" + total);
        System.out.println("Segments to Buy:");
        for (int i = 0; i < mstEdges.size(); i++) {
            Edge e = mstEdges.get(i);
            System.out.println((i + 1) + "( " + hubName[e.u] + " - " + hubName[e.v] + " ) - $" + e.w);
        }
    }
}
