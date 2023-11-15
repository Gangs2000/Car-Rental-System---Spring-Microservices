package com.carrentalsystem.app.Service;

import com.carrentalsystem.app.Configuration.UserAccountRepository;
import com.carrentalsystem.app.Interface.UserAccountInterface;
import com.carrentalsystem.app.Model.UserAccount;
import com.carrentalsystem.app.OpenFeign.UserAccountOpenFeignInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Service
public class UserAccountService implements UserAccountInterface {

    @Autowired UserAccount userAccount;
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired AuthenticationProvider authenticationProvider;
    @Autowired UserAccountOpenFeignInterface userAccountOpenFeignInterface;
    RedirectView redirectView=new RedirectView();
    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);

    @Override
    public int registerNewUser(Map<String, Object> mapper) {
        //Validate Email ID first
        userAccount.setEmailId(mapper.get("emailId").toString());
        userAccount.setPassword(bCryptPasswordEncoder.encode(mapper.get("password").toString()));
        userAccount.setUserName(mapper.get("userName").toString());
        try {
            long mobileNumber=Long.valueOf(mapper.get("phoneNo").toString());
            userAccount.setMobileNumber(mobileNumber);
        }
        catch (NumberFormatException exception){
            return 3;
        }
        userAccount.setCity(mapper.get("locality").toString());
        userAccount.setDistrict(mapper.get("district").toString());
        userAccount.setState(mapper.get("state").toString());
        userAccount.setCountry("IN");
        userAccount.setPinCode(Integer.valueOf(mapper.get("postalCode").toString()));
        return userAccountOpenFeignInterface.registerNewUser(userAccount);
    }

    @Override
    public boolean sendOTP(String emailId) {
        return userAccountOpenFeignInterface.verifyEmailIdAndSendOTP(emailId);
    }

    @Override
    public boolean verifyOTP(String emailId, String otp) {
        try{
            int checkOTPIsInt=Integer.valueOf(otp);
            otp=String.valueOf(checkOTPIsInt);
            return userAccountOpenFeignInterface.verifyOTP(emailId, otp);
        }
        catch(NumberFormatException exception){
            return false;
        }
    }

    @Override
    public boolean updatePassword(Map<String, Object> mapper) {
        userAccount.setEmailId(mapper.get("emailId").toString());
        userAccount.setPassword(bCryptPasswordEncoder.encode(String.valueOf(mapper.get("password").toString())));
        return userAccountOpenFeignInterface.updatePassword(userAccount);
    }

    @Override
    public RedirectView validateLogin(HttpServletRequest httpServletRequest, HttpSession httpSession) {
        String emailId=httpServletRequest.getParameter("emailId").toString();
        String password=httpServletRequest.getParameter("password").toString();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(emailId, password);
        if(userAccountRepository.existsById(emailId)){
            userAccount=userAccountRepository.findById(emailId).get();
            if(bCryptPasswordEncoder.matches(password, userAccount.getPassword())){
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                Authentication authentication= this.authenticationProvider.authenticate(usernamePasswordAuthenticationToken);
                SecurityContext securityContext= SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                //Main line here only we are manually setting session as true
                httpServletRequest.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                httpSession.setAttribute("sessionUserName", userAccount.getUserName());
                httpSession.setAttribute("sessionPassword", password);
                httpSession.setAttribute("sessionEmailId", userAccount.getEmailId());
                httpSession.setAttribute("sessionLocality", userAccount.getCity());
                httpSession.setAttribute("sessionDistrict", userAccount.getDistrict());
                httpSession.setAttribute("sessionState", userAccount.getState());
                httpSession.setAttribute("sessionPinCode", userAccount.getPinCode());
                redirectView.setUrl("/car-rental-system/app/dashboard");
                return redirectView;
            }
        }
        redirectView.setUrl("/car-rental-system/app/login-failure");
        return redirectView;
    }

    @Override
    public UserAccount fetchUserById(String emailId) {
        return userAccountOpenFeignInterface.fetchUserById(emailId);
    }

    @Override
    public UserAccount updateProfileDetails(Map<String, Object> mapper) {
        userAccount.setEmailId(mapper.get("emailId").toString());
        userAccount.setUserName(mapper.get("userName").toString());
        userAccount.setMobileNumber(Long.valueOf(mapper.get("phoneNo").toString()));
        userAccount.setPinCode(Integer.valueOf(mapper.get("postalCode").toString()));
        userAccount.setCity(mapper.get("locality").toString());
        userAccount.setDistrict(mapper.get("district").toString());
        userAccount.setState(mapper.get("state").toString());
        return userAccountOpenFeignInterface.updateOtherDetails(userAccount);
    }
}
