/*
 * A way to test CITS2200 Project with different sized graph using System.nanoTime()
 * I also use a seed to for adding the random edges so the graph should be generated the same every time
 * if anyone using this needs to compare their results to make sure the algorithms are correct
 * 
 * 
 * Created by Alex Hawking
 */

 import java.util.Arrays;
 import java.util.Random;
 
 public class size_test {
 
     private static final Random random = new Random();
 
     public static void main(String[] args) {
         /*
          * Set what different sized graphs you want to test (this is the number of vertices)
          */
         int[] sizes = {18};
         /*
          * Random generation seed, if you want to compare results
          * However storing the results of the different methods can introduce significant overhead 
          * on large graphs, so this will affect the measured time
          */
         int seed = 694;
         
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
 
             String[] v = new String[num_vertices];
             // Generate vertices with IDs between 0 and numVertices - 1
             for (int j = 0; j < num_vertices; j++) {
                 String url = "url_" + j;
                 v[j] = url;
             }
             // Add edges to form a Hamiltonian path
             for (int j = 0; j < num_vertices - 1; j++) {
                 graphs[i].addEdge(v[j], v[j + 1]);
             }
 
             int max_edges = (num_vertices * (num_vertices - 1)) / 2;
             int remaining_edges = max_edges - (num_vertices - 1); // Subtract edges added for Hamiltonian path
 
             // Add remaining random edges
             
             for (int j = 0; j < remaining_edges; j++) {
                 int fromId = random.nextInt(num_vertices);
                 int toId = random.nextInt(num_vertices);
                 graphs[i].addEdge(v[fromId], v[toId]);
             }
             
 
             // Measure the execution time for each method
             long startTime, endTime, executionTime;
 
             
             System.out.println("-----------------------------------------------------------");
             System.out.println("Results for graph with " + num_vertices + " vertices");
             System.out.println("-----------------------------------------------------------");
             // 1. Measure getShortestPath() execution time
             startTime = System.nanoTime();
             graphs[i].getShortestPath("url_0", "url_" + (num_vertices - 1));
             endTime = System.nanoTime();
             executionTime = endTime - startTime;
             System.out.println("getShortestPath() execution time: " + executionTime + " nanoseconds");
             
             // 2. Measure getHamiltonianPath() execution time
             if (num_vertices < 20) {
                 startTime = System.nanoTime();
                 String[] path = graphs[i].getHamiltonianPath();
                 endTime = System.nanoTime();
                 executionTime = endTime - startTime;
                 System.out.println("getHamiltonianPath() execution: " + executionTime + " nanoseconds");
                 System.out.println(Arrays.toString(path));
             } else {
                 System.out.println("Not testing getHamiltonianPath() as too many vertices");
             }
            
             // 3. Measure getStronglyConnectedComponents() execution time
             startTime = System.nanoTime();
             graphs[i].getStronglyConnectedComponents();
             endTime = System.nanoTime();
             executionTime = endTime - startTime;
             System.out.println("getStronglyConnectedComponents() execution time for graph size: " + executionTime + " nanoseconds");
 
             // 4. Measure getCenters() execution time
             startTime = System.nanoTime();
             graphs[i].getCenters();
             endTime = System.nanoTime();
             executionTime = endTime - startTime;
             System.out.println("getCenters() execution time for graph size: " + executionTime + " nanoseconds");
             System.out.println(); 
            
 
         }
     }
 }
 