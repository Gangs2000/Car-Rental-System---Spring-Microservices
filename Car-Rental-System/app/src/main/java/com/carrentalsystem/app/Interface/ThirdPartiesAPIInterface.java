package com.carrentalsystem.app.Interface;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ThirdPartiesAPIInterface {
    ResponseEntity<String> validateEmailId(String emailId);
    ResponseEntity<String> validatePhoneNumber(String phoneNo);
    ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(String postalCode);
}
