package com.hoaxify.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "{hoaxify.constraints.username.NotBlank.message}")
	@Size(min = 4, max = 255)
	private String username;
	@NotBlank
	@Size(min = 4, max = 255)
	private String displayName;
	@NotBlank
	@Size(min = 8, max = 255)
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
			message = "{hoaxify.constraints.password.pattern.message}"
	)	//atleast 1 lowercase 1 uppercase, 1 digit
	private String password;
	public User(String username, String displayName, String password) {
		super();
		this.username = username;
		this.displayName = displayName;
		this.password = password;
	}
	
}
