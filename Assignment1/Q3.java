package Assignment1;


import java.util.ArrayDeque;
import java.util.Deque;

public class Q3 {

    private final Deque<Customer> line = new ArrayDeque<>();
    private final Customer[] cashier = new Customer[3]; 

    public void serve(int i) {
        if (i < 1 || i > 2) return;                 
        if (!line.isEmpty() && cashier[i] == null) {
            cashier[i] = line.removeFirst();
        }
    }

    public void interruptService(int i) {
        if (i < 1 || i > 2) return;                 
        if (cashier[i] != null) {
            line.addFirst(cashier[i]);
            cashier[i] = null;
        }
    }

    public void newcustomer(Customer p) {
        if (p == null) return;                      
        line.addLast(p);
    }

    public void giveUp(int n) {
        for (int k = 0; k < n && !line.isEmpty(); k++) {
            line.removeLast();
        }
    }
}


