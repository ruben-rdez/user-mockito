package com.user.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.user.mockito.entity.User;
import com.user.mockito.exception.UserNotFoundException;
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

	@Test
	public void testUpdateUser_Found(){
		User user = new User(1L, "Test User", "test@example.com");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);

		User result = userService.updateUser(user);
        assertNotNull(result);
        assertEquals(1L, result.getId());
		assertEquals("Test User", result.getName());
		assertEquals("test@example.com", result.getEmail());
	}

	@Test
    void testUpdateUser_NotFound() {
		Long userId = 2L;
        User updatedUser = new User(2L, "New User", "new@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

		User result = userService.updateUser(updatedUser);
        assertNull(result);
		verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
	
	@Test
	public void testDeleteUser(){
		Long id = 1L;
		userService.deleteUser(id);
		assertNull(userService.getUserById(id));
	}
}
