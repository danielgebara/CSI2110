package SEGq2;

import java.time.LocalDate;

public class Investor extends Adult {
    private String riskProfile; // optional

    public Investor(String clientId, String name, String email, LocalDate birthDate) {
        super(clientId, name, email, birthDate);
    }

    public String getRiskProfile() { return riskProfile; }
    public void setRiskProfile(String riskProfile) { this.riskProfile = riskProfile; }
}

