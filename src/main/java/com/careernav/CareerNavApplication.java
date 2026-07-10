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
            if (!userRepository.existsByEmail("admin@scn.com")) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@scn.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("admin");
                userRepository.save(admin);
                System.out.println("Seeded admin account: admin@scn.com / admin123");
            }

            // Seed Student if it doesn't exist
            if (!userRepository.existsByEmail("student@scn.com")) {
                User student = new User();
                student.setName("Student Demo");
                student.setEmail("student@scn.com");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setRole("student");
                student.setCollege("Demo University");
                student.setMajor("Computer Science");
                userRepository.save(student);
                System.out.println("Seeded student account: student@scn.com / student123");
            }
        };
    }
}
