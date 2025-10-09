package SEGq2;

import java.util.*;

public abstract class Account {
    private static final Set<String> NUMBERS = new HashSet<>(); // enforce uniqueness

    private final String number;           // {unique}
    private double balance;
    private String nickname;               // forbidden in LoanAccount
    private Client owner;                  // 1
    private final List<Transaction> transactions = new ArrayList<>(); // 0..*

    protected Account(String number, double openingBalance, String nickname) {
        if (number == null || number.isEmpty())
            throw new IllegalArgumentException("Account number required");
        synchronized (NUMBERS) {
            if (!NUMBERS.add(number)) {
                throw new IllegalArgumentException("Account number must be unique: " + number);
            }
        }
        this.number = number;
        this.balance = openingBalance;
        this.nickname = nickname;
    }

    public String getNumber() { return number; }
    public double getBalance() { return balance; }
    protected void setBalance(double balance) { this.balance = balance; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; } // overridden in LoanAccount

    public Client getOwner() { return owner; }
    /* package-private */ void setOwner(Client owner) { this.owner = owner; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        balance += amount;
        addTransaction(new Transaction(UUID.randomUUID().toString(), "DEPOSIT", amount, java.time.Instant.now(), this));
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        // Default behaviour (no overdraft). CheckingAccount will override rule via helper.
        if (!canWithdraw(amount)) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
        addTransaction(new Transaction(UUID.randomUUID().toString(), "WITHDRAW", amount, java.time.Instant.now(), this));
    }

    protected boolean canWithdraw(double amount) {
        return balance >= amount;
    }

    public List<Transaction> listTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction t) {
        if (t != null) transactions.add(t);
    }
}

