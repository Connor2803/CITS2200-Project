import javax.print.event.PrintEvent;

public class MyProject implements CITS2200Project {
    // Implement the methods declared in the CITS2200Project interface
    
    public void addEdge(String urlFrom, String urlTo){
        System.out.println("adding edge...");
    }

    public int getShortestPath(String urlFrom, String urlTo) {
        // Implementation here
        return 0;
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
        System.out.println("Wow pogger");
    }
}
