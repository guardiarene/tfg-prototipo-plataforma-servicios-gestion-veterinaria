package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.user.User;

@Component
public class UserValidator {

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be positive and not null");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public void validateForCreation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateBasicFields(user);
        validatePassword(user.getPassword());
    }

    public void validateForUpdate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateId(user.getId());
        validateBasicFields(user);
    }

    public void validateForCompleteUpdate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateBasicFields(user);
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }

    private void validateBasicFields(User user) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        validateEmail(user.getEmail());
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

}
