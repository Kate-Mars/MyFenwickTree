package graph;

import ds.MyList;
import ds.SimpleArrayList;

/* adjacency graph list implementation */
public class AdjacencyListGraph<T> implements Graph<T> {
    private final boolean directed;
    private final SimpleArrayList<T> vertices;
    private SimpleArrayList<Integer>[] adj;
    @SuppressWarnings("unchecked")
    public AdjacencyListGraph(boolean directed, int initialCapacity) {
        this.directed = directed;
        this.vertices = new SimpleArrayList<>();
        this.adj = (SimpleArrayList<Integer>[]) new SimpleArrayList[initialCapacity];
    }
    public AdjacencyListGraph(boolean directed) {
        this(directed, 8);
    }

    @Override
    public int addVertex(T value) {
        int id = vertices.size();
        vertices.add(value);
        ensureAdjCapacity(id + 1);
        adj[id] = new SimpleArrayList<>();
        return id;
    }
    @Override
    public void addEdge(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        adj[from].add(to);
        if (!directed && from != to) {
            adj[to].add(from);
        }
    }
    @Override
    public T getVertexValue(int v) {
        checkVertex(v);
        return vertices.get(v);
    }
    @Override
    public MyList<Integer> neighbors(int v) {
        checkVertex(v);
        return adj[v];
    }
    @Override
    public int vertexCount() {
        return vertices.size();
    }
    @Override
    public boolean isDirected() {
        return directed;
    }

    @SuppressWarnings("unchecked")
    private void ensureAdjCapacity(int needed) {
        if (needed <= adj.length) {
            return;
        }
        int newCap = adj.length * 2;
        if (newCap < needed) {
            newCap = needed;
        }
        SimpleArrayList<Integer>[] newAdj =
                (SimpleArrayList<Integer>[]) new SimpleArrayList[newCap];
        for (int i = 0; i < adj.length; i++) {
            newAdj[i] = adj[i];
        }
        adj = newAdj;
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertices.size()) {
            throw new IndexOutOfBoundsException("vertex = " + v);
        }
    }
}