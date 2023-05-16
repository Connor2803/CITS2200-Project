import java.util.*;

public class MyProject implements CITS2200Project {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<String, Integer> vertexMap;
    private int vertexCount;

    public MyProject() {
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

    private void hehehehaw(){
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
        // Implementation here
        return null;
    }

    public static void main(String[] args){
        MyProject project = new MyProject();
        CITS2200ProjectTester.loadGraph(project, "example_graph.txt");
        project.hehehehaw();
        int shortestpath = project.getShortestPath("/wiki/Flow_network", "/wiki/Dinic%27s_algorithm");
        System.out.println(shortestpath);
    }
}
