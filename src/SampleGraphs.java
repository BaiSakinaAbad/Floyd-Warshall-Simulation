public class SampleGraphs {
    private static final int INF = 100000000;

    // Graph 1: Similar to the first photo (4 vertices)
    public static int[][] getGraph1() {
        return new int[][] {
                {0, 5, INF, INF},
                {INF, 0, 3, INF},
                {2, INF, 0, 6},
                {INF, 2, INF, 0}
        };
    }

    // Graph 2: Similar to the second photo (4 vertices)
    public static int[][] getGraph2() {
        return new int[][] {
                {0, 5, INF, INF},
                {2, 0, 3, INF},
                {INF, INF, 0, 2},
                {INF, INF, INF, 0}
        };
    }

    // Graph 3: Similar to the third photo (5 vertices)
    public static int[][] getGraph3() {
        return new int[][] {
                {0, 4, INF, INF, INF},
                {INF, 0, 1, INF, 6},
                {2, INF, 0, 3, INF},
                {INF, INF, INF, 0, 4},
                {INF, INF, INF, INF, 0}
        };
    }

    // Graph 4: Similar to the fourth photo (5 vertices)
    public static int[][] getGraph4() {
        return new int[][] {
                {0, INF, 6, INF, 8},
                {INF, 0, 1, 3, INF},
                {INF, INF, 0, INF, INF},
                {INF, INF, INF, 0, INF},
                {INF, INF, INF, INF, 0}
        };
    }

    // Graph 5: Custom simple graph (4 vertices)
    public static int[][] getGraph5() {
        return new int[][] {
                {0, 7, INF, 3},
                {INF, 0, 4, INF},
                {2, INF, 0, 5},
                {INF, INF, INF, 0}
        };
    }

    // Graph 6: Custom simple graph (5 vertices)
    public static int[][] getGraph6() {
        return new int[][] {
                {0, 2, INF, INF, 5},
                {INF, 0, 3, INF, INF},
                {INF, INF, 0, 4, INF},
                {6, INF, INF, 0, 1},
                {INF, INF, INF, INF, 0}
        };
    }

    // Method to get a graph by index
    public static int[][] getSampleGraph(int index) {
        switch (index) {
            case 0: return getGraph1();
            case 1: return getGraph2();
            case 2: return getGraph3();
            case 3: return getGraph4();
            case 4: return getGraph5();
            case 5: return getGraph6();
            default: return getGraph1();
        }
    }
}