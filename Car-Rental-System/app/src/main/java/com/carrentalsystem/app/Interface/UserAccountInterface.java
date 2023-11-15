package com.carrentalsystem.app.Interface;

import com.carrentalsystem.app.Model.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

public interface UserAccountInterface {
    int registerNewUser(Map<String, Object> mapper);
    boolean sendOTP(String emailId);
    boolean verifyOTP(String emailId, String otp);
    boolean updatePassword(Map<String, Object> mapper);
    RedirectView validateLogin(HttpServletRequest httpServletRequest, HttpSession httpSession);
    UserAccount fetchUserById(String emailId);
    UserAccount updateProfileDetails(Map<String, Object> mapper);
}
