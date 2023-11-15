package com.carrentalsystem.restapi.Controller;

import com.carrentalsystem.restapi.Service.ThirdPartiesAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/car-rental-system")
public class ThirdPartiesAPIController {

    @Autowired ThirdPartiesAPIService thirdPartiesAPIService;

    @GetMapping("/validate/emailId/{emailId}")
    public ResponseEntity<String> validateEmailId(@PathVariable("emailId") String emailId){
        return thirdPartiesAPIService.validateEmailId(emailId);
    }

    @GetMapping("/validate/phoneNo/{phoneNo}")
    public ResponseEntity<String> validatePhoneNumber(@PathVariable("phoneNo") String phoneNo){
        return thirdPartiesAPIService.validatePhoneNumber(phoneNo);
    }

    @GetMapping("/fetch-location-details/postalCode/{postalCode}")
    public ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(@PathVariable("postalCode") String postalCode){
        return thirdPartiesAPIService.fetchLocationDetailsByPostalCode(postalCode);
    }
}
