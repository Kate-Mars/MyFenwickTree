package ds;
/* array list implementation */
public class SimpleArrayList<T> implements MyList<T> {
    private Object[] data;
    private int size;
    public SimpleArrayList() {
        this.data = new Object[8];
        this.size = 0;
    }

    @Override
    public void add(T value) {
        ensureCapacity(size + 1);
        data[size] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @Override
    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length) {
            return;
        }
        int newCap = data.length * 2;
        if (newCap < needed) {
            newCap = needed;
        }
        Object[] newData = new Object[newCap];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
    }
}