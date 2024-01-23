package com.example.tokentest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.tokentest.user.User;
import com.example.tokentest.user.UserRepository;

import lombok.RequiredArgsConstructor;

import com.example.tokentest.user.Role;

@DataJpaTest
@RequiredArgsConstructor
@SpringBootTest
public class UserRepositoryTest {
    
    private UserRepository underTest;

    private final PasswordEncoder passwordEncoder;

    @Test
    public void testFindByEmail() {
        // Arrange
        User user = createUser("john.doe@example.com");
        underTest.save(user);

        // Act
        Optional<User> foundUser = underTest.findByEmail("john.doe@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("john.doe@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindByFirstname() {
        // Arrange
        User user = createUser("John", "Doe", "john.doe@example.com");
        underTest.save(user);

        // Act
        Optional<User> foundUser = underTest.findByFirstname("John");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstname());
    }

    @Test
    public void testFindByEmail_NotFound() {
        // Act
        Optional<User> foundUser = underTest.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }



    //Helper Methods:
    private User createUser(String email) {
        return User.builder()
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .build();
    }

    private User createUser(String firstname, String lastname, String email) {
        return User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .build();
    }
}

