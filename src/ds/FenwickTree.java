package ds;
/**
 * Класс FenwickTree
 *
 * Методы:
 *  - void build(int[] arr)
 *  - void update(int index, int delta)
 *  - int prefixSum(int index)
 *  - int rangeSum(int left, int right)
 */
public class FenwickTree {

    private int n;
    private int[] tree;
    private int[] arr;

    public FenwickTree(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        this.n = n;
        this.tree = new int[n + 1]; // индексы [1..n]
        this.arr = new int[n];      // arr[0..n-1]
    }

    public void build(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException("input is null");
        }
        if (input.length != n) {
            throw new IllegalArgumentException(
                    "input length (" + input.length + ") != n (" + n + ")");
        }

        for (int i = 0; i < n; i++) {
            arr[i] = input[i];
        }

        for (int i = 0; i <= n; i++) {
            tree[i] = 0;
        }

        for (int i = 0; i < n; i++) {
            tree[i + 1] = arr[i];
        }

        for (int i = 1; i <= n; i++) {
            int j = i + (i & -i);
            if (j <= n) {
                tree[j] += tree[i];
            }
        }
    }

    public void update(int index, int delta) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
        arr[index] += delta;
        internalAdd(index + 1, delta);
    }

    public int prefixSum(int index) {
        // строгая проверка: только 0..n-1
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException(
                    "index = " + index + ", допустимый диапазон: 0.." + (n - 1)
            );
        }

        int i = index + 1;
        int res = 0;
        while (i > 0) {
            res += tree[i];
            i -= i & -i;
        }
        return res;
    }

    public int rangeSum(int left, int right) {
        if (left > right) {
            throw new IllegalArgumentException("left > right");
        }
        if (left < 0 || right >= n) {
            throw new IndexOutOfBoundsException("left = " + left + ", right = " + right);
        }
        if (left == 0) {
            return prefixSum(right);
        }
        return prefixSum(right) - prefixSum(left - 1);
    }

    private void internalAdd(int pos, int delta) {
        while (pos <= n) {
            tree[pos] += delta;
            pos += pos & -pos;
        }
    }

    // методы для визуализации

    public int size() {
        return n;
    }

    public int[] getArrSnapshot() {
        int[] copy = new int[n];
        for (int i = 0; i < n; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public int[] getTreeSnapshot() {
        int[] copy = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            copy[i] = tree[i];
        }
        return copy;
    }
}