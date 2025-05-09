import java.util.Random;

public class GraphGenerator {
    private int V; // Number of vertices
    private int[][] graph;
    private static final int INF = 100000000;

    // Constructor for predefined graph
    public GraphGenerator(int[][] predefinedGraph) {
        this.V = predefinedGraph.length;
        this.graph = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = predefinedGraph[i][j];
            }
        }
    }

    // Constructor for random graph (optional, kept for flexibility)
    public GraphGenerator(int vertices) {
        this.V = vertices;
        graph = new int[V][V];
        generateRandomGraph();
    }

    private void generateRandomGraph() {
        Random rand = new Random();
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    graph[i][j] = 0;
                } else {
                    if (rand.nextBoolean()) {
                        graph[i][j] = rand.nextInt(10) + 1;
                    } else {
                        graph[i][j] = INF;
                    }
                }
            }
        }
    }

    public int[][] getGraph() {
        return graph;
    }

    public int getVertices() {
        return V;
    }
}