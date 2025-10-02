package Assignment1;

import java.util.ArrayList;

public class question3 {
    private int size;
    int frontIndex;
    ArrayList<Object> arr;

    public question3() {
        size = 0;
        frontIndex = 0;
        arr = new ArrayList<>();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object top() {
        if(isEmpty()) return null;
        return arr.get(size + frontIndex - 1);
    }

    public void push(Object element) {
        arr.add(element);
        size++;
    }

    public Object pop() {
        if(isEmpty()) return null;
        Object temp = arr.get(size + frontIndex - 1);
        size--;
        return temp;
    }

    public void forget(int k) {
        if (k >= size) {
            frontIndex = 0;
            size = 0;
            arr = new ArrayList<>();
        } else {
            frontIndex += k;
            size -= k;
        }
    }
}
