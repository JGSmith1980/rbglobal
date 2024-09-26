package smith.jeremy.rbglobal.webflux.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;
import smith.jeremy.rbglobal.webflux.exception.ValidationException;

@Getter
@Table(name = "customers")
@Slf4j
public class Customer {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) throws ValidationException {
        // this constructor / setter style enforces business rules on the entity object
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
    }

    public void setFirstName(String firstName) throws ValidationException {
        if (StringUtils.hasText(firstName)) {
            this.firstName = firstName;
        } else {
            log.info("Invalid first name");
            throw new ValidationException("First name cannot be empty");
        }
    }
    public void setLastName(String lastName) throws ValidationException {
        if (StringUtils.hasText(lastName)) {
            this.lastName = lastName;
        }else {
            log.info("Invalid last name");
            throw new ValidationException("Last name cannot be empty");
        }
    }
    public void setEmail(String email) throws ValidationException {
        if (StringUtils.hasText(email) && email.contains("@")) {
            this.email = email;
        }else {
            log.info("Invalid email");
            throw new ValidationException("Email cannot be empty");
        }
    }
}
