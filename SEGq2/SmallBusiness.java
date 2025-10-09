package SEGq2;

import java.time.LocalDate;

public class SmallBusiness extends Adult {
    private String registrationNumber;

    public SmallBusiness(String clientId, String name, String email, LocalDate birthDate, String registrationNumber) {
        super(clientId, name, email, birthDate);
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
}

