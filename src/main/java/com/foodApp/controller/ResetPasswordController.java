package com.foodApp.controller;


import com.foodApp.model.PasswordResetToken;
import com.foodApp.model.User;
import com.foodApp.request.ResetPasswordRequest;
import com.foodApp.response.MessagResponse;
import com.foodApp.service.PasswordResetTokenService;
import com.foodApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


public class ResetPasswordController {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private UserService userService;

    @PostMapping
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
    
    @PostMapping("/reset")
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

