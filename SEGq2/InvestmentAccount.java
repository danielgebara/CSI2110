package SEGq2;

public class InvestmentAccount extends Account {
    private String riskLevel; // e.g., "LOW", "MEDIUM", "HIGH"

    public InvestmentAccount(String number, double openingBalance, String nickname, String riskLevel) {
        super(number, openingBalance, nickname);
        this.riskLevel = riskLevel;
    }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}

