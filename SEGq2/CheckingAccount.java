package SEGq2;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String number, double openingBalance, String nickname, double overdraftLimit) {
        super(number, openingBalance, nickname);
        this.overdraftLimit = Math.max(0.0, overdraftLimit);
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = Math.max(0.0, overdraftLimit); }

    @Override
    protected boolean canWithdraw(double amount) {
        return (getBalance() + overdraftLimit) >= amount;
    }
}

