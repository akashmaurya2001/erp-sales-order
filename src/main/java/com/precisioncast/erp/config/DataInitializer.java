package com.precisioncast.erp.config;

import com.precisioncast.erp.auth.entity.Role;
import com.precisioncast.erp.auth.entity.User;
import com.precisioncast.erp.auth.enums.RoleType;
import com.precisioncast.erp.auth.repository.RoleRepository;
import com.precisioncast.erp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Role adminRole = createRoleIfNotExists(RoleType.ADMIN);
        Role salesManagerRole = createRoleIfNotExists(RoleType.SALES_MANAGER);
        Role productionManagerRole = createRoleIfNotExists(RoleType.PRODUCTION_MANAGER);
        Role qualityManagerRole = createRoleIfNotExists(RoleType.QUALITY_MANAGER);
        Role procurementManagerRole = createRoleIfNotExists(RoleType.PROCUREMENT_MANAGER);

        createUserIfNotExists(
                "Vikram Singh",
                "vikram.singh@precisioncast.com",
                "9876543210",
                "Admin@123",
                adminRole
        );

        createUserIfNotExists(
                "Sneha Mehta",
                "sneha.mehta@precisioncast.com",
                "9876543211",
                "Sales@123",
                salesManagerRole
        );

        createUserIfNotExists(
                "Rajesh Kumar",
                "rajesh.kumar@precisioncast.com",
                "9876543212",
                "Production@123",
                productionManagerRole
        );

        createUserIfNotExists(
                "Priya Sharma",
                "priya.sharma@precisioncast.com",
                "9876543213",
                "Quality@123",
                qualityManagerRole
        );

        createUserIfNotExists(
                "Amit Patel",
                "amit.patel@precisioncast.com",
                "9876543214",
                "Procurement@123",
                procurementManagerRole
        );
    }

    private Role createRoleIfNotExists(RoleType roleType) {
        return roleRepository.findByRoleName(roleType)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName(roleType);
                    return roleRepository.save(role);
                });
    }

    private void createUserIfNotExists(String fullName,
                                       String email,
                                       String mobileNumber,
                                       String rawPassword,
                                       Role role) {

        boolean emailExists = userRepository.existsByEmail(email);
        boolean mobileExists = userRepository.existsByMobileNumber(mobileNumber);

        if (!emailExists && !mobileExists) {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setMobileNumber(mobileNumber);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setActive(true);
            user.setRole(role);
            userRepository.save(user);
            System.out.println("User created: " + email);
        } else {
            System.out.println("User already exists or mobile already mapped: " + email);
        }
    }
}