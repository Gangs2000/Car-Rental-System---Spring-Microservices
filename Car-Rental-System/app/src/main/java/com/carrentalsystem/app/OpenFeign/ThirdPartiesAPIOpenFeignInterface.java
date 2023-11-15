package com.carrentalsystem.app.OpenFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "thirdPartyAPI", url = "localhost:7070/api/car-rental-system")
public interface ThirdPartiesAPIOpenFeignInterface {

    @GetMapping("/validate/emailId/{emailId}")
    ResponseEntity<String> validateEmailId(@PathVariable("emailId") String emailId);
    @GetMapping("/validate/phoneNo/{phoneNo}")
    ResponseEntity<String> validatePhoneNumber(@PathVariable("phoneNo") String phoneNo);
    @GetMapping("/fetch-location-details/postalCode/{postalCode}")
    ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(@PathVariable("postalCode") String postalCode);
}
