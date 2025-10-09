package SEGq2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank {
    private final String name;
    private final List<Branch> branches = new ArrayList<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addBranch(Branch b) {
        if (b == null) return;
        if (!branches.contains(b)) {
            branches.add(b);
        }
    }

    public void removeBranch(Branch b) {
        branches.remove(b);
    }

    public List<Branch> listBranches() {
        return Collections.unmodifiableList(branches);
    }
}
