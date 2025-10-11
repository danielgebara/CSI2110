package ProgrammingAssignment1;

//Part2B

import java.util.*;

public class IslandLakeSurvey {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int S = sc.nextInt(); 
        int T = sc.nextInt(); 
        sc.nextLine(); 

        int[][] map = new int[S][T];
        for (int i = 0; i < S; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < T; j++) {
                map[i][j] = line.charAt(j) - '0';
            }
        }

        // Initialize Partition ADT to keep track 
        Partition<int[]> partition = new Partition<>(S * T);
        Node<int[]>[][] clusters = new Node[S][T];
        int numClusters = 0;

        // First phase: identify the islands
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 1) { 
                    int pos = partition.makeCluster(new int[]{i, j});
                    clusters[i][j] = new Node<>(new int[]{i, j});
                    if (i > 0 && map[i - 1][j] == 1) {
                        partition.union(pos, getCluster(i - 1, j, clusters));
                    }
                    if (j > 0 && map[i][j - 1] == 1) {
                        partition.union(pos, getCluster(i, j - 1, clusters));
                    }
                }
            }
        }

        // Surveying the initial landscape
        surveyLandWithLakes(partition);

        // Reading flood recovery phases
        int F = sc.nextInt(); 
        sc.nextLine(); 
        for (int phase = 0; phase < F; phase++) {
            int L = sc.nextInt(); 
            sc.nextLine();
            for (int l = 0; l < L; l++) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                map[x][y] = 1; 
                int pos = partition.makeCluster(new int[]{x, y});
                clusters[x][y] = new Node<>(new int[]{x, y});
                if (x > 0 && map[x - 1][y] == 1) {
                    partition.union(pos, getCluster(x - 1, y, clusters));
                }
                if (y > 0 && map[x][y - 1] == 1) {
                    partition.union(pos, getCluster(x, y - 1, clusters));
                }
            }
            // Survey the land after flood recovery, including lakes
            surveyLandWithLakes(partition);
        }

        sc.close();
    }

    // Helper function to get the cluster index for a given position
    private static int getCluster(int x, int y, Node<int[]>[][] clusters) {
        return clusters[x][y] != null ? clusters[x][y].getElement()[0] : -1;
    }

    // Function to display the Survey of the Land with lake information
    private static void surveyLandWithLakes(Partition<int[]> partition) {
        System.out.println(partition.numberOfClusters());
        int[] clusterSizes = partition.clusterSizes();
        if (clusterSizes.length == 0) {
            System.out.println("-1");
        } else {
            for (int i = clusterSizes.length - 1; i >= 0; i--) {
                System.out.print(clusterSizes[i] + " ");
            }
            System.out.println();
        }
        System.out.println("0"); // No lakes detected yet
        System.out.println("0"); // No area of lakes
    }
}

