package com.hoaxify.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.shared.GenericResponse;

@RestController
@RequestMapping("/api/1.0/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping({"", "/"})
	GenericResponse createUser(@RequestBody User user) {
		userService.save(user);
		return new GenericResponse("User Saved");
	}
}
