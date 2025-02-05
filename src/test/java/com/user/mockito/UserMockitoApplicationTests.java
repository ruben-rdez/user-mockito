package com.user.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.user.mockito.entity.User;
import com.user.mockito.repository.UserRepository;
import com.user.mockito.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserMockitoApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	public void testGetUserByEmail_Found(){
		String name = "Test User";
		String email = "test@example.com";
		User mockUser = new User(1L, name, email);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		User user = userService.getUserByEmail(email);
		assertNotNull(user);
		assertEquals(email, user.getEmail());
	}

	@Test
    public void testGetUserByEmail_NotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        User user = userService.getUserByEmail(email);
        assertNull(user);
    }

	@Test
	public void testSaveUser(){
		User user = new User(null, "New User", "new@example.com");
		User savedUser = new User(1L, "New User", "new@example.com");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

 		User result = userService.saveUser(user);
        assertNotNull(result);
        assertEquals(1L, result.getId());
	}
}
