package com.carrentalsystem.restapi.Interface;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ThirdPartiesAPIInterface {
    ResponseEntity<String> validateEmailId(String emailId);
    ResponseEntity<String> validatePhoneNumber(String phoneNumber);
    ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(String postalCode);
    void pushPlacesAndDistrictDetailsIntoMap(Map<String, List<String>> locationMap, String key, String value);
}
