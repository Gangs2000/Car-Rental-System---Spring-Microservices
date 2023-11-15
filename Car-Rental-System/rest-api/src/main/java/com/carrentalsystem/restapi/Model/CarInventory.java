package com.carrentalsystem.restapi.Model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Document
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarInventory implements Serializable {
    @Id
    private String _id;     //Car number plate ID
    private String ownedBy;
    private String carModel;
    private String manufacturer;
    private int year;
    private float pricePerDay;
    private String locality;
    private String district;
    private String state;
    private String country;
    private int pinCode;
    private List<String> reservationIds;
    private List<List<String>> reviews;
    private List<Float> carRating;
}
