package login.register.login;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/auth")
public class User_Service {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password) {
        String message = "";

        User user = userRepository.findByUsername(username);
        if (user != null && verifyPassword(password, user)) {
            message = "Login successful!";
        } else {
            message = "Invalid username or password";
        }

        return message;
    }

    @PostMapping("/register")
    public String handleRegistration(
            @RequestParam String username,
            @RequestParam String password) throws Exception {
        String message = "";
        System.out.println("hello");
        // Logic for registration
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            if (isStrongPassword(password)) {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEncryptionKey(generateEncryptionKey());
                String encryptedPassword = AESEncryptionService.encrypt(password, newUser.getEncryptionKey());
                newUser.setEncrypted_password(encryptedPassword);
                userRepository.save(newUser);

                message = "Registration successful!";
            } else {
                message = "Password does not meet the strength requirements.";
            }
        } else {
            message = "Username already exists";
        }

        return message;
    }

        private String generateEncryptionKey () {
            int keySizeInBytes = 32;

            // Generate a secure random key
            byte[] keyBytes = new byte[keySizeInBytes];
            new SecureRandom().nextBytes(keyBytes);

            String encryptionKey = Base64.getEncoder().encodeToString(keyBytes);

            // Debug statement
            System.out.println("Generated Encryption Key: " + encryptionKey);

            return encryptionKey;
        }

    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?`~]).{8,}$";
        return password.matches(regex);
    }

    private boolean verifyPassword(String inputPassword, User user) {
        try {
            String encryptedInputPassword = AESEncryptionService.encrypt(inputPassword, user.getEncryptionKey());

            // Debug statements
            System.out.println("Input Password: " + inputPassword);
            System.out.println("Encrypted Input Password: " + encryptedInputPassword);
            System.out.println("Stored Encrypted Password: " + user.getEncrypted_password());

            return user.getEncrypted_password().equals(encryptedInputPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
