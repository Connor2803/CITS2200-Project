import java.util.*;

// Created by Connor Grayden (23349066) and Nathan Buluran ()

public class MyProjecton implements CITS2200Project {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<String, Integer> vertexMap;
    private static int vertexCount;
    private static final int INF = 999999;

    public MyProjecton() {
        adjacencyList = new HashMap<>();
        vertexMap = new HashMap<>();
        vertexCount = 0;
    }

    public void addEdge(String urlFrom, String urlTo) {
        int fromVertex = getVertex(urlFrom);
        int toVertex = getVertex(urlTo);

        List<Integer> neighbours = adjacencyList.getOrDefault(fromVertex, new ArrayList<>());
        neighbours.add(toVertex);
        adjacencyList.put(fromVertex, neighbours);
        adjacencyList.putIfAbsent(toVertex, new ArrayList<>());
    }

    private int getVertex(String url) {
        if (vertexMap.containsKey(url)) {
            return vertexMap.get(url);
        } else {
            int vertex = vertexCount++;
            vertexMap.put(url, vertex);
            return vertex;
        }
    }

    private void printGraph() {
        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            int vertex = entry.getKey();
            List<Integer> neighbors = entry.getValue();

            System.out.print(vertex + ": ");
            for (int neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }

    private boolean hasEdge(int fromVertex, int toVertex) {
        List<Integer> neighbors = adjacencyList.getOrDefault(fromVertex, new ArrayList<>());
        return neighbors.contains(toVertex);
    }

    private String getURL(int vertex) {
        for (Map.Entry<String, Integer> entry : vertexMap.entrySet()) {
            if (entry.getValue() == vertex) {
                return entry.getKey();
            }
        }
        return null;
    }


    public int getShortestPath(String urlFrom, String urlTo) {
        // Implementation here
        int startVertex = vertexMap.getOrDefault(urlFrom, -1);
        int targetVertex = vertexMap.getOrDefault(urlTo, -1);

        if (startVertex == -1 || targetVertex == -1) {
            return -1; // Invalid URLs
        }

        int[] distance = new int[vertexCount];
        Arrays.fill(distance, -1);
        distance[startVertex] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();

            if (currentVertex == targetVertex) {
                return distance[currentVertex]; // Found the shortest path
            }

            List<Integer> neighbors = adjacencyList.getOrDefault(currentVertex, new ArrayList<>());
            for (int neighbor : neighbors) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[currentVertex] + 1;
                    queue.offer(neighbor);
                }
            }
        }

        return -1; // No path found
    }

    public String[] getCenters() {
        // Implementation here
        return null;
    }

    public String[][] getStronglyConnectedComponents() {
        // Implementation here
        return null;
    }

    public String[] getHamiltonianPath() {
        // Convert adjacency list to adjacency matrix
        int[][] adjacencyMatrix = new int[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                adjacencyMatrix[i][j] = hasEdge(i, j) ? 1 : 0;
            }
        }

        List<Integer> finalpath = findHamiltonianPath(adjacencyMatrix);
        String[] output = new String[20];

        for(int i : finalpath){
            output[i] = getURL(i);
        }

        return output;
    }


    public static List<Integer> findHamiltonianPath(int[][] graph) {
        int n = graph.length;
        int[][] dp = new int[1 << n][n]; // DP table

        // Initialize DP table
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[1][0] = 0; // Base case: Hamiltonian path ending at vertex 0 with no other vertices

        // Compute DP table entries
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 1; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    for (int j = 0; j < n; j++) {
                        if (j != i && (mask & (1 << j)) != 0 && graph[j][i] != -1) {
                            dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j] + graph[j][i]);
                        }
                    }
                }
            }
        }

        // Find the minimum cost of Hamiltonian path
        int minCost = INF;
        for (int i = 1; i < n; i++) {
            if (graph[i][0] != -1) {
                minCost = Math.min(minCost, dp[(1 << n) - 1][i] + graph[i][0]);
            }
        }

        if (minCost == INF) {
            // No Hamiltonian path found
            return null;
        }

        // Traceback to reconstruct the Hamiltonian path
        List<Integer> path = new ArrayList<>();
        int currVertex = 0;
        int remainingSet = (1 << n) - 1;
        for (int i = 0; i < n - 1; i++) {
            int nextVertex = -1;
            for (int j = 1; j < n; j++) {
                if (j != currVertex && (remainingSet & (1 << j)) != 0 &&
                        graph[j][currVertex] != -1 &&
                        dp[remainingSet][j] + graph[j][currVertex] == minCost) {
                    nextVertex = j;
                    break;
                }
            }
            path.add(nextVertex);
            remainingSet &= ~(1 << nextVertex);
            currVertex = nextVertex;
        }
        path.add(0); // Add the starting vertex

        // Print the minimum cost Hamiltonian path
        //System.out.println("Minimum Cost: " + minCost);
        System.out.println("Hamiltonian Path: " + path);

        return path;
    }

    public static void main(String[] args) {
        MyProjecton project = new MyProjecton();
        CITS2200ProjectTester.loadGraph(project, "alex.txt");
        project.printGraph();
        int shortestPath = project.getShortestPath("/wiki/Flow_network", "/wiki/Dinic%27s_algorithm");
        System.out.println("Shortest Path: " + shortestPath);


        String[] hamiltonianPath = project.getHamiltonianPath();
        System.out.println("Hamiltonian Path:");
        for (String url : hamiltonianPath) {
            System.out.println(url);
        }
    }
}
