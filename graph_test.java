import java.util.*;

public class graph_test {

    private static final Random random = new Random();

    public static void main(String[] args) {

        int[] sizes = {10};
        int seed = 20;     
        random.setSeed(seed);
        test(sizes, seed);
    }

    private static void test(int[] sizes, int seed) {
        CITS2200Project[] graphs = new CITS2200Project[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            /*
             * 
             * IMPORTANT: Change MyCITS2200Project to whatever your class is called
             * 
             */
            graphs[i] = new MyProject();
            /*
             * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             */
            int num_vertices = sizes[i];

            Map<String, ArrayList<String>> adjList = new HashMap<>();


            // Generate vertices with IDs between 0 and numVertices - 1
            for (int j = 0; j < num_vertices; j++) {
                String url = "url_" + j;
                adjList.put(url,  new ArrayList<String>());
            }

            ArrayList<String> urls = new ArrayList<>(adjList.keySet());           
             // Add edges to ensure a Hamiltonian path
            for (int j = 0; j < num_vertices - 1; j++) {
                graphs[i].addEdge(urls.get(j), urls.get(j + 1));
                adjList.get(urls.get(j)).add(urls.get(j + 1));
            }
            int max_edges = (num_vertices * (num_vertices - 1)) / 2;
            int remaining_edges = max_edges - (num_vertices - 1); 
            
            
            for (int j = 0; j < remaining_edges; j++) {
                int fromId = random.nextInt(num_vertices - 1);
                int toId = random.nextInt(num_vertices - 1);
                graphs[i].addEdge(urls.get(fromId), urls.get(toId));
                adjList.get(urls.get(fromId)).add(urls.get(toId));
            }
            

            
            System.out.println("-----------------------------------------------------------");
            System.out.println("Results for graph with " + num_vertices + " vertices");
            System.out.println("-----------------------------------------------------------\n");

            System.out.println("Adjacency List:");
            for (int x = 0; x < urls.size(); x++) {
                //Remove duplicates
                ArrayList<String> toUrls = new ArrayList<>(new HashSet<>(adjList.get("url_" + x)));
                System.out.println("url_" + x + " : " + toUrls.toString());
            };
            System.out.println();
            
            int shortestPath = graphs[i].getShortestPath("url_0", "url_" + (num_vertices - 1));
            System.out.println("getShortestPath() from url_0 to url_"+ (num_vertices - 1) + ": \n" + shortestPath + "\n");

            if (num_vertices < 20) {
                String[] path = graphs[i].getHamiltonianPath();
                System.out.println("getHamiltonianPath() execution: \n" + Arrays.toString(path) + "\n");
            } else {
                System.out.println("Not testing getHamiltonianPath() as too many vertices\n");
            }
            
            String[][] scc = graphs[i].getStronglyConnectedComponents();
            System.out.println("getStronglyConnectedComponents(): \n" + Arrays.deepToString(scc) + "\n");

            String[] centers = graphs[i].getCenters();
            System.out.println("getCenters(): \n" + Arrays.toString(centers) + "\n");
            System.out.println(); 

        }
    }
}