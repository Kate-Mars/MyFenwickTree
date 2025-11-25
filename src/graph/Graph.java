package graph;
import ds.MyList;
/* graph class */
public interface Graph<T> {
    int addVertex(T value);
    void addEdge(int from, int to);
    T getVertexValue(int v);
    MyList<Integer> neighbors(int v);
    int vertexCount();
    boolean isDirected();
}