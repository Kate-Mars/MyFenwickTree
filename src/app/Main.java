package app;

import ds.SimpleArrayList;
import graph.AdjacencyListGraph;


public class Main {
    public static void main(String[] args) {
        System.out.println("array list");
        SimpleArrayList<Integer> list = new SimpleArrayList<>();
        list.add(5);
        list.add(10);
        list.add(15);
        System.out.println("size: " + list.size());
        System.out.println("el:");
        for (int i = 0; i < list.size(); i++)
            System.out.println("[" + i + "] = " + list.get(i));

        System.out.println("\nграф");
        AdjacencyListGraph<String> g = new AdjacencyListGraph<>(false);
        int a = g.addVertex("A");
        int b = g.addVertex("B");
        int c = g.addVertex("C");
        g.addEdge(a, b);
        g.addEdge(a, c);
        System.out.println("vertex: " + g.vertexCount());
        for (int i = 0; i < g.vertexCount(); i++) {
            System.out.print(g.getVertexValue(i) + " -> ");
            var neigh = g.neighbors(i);
            for (int j = 0; j < neigh.size(); j++)
                System.out.print(g.getVertexValue(neigh.get(j)) + " ");
            System.out.println();
        }
    }
}
