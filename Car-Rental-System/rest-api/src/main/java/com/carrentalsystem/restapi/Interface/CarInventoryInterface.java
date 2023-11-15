package com.carrentalsystem.restapi.Interface;

import com.carrentalsystem.restapi.Model.CarInventory;

import java.time.LocalDate;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public interface CarInventoryInterface {
    boolean registerNewCar(CarInventory carInventory);
    int isCarAvailable(String carId, LocalDate tripStartDate, LocalDate tripEndDate);
    CarInventory fetchCarById(String carId);
    List<CarInventory> fetchCarsByUserEmail(String mailId);
    List<CarInventory> fetchOtherCarsExceptCurrentEmail(String emailId);
    List<List<LocalDate>> fetchSlotsByCarId(String carId);
    Set<String> fetchCarModels();
    List<CarInventory> fetchCarsByGeoLocation(String geoLocationInfo, int option);
    List<CarInventory> fetchCarsByLocation(String emailId, CarInventory carInventory);
    List<List<String>> fetchCarReviews(String carId);
    List<CarInventory> filterCarsByPrice(String emailId, float price, int option);
    List<CarInventory> filterPriceRange(String emailId, String carModel, float minPrice, float maxPrice);
    List<CarInventory> filterCarsUsingCustomValue(CarInventory carInventory, float minPrice, float maxPrice);
    CarInventory updateCarDetails(CarInventory carInventory);
    CarInventory updateLocationDetails(CarInventory carInventory);
    CarInventory updateCarRating(String carId, float rate);
    boolean postReview(String carId, List<String> payload);
    boolean deleteCarFromInventory(String carId);
    int tripSlotValidation(PriorityQueue<List<LocalDate>> fetchAllBookedTripDates, LocalDate tripStartDate, LocalDate tripEndDate);
}

