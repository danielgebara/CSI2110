package SEGq2;

public class SavingsAccount extends Account {
    private double interestRate; // e.g., 0.02 = 2% per month (for demo)

    public SavingsAccount(String number, double openingBalance, String nickname, double interestRate) {
        super(number, openingBalance, nickname);
        this.interestRate = interestRate;
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public void applyMonthlyInterest() {
        if (interestRate != 0.0) {
            double interest = getBalance() * interestRate;
            setBalance(getBalance() + interest);
            addTransaction(new Transaction(java.util.UUID.randomUUID().toString(), "INTEREST_CREDIT", interest, java.time.Instant.now(), this));
        }
    }
}

