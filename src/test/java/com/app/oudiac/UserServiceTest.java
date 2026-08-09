package com.app.oudiac;

import static org.junit.jupiter.api.Assertions.*;

import com.app.oudiac.dtos.userDtos.UserRegisterRequestDto;
import com.app.oudiac.repositories.AdminRepository;
import com.app.oudiac.services.adminService.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired // Injects the REAL service
    private AdminService adminService;

    @Autowired // Injects the REAL repository so you can check the DB
    private AdminRepository adminRepository;

    @Test
    void testSaveUserToDatabase() {
        // Arrange
        UserRegisterRequestDto dto = new UserRegisterRequestDto();
        dto.setEmail("mohd.maarula11@gmail.com");
        dto.setPassword("Abcd@1234");
        dto.setName("Nafees");

        // Act: This will actually run SQL and insert into the DB
        adminService.registerAdmin(dto);

        // Assert: Query the real database to ensure it was saved
        boolean exists = adminRepository.existsByEmail("mohd.maarula11@gmail.com");
        assertTrue(exists, "The user should be saved in the database");
    }
}
