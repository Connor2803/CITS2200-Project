import java.util.*;

// Created by Connor Grayden (23349066) and Nathan Buluran ()

public class MyProjecto implements CITS2200Project {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<String, Integer> vertexMap;
    private int vertexCount;

    public MyProjecto() {
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
        int[] visited = new int[vertexCount]; // Array to track visited vertices
        List<Integer> path = new ArrayList<>(); // List to store the path
    
        // Start the backtracking from the first vertex
        if (findHamiltonianPath(0, visited, path)) {
            // Convert the path list to an array of strings
            String[] hamiltonianPath = new String[path.size()];
            for (int i = 0; i < path.size(); i++) {
                int vertex = path.get(i);
                hamiltonianPath[i] = getURL(vertex);
            }
            return hamiltonianPath;
        } else {
            return null; // No Hamiltonian path found
        }
    }
    
    private boolean findHamiltonianPath(int currentVertex, int[] visited, List<Integer> path) {
        visited[currentVertex] = 1; // Mark the current vertex as visited
        path.add(currentVertex); // Add the current vertex to the path
    
        if (path.size() == vertexCount) {
            return true; // All vertices have been visited, Hamiltonian path found
        }
        
        // try get the neighbors if no neighbours set -1 or smth to say it has no neighbirs

        List<Integer> neighbors = adjacencyList.getOrDefault(currentVertex, new ArrayList<>());
        for (int neighbor : neighbors) {
            if (visited[neighbor] == 0) { // Neighbor is not visited
                if (findHamiltonianPath(neighbor, visited, path)) {
                    return true; // Hamiltonian path found
                }
            }
        }
    
        visited[currentVertex] = 0; // Backtrack: mark the current vertex as not visited
        path.remove(path.size() - 1); // Backtrack: remove the current vertex from the path
    
        return false; // No Hamiltonian path found from the current vertex
    }
    

    public static void main(String[] args) {
        MyProjecto project = new MyProjecto();
        CITS2200ProjectTester.loadGraph(project, "testdata/alex.txt");
        project.printGraph();
        int shortestPath = project.getShortestPath("/wiki/Flow_network", "/wiki/Dinic%27s_algorithm");
        System.out.println("Shortest Path: " + shortestPath);

        boolean x = project.hasEdge(0, 0);
        System.out.println(x);

        String[] hamiltonianPath = project.getHamiltonianPath();
        if (hamiltonianPath != null){
            System.out.print("Hamiltonian Path: [");
            for (String url : hamiltonianPath) {
                System.out.print(url+",");
            }
            System.out.print("]");
        }
        else System.out.println("No Hamiltonian Path");
        
    }
}
