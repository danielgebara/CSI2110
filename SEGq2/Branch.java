package SEGq2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Branch {
    private final String code;
    private final String address;
    private final List<Client> clients = new ArrayList<>();

    public Branch(String code, String address) {
        this.code = code;
        this.address = address;
    }

    public String getCode() { return code; }
    public String getAddress() { return address; }

    public void addClient(Client c) {
        if (c == null) return;
        if (!clients.contains(c)) {
            clients.add(c);
            c.setPrimaryBranch(this);
        }
    }

    public void removeClient(Client c) {
        clients.remove(c);
        if (c != null && c.getPrimaryBranch() == this) {
            c.setPrimaryBranch(null);
        }
    }

    public List<Client> listClients() {
        return Collections.unmodifiableList(clients);
    }
}

