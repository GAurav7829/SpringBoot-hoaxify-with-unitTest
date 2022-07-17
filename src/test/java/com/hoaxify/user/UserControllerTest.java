package com.hoaxify.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.hoaxify.error.ApiError;
import com.hoaxify.shared.GenericResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
	
	private static final String API_1_0_USERS = "/api/1.0/users";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	UserRepository userRepository;
	
	@BeforeEach
	public void cleanUp() {
		userRepository.deleteAll();
	}
	
	@Test
	public void postUser_whenUserIsValid_receiveOK() {
		User user = createValidUser();
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void postUser_whenUserIsValid_userSavedToDatabase() {
		User user = createValidUser();
		postSignUp(user, Object.class);
		assertThat(userRepository.count()).isEqualTo(1);
	}
	
	@Test
	public void postUser_whenUserIsValid_receiveSuccessMessage() {
		User user = createValidUser();
		ResponseEntity<GenericResponse> response = postSignUp(user, GenericResponse.class);
		assertThat(response.getBody().getMessage()).isNotNull();
	}
	
	@Test
	public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
		User user = createValidUser();
		postSignUp(user, Object.class);
		List<User> users = userRepository.findAll();
		User inDB = users.get(0);
		assertThat(inDB.getPassword()).isNotEqualTo(user.getPassword());
	}
	
	@Test
	public void postUser_whenUserHasNullUsername_receiveBadRequest() {
		User user = createValidUser();
		user.setUsername(null);
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasNullDisplay_receiveBadRequest() {
		User user = createValidUser();
		user.setDisplayName(null);
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasNullPassword_receiveBadRequest() {
		User user = createValidUser();
		user.setPassword(null);
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasUsernameWithLessThanRequired_receiveBadRequest() {
		User user = createValidUser();
		user.setUsername("abc");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasDisplaynameWithLessThanRequired_receiveBadRequest() {
		User user = createValidUser();
		user.setDisplayName("abc");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {
		User user = createValidUser();
		user.setPassword("abc");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasUsernameWithExceedsLengthLimit_receiveBadRequest() {
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x->"a").collect(Collectors.joining());
		user.setUsername(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasDisplayNameWithExceedsLengthLimit_receiveBadRequest() {
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x->"a").collect(Collectors.joining());
		user.setDisplayName(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasPasswordWithExceedsLengthLimit_receiveBadRequest() {
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x->"a").collect(Collectors.joining());
		user.setPassword(valueOf256Chars+"A1");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasPasswordWithAllLowerCase_receiveBadRequest() {
		User user = createValidUser();
		user.setPassword("alllowercase");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasPasswordWithAllUpperCase_receiveBadRequest() {
		User user = createValidUser();
		user.setPassword("ALLUPPERCASE");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasPasswordWithAllNumbers_receiveBadRequest() {
		User user = createValidUser();
		user.setPassword("123456789");
		ResponseEntity<Object> response = postSignUp(user, null);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserIsInvalid_receiveApiError() {
		User user = new User();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
	}
	
	@Test
	public void postUser_whenUserIsInvalid_receiveApiErrorWithValidationErrors() {
		User user = new User();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
	}
	
	@Test
	public void postUser_whenWhenUserHasNullUsername_receiveMessageOfNullErrorForUsername() {
		User user = new User();
		user.setUsername(null);
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("username")).isEqualTo("Username cannot be blank");
	}
	
	@Test
	public void postUser_whenWhenUserHasNullPassword_receiveMessageOfNullErrorForPassword() {
		User user = new User();
		user.setUsername(null);
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("password")).isEqualTo("Cannot be blank");
	}
	
	@Test
	public void postUser_whenWhenUserHasInvalidOfUsername_receiveGenericMessageOfSizeError() {
		User user = new User();
		user.setUsername("abc");
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("username")).isEqualTo("It must be minimum 4 and maximum 255 characters");
	}
	
	@Test
	public void postUser_whenWhenUserHasInvalidPasswordPattern_receiveMessageOfPasswordPatternError() {
		User user = new User();
		user.setPassword("abcdefghij");
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("password")).isEqualTo("Password must have atleast one Uppercase, one Lowercase and one Number");
	}
	
	@Test
	public void postUser_whenAnotherUserHasSameUsername_receiveBadRequest() {
		userRepository.save(createValidUser());
		User user = createValidUser();
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenAnotherUserHasSameUsername_receiveMessageOfDuplicateUsername() {
		userRepository.save(createValidUser());
		User user = createValidUser();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("username")).isEqualTo("This name is in use");
	}
	
	public <T> ResponseEntity<T> postSignUp(Object request, Class<T> response){
		return testRestTemplate.postForEntity(API_1_0_USERS, request, response);
	}
	
	private User createValidUser() {
		User user = new User("test-user","test-display","P4ssword");
		return user;
	}
}
