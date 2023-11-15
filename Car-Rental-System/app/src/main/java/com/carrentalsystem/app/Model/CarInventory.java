package com.carrentalsystem.app.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarInventory implements Serializable {
    private String _id;     //Car number plate ID
    private String ownedBy;
    private String carModel;
    private String manufacturer;
    //Need to add one variable for poster
    private int year;
    private float pricePerDay;
    private String locality;
    private String district;
    private String state;
    private String country;
    private int pinCode;
    private List<String> reservationIds;
    private List<Float> carRating;
}
