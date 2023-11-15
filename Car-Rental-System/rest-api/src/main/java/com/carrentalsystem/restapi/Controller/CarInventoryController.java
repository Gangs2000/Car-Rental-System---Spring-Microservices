package com.carrentalsystem.restapi.Controller;

import com.carrentalsystem.restapi.Model.CarInventory;
import com.carrentalsystem.restapi.Service.CarInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/car-rental-system/car-inventory")
public class CarInventoryController {
    @Autowired CarInventoryService carInventoryService;

    @PostMapping("/register")
    public boolean registerCarIntoInventory(@RequestBody CarInventory carInventory){
        return carInventoryService.registerNewCar(carInventory);
    }

    @GetMapping("/fetch-car/availability/carId/{carId}/tripStartDate/{tripStartDate}/tripEndDate/{tripEndDate}")
    public int bookTrip(@PathVariable("carId") String carId, @PathVariable("tripStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate tripStartDate, @PathVariable("tripEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate tripEndDate){
        return carInventoryService.isCarAvailable(carId, tripStartDate, tripEndDate);
    }

    @GetMapping("/fetch-car/id/{carId}")
    public CarInventory fetchCarById(@PathVariable("carId") String carId){
        return carInventoryService.fetchCarById(carId);
    }

    @GetMapping("/fetch-cars/email/{mailId}")
    public List<CarInventory> fetchCarsByUserEmail(@PathVariable("mailId") String mailId){
        return carInventoryService.fetchCarsByUserEmail(mailId);
    }

    @GetMapping("/fetch-cars/except/email/{mailId}")
    public List<CarInventory> fetchOtherCarsExceptCurrentEmail(@PathVariable("mailId") String mailId){
        return carInventoryService.fetchOtherCarsExceptCurrentEmail(mailId);
    }

    @GetMapping("/fetch-cars/models")
    public Set<String> fetchCarModels(){
        return carInventoryService.fetchCarModels();
    }

    @GetMapping("/fetch-slots/carId/{carId}")
    public List<List<LocalDate>> fetchSlotsByCarId(@PathVariable("carId") String carId){
        return carInventoryService.fetchSlotsByCarId(carId);
    }

    @GetMapping("/fetch-reviews/carId/{carId}")
    public List<List<String>> fetchCarReviews(@PathVariable("carId") String carId){
        return carInventoryService.fetchCarReviews(carId);
    }

    @GetMapping("/filter-cars/geolocation/{geolocation}/option/{option}")
    public List<CarInventory> fetchCarsByGeoLocation(@PathVariable("geolocation") String geolocation, @PathVariable("option") int option){
        return carInventoryService.fetchCarsByGeoLocation(geolocation, option);
    }

    @PostMapping("/filter-cars/location/exclude/{mailId}")
    public List<CarInventory> fetchCarsByLocation(@RequestBody CarInventory carInventory, @PathVariable("mailId") String emailId){
        return carInventoryService.fetchCarsByLocation(emailId, carInventory);
    }

    @GetMapping("/filter-cars/exclude/{mailId}/price/{price}/option/{option}")
    public List<CarInventory> filterCarsByPrice(@PathVariable("mailId") String emailId, @PathVariable("price") float price, @PathVariable("option") int option){
        return carInventoryService.filterCarsByPrice(emailId, price, option);
    }

    @GetMapping("/filter-cars/price-range/exclude/{mailId}/manufacturer/{carManufacturer}/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public List<CarInventory> filterPriceRange(@PathVariable("mailId") String emailId, @PathVariable("carManufacturer") String carManufacturer, @PathVariable("minPrice") float minPrice, @PathVariable("maxPrice") float maxPrice){
        return carInventoryService.filterPriceRange(emailId, carManufacturer, minPrice, maxPrice);
    }

    @PostMapping("/filter-cars/custom-filter/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public List<CarInventory> filterCarsUsingCustomValue(@RequestBody CarInventory carInventory, @PathVariable("minPrice") float minPrice, @PathVariable("maxPrice") float maxPrice){
        return carInventoryService.filterCarsUsingCustomValue(carInventory, minPrice, maxPrice);
    }

    @PutMapping("/update/car-details")
    public CarInventory updateCarDetails(@RequestBody CarInventory carInventory){
        return carInventoryService.updateCarDetails(carInventory);
    }

    @PutMapping("/update/location-details")
    public CarInventory updateLocationDetails(@RequestBody CarInventory carInventory){
        return carInventoryService.updateLocationDetails(carInventory);
    }

    @PutMapping("/update/rating/carId/{carId}/rate/{rate}")
    public CarInventory updateCarRating(@PathVariable("carId") String carId, @PathVariable("rate") float rate){
        return carInventoryService.updateCarRating(carId, rate);
    }

    @PostMapping("/post/review/carId/{carId}/payload/{payload}")
    public boolean postReview(@PathVariable("carId") String carId, @PathVariable("payload") List<String> payload){
        return carInventoryService.postReview(carId, payload);
    }

    @DeleteMapping("/delete-car/carId/{carId}")
    public boolean deleteCarFromInventory(@PathVariable("carId") String carId){
        return carInventoryService.deleteCarFromInventory(carId);
    }
}
