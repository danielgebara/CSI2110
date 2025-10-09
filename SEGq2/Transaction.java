package SEGq2;


import java.time.Instant;

public class Transaction {
    private final String id;
    private final String type;     // "DEPOSIT", "WITHDRAW", "INTEREST_*", etc.
    private final double amount;
    private final Instant timestamp;
    private final Account account; // back-reference

    public Transaction(String id, String type, double amount, Instant timestamp, Account account) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.account = account;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public Instant getTimestamp() { return timestamp; }
    public Account getAccount() { return account; }

    public String describe() {
        return "[" + timestamp + "] " + type + " $" + amount + " on #" + (account != null ? account.getNumber() : "?");
    }
}

