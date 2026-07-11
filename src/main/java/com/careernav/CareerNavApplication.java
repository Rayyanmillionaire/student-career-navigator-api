package com.careernav;

import com.careernav.model.User;
import com.careernav.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CareerNavApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerNavApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Admin if it doesn't exist
            if (!userRepository.existsByRollNumber("IEMML25CS000")) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setRollNumber("IEMML25CS000");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("admin");
                userRepository.save(admin);
                System.out.println("Seeded admin account: IEMML25CS000 / admin123");
            }

            // Seed Custom Owner Admin if it doesn't exist
            if (!userRepository.existsByRollNumber("IEMML25CS001")) {
                User owner = new User();
                owner.setName("Rayyan Admin");
                owner.setRollNumber("IEMML25CS001");
                owner.setPassword(passwordEncoder.encode("Rayyan@Admin"));
                owner.setRole("admin");
                userRepository.save(owner);
                System.out.println("Seeded owner admin account: IEMML25CS001 / Rayyan@Admin");
            }

            // Seed Student if it doesn't exist
            if (!userRepository.existsByRollNumber("IEMML25CS002")) {
                User student = new User();
                student.setName("Student Demo");
                student.setRollNumber("IEMML25CS002");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setRole("student");
                student.setCollege("Demo University");
                student.setMajor("Computer Science");
                userRepository.save(student);
                System.out.println("Seeded student account: IEMML25CS002 / student123");
            }
        };
    }
}
