import java.util.*;

// Created by Connor Grayden (23349066) and Nathan Buluran (23402727)

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
        List<Integer> centers = new ArrayList<>();
        int minMaxPath = Integer.MAX_VALUE;

        for (int i = 0; i < vertexCount; i++){
            int [] currentArray = getDistances(i);
            
            //sets currentMaxPath to the max path from the vertex i to any other vertex
            int currentMaxPath = Arrays.stream(currentArray).max().getAsInt();

            //if the max path of i is smaller than the current minimum maximum path then clear the centers and set i as the center
            //also filters out vertexes with no outward paths i.e. a vertex with 0 as the max path
            if (currentMaxPath < minMaxPath && currentMaxPath > 0){
                minMaxPath = currentMaxPath;
                centers.clear();
                centers.add(i);
            }
            //adds vertex to centers if max path is equal to the current minimum max path
            else if (currentMaxPath == minMaxPath){
                centers.add(i);
            }
        }

        //converts from int to URL
        String[] output = new String[centers.size()];
        for (int i = 0; i < centers.size(); i++){
            output[i] = getURL(centers.get(i));
        }
        return output;
    }

    //outputs the distance from the startVertex to all other vertexes
    private int[] getDistances(int startVertex){
        //initialises and sets all elements to be -1 in distances
        int[] distances = new int[vertexCount];
        Arrays.fill(distances, -1);

        //initialises and sets all elements to be false in visited
        boolean[] visited = new boolean[vertexCount];
        Arrays.fill(visited, false);

        //initialises queue and adds the starting vertex to queue
        Queue<Integer> q = new LinkedList<>();
        q.add(startVertex);

        //currDistance will be used to keep track of how many links are between the current vertex and the starting vertex
        int currDistance = 0;
        distances[startVertex] = currDistance;

        //iterates throught queue until empty
        while (!q.isEmpty()){
            //poll the front of the queue and set the current distance to the distance of the vertex from the startVertex
            int current = q.poll();
            currDistance = distances[current];
            //catches null pointer exceptions since vertexes with no outward edges are not represeneted in adjacencyList
            try{
                for (int i : adjacencyList.get(current)){
                    //visits neighbours if they have not been visted and assigns them a distance from the start vertex and adds them to the queue
                    if (!visited[i]){
                        distances[i] = currDistance + 1;
                        q.add(i);
                        visited[i] = true;
                    }
                }
            }
            catch (NullPointerException e){}
        }

        return distances;
    }

    public String[][] getStronglyConnectedComponents() {
        // Implementation here
        //initialises and sets all elements to be false in visited
        boolean[] visited = new boolean[vertexCount];
        Arrays.fill(visited, false);

        //initialises stack to store vertexs in during dfs and initialise an array list to store our strongly connected components in
        Stack<Integer> stack = new Stack<>();
        List<Stack<Integer>> stronglyConComps = new ArrayList<>();
        
        //performs dfs, utilises for loop in case of any vertexes with no inward edges so therefore has to be manually visited
        for (int i = 0 ; i < vertexCount; i++){
            if (!visited[i]){
                dfs(i, visited, stack, adjacencyList);
            }
        }

        //the graph is transposed witout mutating the adjacency list
        Map<Integer, List<Integer>> transposedAdjList = new HashMap<Integer, List<Integer>>();
        transposedAdjList = getTranspose();

        //resets visited
        Arrays.fill(visited, false);

        //iterates through stack allowing manual jump between strongly connect components 
        while (!stack.isEmpty()){
            int current = stack.pop();
            if (!visited[current]){
                //vertexes that dfs can access in the transposed adjacency list are part of a strongly connect component
                Stack<Integer> scc = new Stack<>();
                dfs(current, visited, scc, transposedAdjList);
                //adds the stack of scc's from dfs to strongConComps
                stronglyConComps.add(scc);
            }
        }

        //converts integers and the stack to our output type of String[][]
        String[][] output = new String[stronglyConComps.size()][];
        //counter for seperating each scc for the array
        int i = 0;
        //iterates through each stack in the array and converts them from int to String representations of the links
        for (Stack<Integer> scc : stronglyConComps){
            List<String> convertedScc = new ArrayList<>();
            while (!scc.isEmpty()){
                int current = scc.pop();
                convertedScc.add(getURL(current));
            }
            output[i] = convertedScc.toArray(new String[convertedScc.size()]);
            i++;
        }

        return output;
    }

    //transposes graph
    private Map<Integer, List<Integer>> getTranspose(){
        Map<Integer,List<Integer>> transpose = new HashMap<Integer,List<Integer>>();
        for (int u : adjacencyList.keySet()) {
            for (int v : adjacencyList.get(u)) {
                //initialises new values for each of v where v are u's neighbours
                List<Integer> newValues = transpose.get(v);
                //constructs an array list as the value for the hashmap in transposed adjacency list if no array list already
                if (newValues == null) {
                    transpose.put(v, newValues = new ArrayList<Integer>());
                }
                //adds the vertex u which was formerly an inward connection to u's values this becoming an outward connection thereby tranposing
                newValues.add(u);
            }
        }
        return transpose;
    }

    //recursive dfs for getStronglyConnectedComponents()
    private void dfs(int vertex, boolean[] visited, Stack<Integer> stack, Map<Integer, List<Integer>> inputMap){
        //sets input vertex as true
        visited[vertex] = true;

        //catches null pointer exceptions since vertexes with no outward edges are not represeneted in adjacencyList
        try{
            for (int u : inputMap.get(vertex)){
                //recursively calls dfs if vertex u has not been visited
                if (!visited[u]){
                    dfs(u, visited, stack, inputMap);
                }
            }
        }
        catch (NullPointerException e){
        }
        //pushes vertex to the top of the stack
        stack.push(vertex);
    }

    public String[] getHamiltonianPath() {
        int[] visited = new int[vertexCount]; // Array to track visited vertices
        List<Integer> path = new ArrayList<>(); // List to store the path
    
        // Try to start the backtracking from every vertex
        for(int hamStart = 0;hamStart < vertexCount;hamStart++){
            if (findHamiltonianPath(hamStart, visited, path)) {
                // Convert the path list to an array of strings
                String[] hamiltonianPath = new String[path.size()];
                for (int i = 0; i < path.size(); i++) {
                    int vertex = path.get(i);
                    hamiltonianPath[i] = getURL(vertex);
                }
                return hamiltonianPath;
            } else {
                continue;
            }
        }
        return new String[0]; // No Hamiltonian path found for any start vertex
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
}
