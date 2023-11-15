package com.carrentalsystem.restapi.Interface;

import com.carrentalsystem.restapi.Model.UserAccount;

public interface UserAccountInterface {
    int createNewUser(UserAccount userAccount);
    boolean sendOTP(String emailId);
    boolean verifyOTP(String emailId, String otp);
    UserAccount fetchUserById(String emailId);
    boolean updatePassword(UserAccount userAccount);
    UserAccount updateOtherDetails(UserAccount userAccount);
}
