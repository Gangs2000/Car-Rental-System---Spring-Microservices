package com.carrentalsystem.restapi.Service;

import com.carrentalsystem.restapi.Interface.ThirdPartiesAPIInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ThirdPartiesAPIService implements ThirdPartiesAPIInterface {

    RestTemplate restTemplate=new RestTemplate();
    @Override
    public ResponseEntity<String> validateEmailId(String emailId) {
        String apiKey=***************************;
        String emailValidationAPI="https://emailvalidation.abstractapi.com/v1/?api_key="+apiKey+"&email="+emailId;
        String jsonResponse=restTemplate.getForObject(emailValidationAPI,String.class);
        String isDeliverable = new JSONObject(jsonResponse).get("deliverability").toString();
        return new ResponseEntity<>(isDeliverable, (isDeliverable.equals("DELIVERABLE")?(HttpStatus.ACCEPTED):(HttpStatus.BAD_REQUEST)));
    }

    @Override
    public ResponseEntity<String> validatePhoneNumber(String phoneNumber) {
        String apiKey="28b7a48c5b874ec4bc620fdbc15ee088";
        String phoneNumberValidationAPI="https://phonevalidation.abstractapi.com/v1/?api_key="+apiKey+"&phone="+phoneNumber;
        String jsonResponse=restTemplate.getForObject(phoneNumberValidationAPI, String.class);
        String isValid = new JSONObject(jsonResponse).get("valid").toString();
        return new ResponseEntity<>(isValid, (isValid.equals("true")?(HttpStatus.ACCEPTED):(HttpStatus.BAD_REQUEST)));
    }

    @Override
    public ResponseEntity<Map<String, List<String>>> fetchLocationDetailsByPostalCode(String postalCode) {
        Map<String, List<String>> locationMap=new HashMap<>();
        String userName="gangs";                //API requires an username..
        String fetchGeoLocationAPI="http://api.geonames.org/postalCodeLookupJSON?postalcode="+postalCode+"&country=IN&username="+userName;
        String jsonResponse=restTemplate.getForObject(fetchGeoLocationAPI, String.class);
        JSONArray postalCodes = new JSONObject(jsonResponse).getJSONArray("postalcodes");
        for(int i=0;i<postalCodes.length();i++){
            String placeName=postalCodes.getJSONObject(i).get("placeName").toString();
            String district=postalCodes.getJSONObject(i).getString("adminName2").toString();
            String state=postalCodes.getJSONObject(i).getString("adminName1").toString();
            pushPlacesAndDistrictDetailsIntoMap(locationMap, "places", placeName);
            pushPlacesAndDistrictDetailsIntoMap(locationMap, "districts", district);
            pushPlacesAndDistrictDetailsIntoMap(locationMap, "state", state);
        }
        return new ResponseEntity<>(locationMap, HttpStatus.OK);
    }

    @Override
    public void pushPlacesAndDistrictDetailsIntoMap(Map<String, List<String>> locationMap, String key, String value) {
        locationMap.putIfAbsent(key, new LinkedList<>());
        List<String> places=locationMap.get(key);
        if(!places.contains(value))
            places.add(value);
        locationMap.put(key, places);
    }
}
