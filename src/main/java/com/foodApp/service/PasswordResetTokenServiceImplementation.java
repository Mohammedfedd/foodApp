package com.foodApp.service;


import com.foodApp.model.PasswordResetToken;
import com.foodApp.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PasswordResetTokenServiceImplementation implements PasswordResetTokenService {
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public PasswordResetToken findByToken(String token) {
		PasswordResetToken passwordResetToken =passwordResetTokenRepository.findByToken(token);
		return passwordResetToken;
	}

	@Override
	public void delete(PasswordResetToken resetToken) {
		passwordResetTokenRepository.delete(resetToken);
		
	}

}
