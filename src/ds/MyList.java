package ds;
/* simple list */
public interface MyList<T> {
    void add(T value);
    T get(int index);
    void set(int index, T value);
    int size();
    boolean isEmpty();
}