package com.foodApp.service;

import com.foodApp.config.JwtProvider;
import com.foodApp.model.PasswordResetToken;
import com.foodApp.model.Restaurant;
import com.foodApp.model.User;
import com.foodApp.repository.CartRepository;
import com.foodApp.repository.PasswordResetTokenRepository;
import com.foodApp.repository.RestaurantRepository;
import com.foodApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        User user=userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        // Find the restaurant associated with the user
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        // Delete the restaurant if it exists
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
        }

        // Delete the cart entries associated with the user
        cartRepository.deleteById(userId);

        // Finally, delete the user
        userRepository.deleteById(userId);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public void sendPasswordResetEmail(User user) {

        // Generate a random token (you might want to use a library for this)
        String resetToken = generateRandomToken();

        // Calculate expiry date
        Date expiryDate = calculateExpiryDate();

        // Save the token in the database
        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken,user,expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        // Send an email containing the reset link
        sendEmail(user.getEmail(), "Password Reset", "Click the following link to reset your password: http://localhost:3000/account/reset-password?token=" + resetToken);
    }
    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }



//    @Override
//    public List<User> getPenddingRestaurantOwner() {
//        return List.of();
//    }
}
