package SEGq2;

import java.time.LocalDate;

public class Individual extends Adult {
    public Individual(String clientId, String name, String email, LocalDate birthDate) {
        super(clientId, name, email, birthDate);
    }
}

