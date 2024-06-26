package graphs;

import java.util.*;

public class Graph<T> {
    private final HashMap<T, HashSet<T>> adjacencyList = new HashMap<>();
    private final boolean isDirected;
    private static final String[] COLORS = {
        "\033[0m",      // RESET
        "\033[0;90m",  // BLACK
        "\033[0;91m",    // RED
        "\033[0;92m",  // GREEN
        "\033[0;93m", // YELLOW
        "\033[0;94m",   // BLUE
        "\033[0;95m", // PURPLE
        "\033[0;96m",   // CYAN
        "\033[0;97m" // WHITE
    };
    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
    }
    public Graph() {
        this(false);
    }
    public void addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex))
            adjacencyList.put(vertex, new HashSet<>());
        else throw new IllegalArgumentException("Vertex already in the graph");
    }
    public void addEdge(T from, T to) {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to))
            throw new IllegalArgumentException("Vertices are not in the graph");
        if (!adjacencyList.get(from).contains(to))
            adjacencyList.get(from).add(to);
        else throw new IllegalArgumentException("Edge already exists");
        if (!isDirected) { // undirected graph
            if (!adjacencyList.get(to).contains(from))
                adjacencyList.get(to).add(from);
            else throw new IllegalArgumentException("Edge already exists");
        }
    }
    /*Exploring the graph starting from a given vertex v from V*/
    public void explore(T vertex) {
        if (!adjacencyList.containsKey(vertex))
            throw new IllegalArgumentException("Vertex not in the graph");
        HashSet<T> visited = new HashSet<>();
        System.out.print("Explored: ");
        explore(vertex, visited);
        System.out.println();
    }
    private void explore(T vertex, HashSet<T> visited) {
        visited.add(vertex); // same as v.visited = True
        System.out.print(vertex + " ");
        for(T neighbor: adjacencyList.get(vertex))
            if (!visited.contains(neighbor))
                explore(neighbor, visited);
    }
    /*end of exploring*/

    /* depth first search algorithm on graph */
    public void dfs() {
        // those visited will be saved into this hash set
        HashSet<T> visited = new HashSet<>();
        System.out.print("DFS: ");
        for(T vertex : adjacencyList.keySet())
            if (!visited.contains(vertex)) // if not v.visited
                explore(vertex, visited);
        System.out.println();
    }
    /*end of DFS*/

    /* connected components*/
    public void connectedComponents() {
        // print connected components with the same color, if we have more components than colors it will crash :D
        HashMap<T, Integer> canvas = new HashMap<>();
        int color = 1;
        for (T vertex : adjacencyList.keySet()) {
            if (!canvas.containsKey(vertex)) { // not visited
                paintComponents(vertex, color, canvas);
                color += 1;
            }
        } // done painting components
        // let's print it now
        printWithColors(canvas);
    }

    private void paintComponents(T vertex, int color, HashMap<T, Integer> canvas) {
        canvas.put(vertex, color); // visited and painted
        for (T neighbor: adjacencyList.get(vertex)) {
            if (!canvas.containsKey(neighbor))
                paintComponents(neighbor, color, canvas);
        }
    }

    private void printWithColors(HashMap<T, Integer> colors) {
        try {
            for(T vertex : colors.keySet())
                System.out.print(COLORS[colors.get(vertex)] + vertex + " ");
            System.out.println(COLORS[0]); // to reset the color
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Not enough colors to paint components");
            System.exit(1);
        }
    }
    /* end of connected components*/

    /* pre-visit and post-visit */
    public void clockTick() {
        // clock ticks at each pre/post - visit
        // hash map with vertices as keys and an array with 2 elements [pre, post]
        HashSet<T> visited = new HashSet<>();
        HashMap<T, Integer> preVisit = new HashMap<>();
        HashMap<T, Integer> postVisit = new HashMap<>();
        int[] clock = {1};
        for(T v : adjacencyList.keySet()) {
            if (!visited.contains(v))
                visit(v, visited, preVisit, postVisit, clock);
        }
        // print the order in which they were visited
        for(T v : visited) {
            System.out.printf("%s (%d/%d), ", v, preVisit.get(v), postVisit.get(v));
        }
        System.out.println();
    }

    private void visit(T v, HashSet<T> visited, HashMap<T, Integer> preVisit, HashMap<T, Integer> postVisit, int[] clock) {
        visited.add(v);
        preVisit.put(v, clock[0]);
        clock[0]++;
        for(T neighbor : adjacencyList.get(v)) {
            if (!visited.contains(neighbor))
                visit(neighbor, visited, preVisit, postVisit, clock);
        }
        postVisit.put(v, clock[0]);
        clock[0]++;
    }
    /* end of pre-visit and post-visit */

    /* strongly connected components */
    public void stronglyConnectedComponents() {
        Graph<T> reversedGraph = buildReversedGraph();
        SortedMap<Integer, T> postOrder = new TreeMap<>(Collections.reverseOrder());
        sccDFS(reversedGraph, postOrder);
        HashMap<T, Integer> colors = new HashMap<>();
        int color = 1;
        for(int order : postOrder.keySet()) {
            T v = postOrder.get(order);
            if (!colors.containsKey(v)) {
                markSCC(v, color, colors);
                color++;
            }
        }
        // let's print it now
        printWithColors(colors);
    }

    private void markSCC(T v, int color, HashMap<T, Integer> colors) {
        colors.put(v, color);
        for(T neighbor : adjacencyList.get(v)) {
            if (!colors.containsKey(neighbor))
                markSCC(neighbor, color, colors);
        }
    }

    private void sccDFS(Graph<T> reversedGraph, SortedMap<Integer, T> postOrder) {
        int[] clock = {1};
        HashSet<T> visited = new HashSet<>();
        for (T v : reversedGraph.adjacencyList.keySet()) {
            if (!visited.contains(v)) {
                explorePostOrder(reversedGraph, v, visited, postOrder, clock);
            }
        }
    }

    private void explorePostOrder(Graph<T> reversedGraph, T v, HashSet<T> visited, SortedMap<Integer, T> postOrder, int[] clock) {
        visited.add(v);
        clock[0]++;
        for (T neighbor : reversedGraph.adjacencyList.get(v)) {
            if (!visited.contains(neighbor))
                explorePostOrder(reversedGraph, neighbor, visited, postOrder, clock);
        }
        postOrder.put(clock[0], v);
        clock[0]++;
    }

    private Graph<T> buildReversedGraph() {
        Graph<T> reversedGraph = new Graph<>(this.isDirected);
        for (T v : adjacencyList.keySet())
            reversedGraph.addVertex(v);
        for (T v : adjacencyList.keySet()) {
            for (T neighbor : adjacencyList.get(v)) {
                reversedGraph.addEdge(neighbor, v);
            }
        }
        return reversedGraph;
    }

    /* end of strongly connected components */

    /* breadth first search the whole graph starting from a given vertex */
    public void bfs(T vertex) {
        HashMap<T, Integer> distances = new HashMap<>();
        for (T v : adjacencyList.keySet())
            distances.put(v, Integer.MAX_VALUE); // + infinity
        distances.put(vertex, 0); // distance from itself is 0
        Queue<T> queue = new ArrayDeque<>();
        queue.add(vertex);
        while (!queue.isEmpty()) {
            T v = queue.remove();
            for(T neighbor : adjacencyList.get(v)) {
                if (distances.get(neighbor) == Integer.MAX_VALUE) {
                    queue.add(neighbor);
                    distances.put(neighbor, distances.get(v) + 1);
                }
            }
        }
        System.out.print("BFS order from vertex " + vertex + " : ");
        for (T v : distances.keySet()) {
            String d = (distances.get(v) != Integer.MAX_VALUE ? distances.get(v).toString() : "infinity");
            System.out.printf("%s(%s), ", v, d);
        }
        System.out.println();
    }
    /* end of breadth first search */

    // another implementation of BFS, this one is a bit fancier. Visit any node reachable from vertex s.
    public void BFS(T s) {
        HashMap<T, Integer> level = new HashMap<>(); level.put(s, 0);
        HashMap<T, T> parent = new HashMap<>(); parent.put(s, null);
        int i = 1;
        Queue<T> frontier = new ArrayDeque<>(); frontier.add(s); // vertices reachable in level i - 1
        while (!frontier.isEmpty()) {
            Queue<T> next = new ArrayDeque<>();
            for (T v : frontier) {
                for(T u : adjacencyList.get(v)) {
                    if (!level.containsKey(u)) {
                        level.put(u, i);
                        parent.put(u, v);
                        next.add(u);
                    }
                }
            }
            frontier = next;
            i += 1;
        }
        reconstructAllPaths(s, level, parent);
    }

    private void reconstructAllPaths(T s, HashMap<T, Integer> level, HashMap<T, T> parent) {
        for(T v : adjacencyList.keySet()) { // for all vertices
            if(level.containsKey(v)) reconstructPath(s, v, level, parent);
            else System.out.println("No path from " + s + " to " + v);
        }
    }

    private void reconstructPath(T s, T v, HashMap<T, Integer> level, HashMap<T, T> parent) {
        System.out.print("Distance from " + s + " to " + v + " is " + level.get(v));
        ArrayList<T> result = new ArrayList<>();
        while (v != null) {
            result.add(v);
            v = parent.get(v);
        }
        Collections.reverse(result);
        System.out.println(", path is: " + result.stream().map(Object::toString).reduce((x, y) -> x + "->" + y).orElse("[]"));

    }

    /* you're a functional programming expert now*/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(T vertex : adjacencyList.keySet()) {
            sb.append(vertex)
                    .append(": ")
                    .append(adjacencyList.get(vertex).stream()
                            .map(Object::toString)
                            .reduce((x, y) -> x + "->" + y)
                            .orElse("[]"))
                    .append("\n");
        }
        return sb.toString();
    }
}
