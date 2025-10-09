package SEGq2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Client {
    private final String clientId;
    private final String fullNameOrRegisteredName;
    private final String email;
    private final java.time.LocalDate birthDate;

    private Branch primaryBranch;                 // 1
    private final List<Account> accounts = new ArrayList<>(); // 1..*
    private LoyaltyProfile loyalty;               // 0..1

    protected Client(String clientId, String name, String email, java.time.LocalDate birthDate) {
        this.clientId = clientId;
        this.fullNameOrRegisteredName = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public String getClientId() { return clientId; }
    public String getName() { return fullNameOrRegisteredName; }
    public String getEmail() { return email; }
    public java.time.LocalDate getBirthDate() { return birthDate; }

    public void setPrimaryBranch(Branch b) { this.primaryBranch = b; }
    public Branch getPrimaryBranch() { return primaryBranch; }

    /* Composition-ish helper: add/remove Accounts that belong to this Client */
    public void addAccount(Account a) {
        if (a == null) return;
        if (!accounts.contains(a)) {
            accounts.add(a);
            a.setOwner(this);
        }
    }

    public void removeAccount(Account a) {
        if (a == null) return;
        accounts.remove(a);
        if (a.getOwner() == this) {
            a.setOwner(null);
        }
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public LoyaltyProfile getLoyaltyProfile() { return loyalty; }
    public void setLoyaltyProfile(LoyaltyProfile profile) {
        this.loyalty = profile;
        if (profile != null && profile.getOwner() != this) {
            profile.setOwner(this);
        }
    }
}

