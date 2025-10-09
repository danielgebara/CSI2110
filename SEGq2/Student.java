package SEGq2;

import java.time.LocalDate;

public class Student extends Client {
    public Student(String clientId, String name, String email, LocalDate birthDate) {
        super(clientId, name, email, birthDate);
    }
}

