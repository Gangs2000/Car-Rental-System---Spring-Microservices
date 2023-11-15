package com.carrentalsystem.app.Service;

import com.carrentalsystem.app.Interface.ThirdPartiesAPIInterface;
import com.carrentalsystem.app.OpenFeign.ThirdPartiesAPIOpenFeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ThirdPartiesAPIService implements ThirdPartiesAPIInterface {

    @Autowired ThirdPartiesAPIOpenFeignInterface thirdPartiesAPIOpenFeignInterface;

    @Override
    public ResponseEntity<String> validateEmailId(String emailId) {
        return thirdPartiesAPIOpenFeignInterface.validateEmailId(emailId);
    }

    @Override
    public ResponseEntity<String> validatePhoneNumber(String phoneNo) {
        return thirdPartiesAPIOpenFeignInterface.validatePhoneNumber(phoneNo);
    }

    @Override
    public ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(String postalCode) {
        return thirdPartiesAPIOpenFeignInterface.fetchLocationDetailsByPostalCode(postalCode);
    }
}
