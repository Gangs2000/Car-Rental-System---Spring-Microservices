package com.carrentalsystem.restapi.Controller;

import com.carrentalsystem.restapi.Model.UserAccount;
import com.carrentalsystem.restapi.Service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car-rental-system/user")
public class UserAccountController {

    @Autowired UserAccountService userAccountService;
    @PostMapping("/register")
    public int registerNewUser(@RequestBody UserAccount userAccount){
        return userAccountService.createNewUser(userAccount);
    }

    @GetMapping("/send-otp/emailId/{emailId}")
    public boolean sendOTP(@PathVariable("emailId") String emailId){
        return userAccountService.sendOTP(emailId);
    }

    @GetMapping("/verify-otp/emailId/{emailId}/pin/{otp}")
    public boolean verifyOTP(@PathVariable("emailId") String emailId, @PathVariable("otp") String otp){
        return userAccountService.verifyOTP(emailId, otp);
    }

    @GetMapping("/fetch-user/{emailId}")
    public UserAccount fetchUserById(@PathVariable("emailId") String emailId){
        return userAccountService.fetchUserById(emailId);
    }

    @PutMapping("/update-password")
    public boolean updatePassword(@RequestBody UserAccount userAccount){
        return userAccountService.updatePassword(userAccount);
    }

    @PutMapping("/update-other-details")
    public UserAccount updateOtherDetails(@RequestBody UserAccount userAccount){
        return userAccountService.updateOtherDetails(userAccount);
    }
}
