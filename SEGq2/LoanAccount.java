package SEGq2;


public class LoanAccount extends Account {
    private double interestRate; // e.g., 0.01 = 1% per month (for demo)

    public LoanAccount(String number, double openingBalance, double interestRate) {
        // nickname must be forbidden for loan accounts
        super(number, openingBalance, null);
        this.interestRate = interestRate;
    }

    @Override
    public void setNickname(String nickname) {
        if (nickname != null && !nickname.isEmpty()) {
            throw new UnsupportedOperationException("Nickname is not allowed for LoanAccount.");
        }
        // silently ignore or set to null
        super.setNickname(null);
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public void applyMonthlyInterest() {
        if (interestRate != 0.0) {
            double interest = getBalance() * interestRate;
            setBalance(getBalance() + interest); // increases amount owed
            addTransaction(new Transaction(java.util.UUID.randomUUID().toString(), "INTEREST_CHARGE", interest, java.time.Instant.now(), this));
        }
    }
}

