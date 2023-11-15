package com.carrentalsystem.app.OpenFeign;

import com.carrentalsystem.app.Model.CarInventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FeignClient(value = "carInventory", url = "localhost:7070/api/car-rental-system/car-inventory")
public interface CarInventoryOpenFeignInterface {
    @PostMapping("/register")
    boolean registerCarIntoInventory(@RequestBody CarInventory carInventory);
    @GetMapping("/fetch-car/id/{carId}")
    CarInventory fetchCarById(@PathVariable("carId") String carId);
    @GetMapping("/fetch-car/availability/carId/{carId}/tripStartDate/{tripStartDate}/tripEndDate/{tripEndDate}")
    int bookTrip(@PathVariable("carId") String carId, @PathVariable("tripStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate tripStartDate, @PathVariable("tripEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate tripEndDate);
    @GetMapping("/fetch-cars/email/{mailId}")
    List<CarInventory> fetchCarsByUserEmail(@PathVariable("mailId") String mailId);
    @GetMapping("/fetch-cars/except/email/{mailId}")
    List<CarInventory> fetchOtherCarsExceptCurrentEmail(@PathVariable("mailId") String mailId);
    @GetMapping("/fetch-cars/models")
    Set<String> fetchCarModels();
    @GetMapping("/filter-cars/exclude/{mailId}/price/{price}/option/{option}")
    List<CarInventory> filterCarsByPrice(@PathVariable("mailId") String emailId, @PathVariable("price") float price, @PathVariable("option") int option);
    @GetMapping("/filter-cars/price-range/exclude/{mailId}/manufacturer/{carManufacturer}/minPrice/{minPrice}/maxPrice/{maxPrice}")
    List<CarInventory> filterPriceRange(@PathVariable("mailId") String emailId, @PathVariable("carManufacturer") String carManufacturer, @PathVariable("minPrice") float minPrice, @PathVariable("maxPrice") float maxPrice);
    @PostMapping("/filter-cars/location/exclude/{mailId}")
    List<CarInventory> fetchCarsByLocation(@RequestBody CarInventory carInventory, @PathVariable("mailId") String emailId);
    @GetMapping("/fetch-slots/carId/{carId}")
    List<List<LocalDate>> fetchSlotsByCarId(@PathVariable("carId") String carId);
    @GetMapping("/fetch-reviews/carId/{carId}")
    List<List<String>> fetchCarReviews(@PathVariable("carId") String carId);
    @PutMapping("/update/car-details")
    CarInventory updateCarDetails(@RequestBody CarInventory carInventory);
    @PutMapping("/update/location-details")
    CarInventory updateLocationDetails(@RequestBody CarInventory carInventory);
    @PutMapping("/update/rating/carId/{carId}/rate/{rate}")
    CarInventory updateCarRating(@PathVariable("carId") String carId, @PathVariable("rate") float rate);
    @PostMapping("/post/review/carId/{carId}/payload/{payload}")
    boolean postReview(@PathVariable("carId") String carId, @PathVariable("payload") List<String> payload);
    @DeleteMapping("/delete-car/carId/{carId}")
    boolean deleteCarFromInventory(@PathVariable("carId") String carId);
}
