package SEGq2;

import java.time.LocalDate;

public abstract class Adult extends Client {
    protected Adult(String clientId, String name, String email, LocalDate birthDate) {
        super(clientId, name, email, birthDate);
    }
}

