package SEGq2;

import java.time.LocalDate;

/** Non-BMO clients we keep info about. They do NOT have BMO accounts. */
public class ExternalBankClient extends Client {
    public ExternalBankClient(String clientId, String name, String email, LocalDate birthDate) {
        super(clientId, name, email, birthDate);
    }
}

