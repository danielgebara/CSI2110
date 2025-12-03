package ProgrammingAssignment2;

// Expensive Subway (P2 - Part A)
// Daniel Gebara, #300401006

import java.util.*;

class Main {

    static class Edge implements Comparable<Edge> {
        int u, v, w;
        Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
        public int compareTo(Edge o) { return Integer.compare(this.w, o.w); }
    }

    static class DSU {
        int[] p, r;
        DSU(int n) {
            p = new int[n];
            r = new int[n];
            for (int i = 0; i < n; i++) p[i] = i;
        }
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

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int nv = in.nextInt();
            int ne = in.nextInt();
            if (nv == 0 && ne == 0) break;

            Map<String, Integer> id = new HashMap<>();
            for (int i = 0; i < nv; i++) id.put(in.next(), i);

            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < ne; i++) {
                String a = in.next(), b = in.next();
                int w = in.nextInt();
                Integer ia = id.get(a), ib = id.get(b);
                if (ia != null && ib != null) edges.add(new Edge(ia, ib, w));
            }

            String home = in.next(); // just need to know it exists
            if (!id.containsKey(home)) { System.out.println("Impossible"); continue; }

            Collections.sort(edges);
            DSU dsu = new DSU(nv);
            long total = 0;
            int taken = 0;
            for (Edge e : edges) {
                if (dsu.unite(e.u, e.v)) {
                    total += e.w;
                    taken++;
                    if (taken == nv - 1) break;
                }
            }

            System.out.println(taken == nv - 1 ? total : "Impossible");
        }
        in.close();
    }
}
