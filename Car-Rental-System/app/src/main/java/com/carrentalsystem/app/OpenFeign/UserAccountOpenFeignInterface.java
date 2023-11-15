package com.carrentalsystem.app.OpenFeign;

import com.carrentalsystem.app.Model.UserAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "userAccountAPI", url = "localhost:7070/api/car-rental-system/user")
public interface UserAccountOpenFeignInterface {
    @PostMapping("/register")
    int registerNewUser(@RequestBody UserAccount userAccount);
    @GetMapping("/send-otp/emailId/{emailId}")
    boolean verifyEmailIdAndSendOTP(@PathVariable("emailId") String emailId);
    @GetMapping("/verify-otp/emailId/{emailId}/pin/{otp}")
    boolean verifyOTP(@PathVariable("emailId") String emailId, @PathVariable("otp") String otp);
    @PutMapping("/update-password")
    boolean updatePassword(@RequestBody UserAccount userAccount);
    @GetMapping("/fetch-user/{emailId}")
    UserAccount fetchUserById(@PathVariable("emailId") String emailId);
    @PutMapping("/update-other-details")
    UserAccount updateOtherDetails(@RequestBody UserAccount userAccount);
}
