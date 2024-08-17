package com.foodApp.controller;

import com.foodApp.config.JwtProvider;
import com.foodApp.model.Cart;
import com.foodApp.model.PasswordResetToken;
import com.foodApp.model.User;
import com.foodApp.repository.CartRepository;
import com.foodApp.repository.UserRepository;
import com.foodApp.request.LoginRequest;
import com.foodApp.request.ResetPasswordRequest;
import com.foodApp.response.AuthResponse;
import com.foodApp.response.MessagResponse;
import com.foodApp.service.CustomerUserDetailsService;

import com.foodApp.domain.USER_ROLE;
import com.foodApp.service.PasswordResetTokenService;
import com.foodApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws Exception {

		User isEmailExist=userRepository.findByEmail(user.getEmail());
		if(isEmailExist!=null){
			throw new Exception("Email is already used with another account");
		}

		User createdUser=new User();
		createdUser.setEmail(user.getEmail());
		createdUser.setFullName(user.getFullName());
		createdUser.setRole(user.getRole());
		createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

		User savedUser = userRepository.save(createdUser);

		Cart cart=new Cart();
		cart.setCustomer(savedUser);
		cartRepository.save(cart);

		Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt=jwtProvider.generateToken(authentication);

		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register success");
		authResponse.setRole(savedUser.getRole());

		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {

		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		System.out.println(username + " ----- " + password);

		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();

		authResponse.setMessage("Login Success");
		authResponse.setJwt(token);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();


		authResponse.setRole(USER_ROLE.valueOf(roleName));

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails= customerUserDetailsService.loadUserByUsername(username);
		if(userDetails==null){
			throw new BadCredentialsException("Invalid username...");

		}
		if (!passwordEncoder.matches(password,userDetails.getPassword())){

			throw new BadCredentialsException("Invalid password ...");

		}
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
	@PostMapping("/reset-password")
	public ResponseEntity<MessagResponse> resetPassword(

			@RequestBody ResetPasswordRequest req) throws Exception {

		PasswordResetToken resetToken = passwordResetTokenService.findByToken(req.getToken());

		if (resetToken == null ) {
			throw new Exception("token is required...");
		}
		if(resetToken.isExpired()) {
			passwordResetTokenService.delete(resetToken);
			throw new Exception("token get expired...");

		}

		// Update user's password
		User user = resetToken.getUser();
		userService.updatePassword(user, req.getPassword());

		// Delete the token
		passwordResetTokenService.delete(resetToken);

		MessagResponse res=new MessagResponse();
		res.setMessage("Password updated successfully.");
		res.setStatus(true);

		return ResponseEntity.ok(res);
	}

	@PostMapping("/reset-password-request")
	public ResponseEntity<MessagResponse> resetPassword(@RequestParam("email") String email) throws Exception {
		User user = userService.findUserByEmail(email);
		System.out.println("ResetPasswordController.resetPassword()");

		if (user == null) {
			throw new Exception("user not found");
		}

		userService.sendPasswordResetEmail(user);

		MessagResponse res=new MessagResponse();
		res.setMessage("Password reset email sent successfully.");
		res.setStatus(true);

		return ResponseEntity.ok(res);
	}
}