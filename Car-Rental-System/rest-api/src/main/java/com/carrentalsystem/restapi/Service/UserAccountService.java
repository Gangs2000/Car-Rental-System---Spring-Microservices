package com.carrentalsystem.restapi.Service;

import com.carrentalsystem.restapi.Config.RabbitMqConfig;
import com.carrentalsystem.restapi.Interface.UserAccountInterface;
import com.carrentalsystem.restapi.Model.OTPBucket;
import com.carrentalsystem.restapi.Model.UserAccount;
import com.carrentalsystem.restapi.Repository.OTPBucketRepository;
import com.carrentalsystem.restapi.Repository.UserAccountRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserAccountService implements UserAccountInterface {
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired ThirdPartiesAPIService thirdPartiesAPIService;
    @Autowired UserAccount existingUserAccount;
    @Autowired OTPBucketRepository otpBucketRepository;
    @Autowired OTPBucket existingOTPBucket;
    @Autowired RabbitTemplate rabbitTemplate;

    @Override
    public int createNewUser(UserAccount userAccount) {
        //Before creating an account need to verify the email ID and phone number is valid
        if(!userAccountRepository.existsById(userAccount.getEmailId())){
            if(thirdPartiesAPIService.validateEmailId(userAccount.getEmailId()).getBody().equals("DELIVERABLE")) {
                if (thirdPartiesAPIService.validatePhoneNumber(String.valueOf(userAccount.getMobileNumber())).getBody().equals("true")) {
                    userAccount.setLastUpdated(LocalDateTime.now());
                    userAccountRepository.saveAndFlush(userAccount);
                    return 4;
                }
                else
                    return 2;
            }
            else
                return 3;
        }
        return 1;
    }

    @Override
    public boolean sendOTP(String emailId) {
        if(userAccountRepository.existsById(emailId)){
            int minRange=100000;
            int maxRange=999999;
            String generatedPin=String.format("%06d", new Random().nextInt(maxRange-minRange+1)+minRange);
            if(otpBucketRepository.existsById(emailId)){
                existingOTPBucket=otpBucketRepository.findById(emailId).get();
                existingOTPBucket.setOtp(generatedPin);
            }
            else{
                existingOTPBucket.setEmailId(emailId);
                existingOTPBucket.setOtp(generatedPin);
            }
            otpBucketRepository.saveAndFlush(existingOTPBucket);
            //Sending data to RabbitMQ for OTP email trigger process
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY3, emailId);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyOTP(String emailId, String otp) {
        existingOTPBucket=otpBucketRepository.findById(emailId).get();
        return (existingOTPBucket.getOtp().equals(otp));
    }

    @Override
    public UserAccount fetchUserById(String emailId) {
        return userAccountRepository.findById(emailId).orElse(null);
    }

    @Override
    public boolean updatePassword(UserAccount userAccount) {
        if(userAccountRepository.existsById(userAccount.getEmailId())){
            existingUserAccount=userAccountRepository.findById(userAccount.getEmailId()).get();
            existingUserAccount.setPassword(userAccount.getPassword());
            userAccountRepository.saveAndFlush(existingUserAccount);
            return true;
        }
        return false;
    }

    @Override
    public UserAccount updateOtherDetails(UserAccount userAccount) {
        if(userAccountRepository.existsById(userAccount.getEmailId())){
            existingUserAccount=userAccountRepository.findById(userAccount.getEmailId()).get();
            existingUserAccount.setUserName(userAccount.getUserName());
            existingUserAccount.setMobileNumber(userAccount.getMobileNumber());
            existingUserAccount.setPinCode(userAccount.getPinCode());
            existingUserAccount.setCity(userAccount.getCity());
            existingUserAccount.setDistrict(userAccount.getDistrict());
            existingUserAccount.setState(userAccount.getState());
            existingUserAccount.setCountry("IN");
            return userAccountRepository.saveAndFlush(existingUserAccount);
        }
        return null;
    }
}
